package com.fudo.identity.controller;

import com.fudo.identity.application.dto.command.ChangePasswordCommand;
import com.fudo.identity.application.dto.command.LoginCommand;
import com.fudo.identity.application.dto.command.LogoutCommand;
import com.fudo.identity.application.dto.command.RefreshTokenCommand;
import com.fudo.identity.application.dto.command.RegisterCommand;
import com.fudo.identity.application.dto.query.GetUserQuery;
import com.fudo.shared.cqrs.Mediator;
import com.fudo.shared.exception.NotFoundException;
import com.fudo.shared.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Mediator mediator;

    public AuthController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterCommand.RegisterResult> register(@Valid @RequestBody RegisterCommand cmd) {
        return ResponseEntity.ok(mediator.send(cmd));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginCommand.LoginResult> login(@Valid @RequestBody LoginCommand cmd) {
        return ResponseEntity.ok(mediator.send(cmd));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenCommand.RefreshTokenResult> refresh(@Valid @RequestBody RefreshTokenCommand cmd) {
        return ResponseEntity.ok(mediator.send(cmd));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutCommand cmd) {
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordCommand cmd) {
        cmd.setUserId(CurrentUser.require().userId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserQuery.UserDto> getMe() {
        var userId = CurrentUser.require().userId();
        GetUserQuery.UserDto result = mediator.send(new GetUserQuery(userId));
        if (result == null) {
            throw new NotFoundException("User", userId);
        }
        return ResponseEntity.ok(result);
    }
}

package com.fudo.story.features.handler.command;

import com.fudo.story.application.dto.command.CreateStoryCommand;
import com.fudo.story.application.dto.command.CreateStoryCommand.CreateStoryResult;
import com.fudo.story.entity.Story;
import com.fudo.story.interfaces.IStoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateStoryHandlerTest {

    @Mock
    private IStoryRepository stories;

    @Test
    void handle_createsStoryWithTwentyFourHourExpiry_andDelegatesToRepository() {
        CreateStoryHandler handler = new CreateStoryHandler(stories);

        UUID authorId = UUID.randomUUID();
        CreateStoryCommand cmd = new CreateStoryCommand();
        cmd.setAuthorId(authorId);
        cmd.setMediaUrl("https://cdn.fudo.dev/media/abc.jpg");
        cmd.setMediaType("image");
        cmd.setCaption("hello world");

        Instant before = Instant.now();
        CreateStoryResult result = handler.handle(cmd);
        Instant after = Instant.now();

        ArgumentCaptor<Story> captor = ArgumentCaptor.forClass(Story.class);
        verify(stories).add(captor.capture());

        Story saved = captor.getValue();
        assertThat(saved.getAuthorId()).isEqualTo(authorId);
        assertThat(saved.getMediaUrl()).isEqualTo("https://cdn.fudo.dev/media/abc.jpg");
        assertThat(saved.getMediaType()).isEqualTo("image");
        assertThat(saved.getCaption()).isEqualTo("hello world");

        // ExpiresAt should be ~24h from now (allow the before/after window for execution time).
        assertThat(saved.getExpiresAt()).isAfter(before.plusSeconds(24 * 3600 - 5));
        assertThat(saved.getExpiresAt()).isBefore(after.plusSeconds(24 * 3600 + 5));

        assertThat(result.getStoryId()).isEqualTo(saved.getId());
        assertThat(result.getExpiresAt()).isEqualTo(saved.getExpiresAt());
    }
}

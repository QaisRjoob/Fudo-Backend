package com.fudo.story.features.handler.command;

import com.fudo.story.application.dto.command.DeleteStoryCommand;
import com.fudo.story.entity.Story;
import com.fudo.story.interfaces.IStoryRepository;
import com.fudo.shared.exception.ForbiddenException;
import com.fudo.shared.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteStoryHandlerTest {

    @Mock
    private IStoryRepository stories;

    @Test
    void handle_deletesStory_whenRequestingUserIsAuthor() {
        DeleteStoryHandler handler = new DeleteStoryHandler(stories);

        UUID storyId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        Story story = new Story();
        story.setId(storyId);
        story.setAuthorId(authorId);

        when(stories.getById(storyId)).thenReturn(Optional.of(story));

        DeleteStoryCommand cmd = new DeleteStoryCommand();
        cmd.setStoryId(storyId);
        cmd.setRequestingUserId(authorId);

        handler.handle(cmd);

        verify(stories).delete(storyId);
    }

    @Test
    void handle_throwsForbidden_whenRequestingUserIsNotAuthor() {
        DeleteStoryHandler handler = new DeleteStoryHandler(stories);

        UUID storyId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        Story story = new Story();
        story.setId(storyId);
        story.setAuthorId(authorId);

        when(stories.getById(storyId)).thenReturn(Optional.of(story));

        DeleteStoryCommand cmd = new DeleteStoryCommand();
        cmd.setStoryId(storyId);
        cmd.setRequestingUserId(otherUserId);

        assertThatThrownBy(() -> handler.handle(cmd)).isInstanceOf(ForbiddenException.class);

        verify(stories).getById(storyId);
        verifyNoMoreInteractions(stories);
    }

    @Test
    void handle_throwsNotFound_whenStoryDoesNotExist() {
        DeleteStoryHandler handler = new DeleteStoryHandler(stories);

        UUID storyId = UUID.randomUUID();
        when(stories.getById(storyId)).thenReturn(Optional.empty());

        DeleteStoryCommand cmd = new DeleteStoryCommand();
        cmd.setStoryId(storyId);
        cmd.setRequestingUserId(UUID.randomUUID());

        assertThatThrownBy(() -> handler.handle(cmd)).isInstanceOf(NotFoundException.class);
    }
}

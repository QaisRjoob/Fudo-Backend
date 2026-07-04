package com.fudo.post.features.handler.command;

import com.fudo.post.application.dto.command.CreatePostCommand;
import com.fudo.post.application.dto.command.CreatePostCommand.CreatePostResult;
import com.fudo.post.application.dto.command.CreatePostCommand.IngredientDto;
import com.fudo.post.application.dto.command.CreatePostCommand.StepDto;
import com.fudo.post.entity.Post;
import com.fudo.post.interfaces.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePostHandlerTest {

    @Mock
    private PostRepository posts;

    private CreatePostHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreatePostHandler(posts);
    }

    @Test
    void handle_buildsPostWithOrderedChildrenAndNormalizedFields() {
        CreatePostCommand cmd = new CreatePostCommand();
        UUID authorId = UUID.randomUUID();
        cmd.setAuthorId(authorId);
        cmd.setTitle("  My Recipe  ");
        cmd.setDescription("desc");
        cmd.setCookingTime("30m");
        cmd.setDifficulty("easy");
        cmd.setServingSize(4);
        cmd.setMediaUrls(List.of("url1", "url2"));
        cmd.setTags(List.of("  Spicy ", "VEGAN"));
        cmd.setIngredients(List.of(new IngredientDto("Flour", "2", "cups")));
        cmd.setSteps(List.of(new StepDto(1, "Mix", null)));

        when(posts.add(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreatePostResult result = handler.handle(cmd);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(posts).add(captor.capture());
        Post saved = captor.getValue();

        assertThat(saved.getAuthorId()).isEqualTo(authorId);
        assertThat(saved.getTitle()).isEqualTo("My Recipe");
        assertThat(saved.getMedia()).hasSize(2);
        assertThat(saved.getMedia().get(0).getSortOrder()).isZero();
        assertThat(saved.getMedia().get(1).getSortOrder()).isEqualTo(1);
        assertThat(saved.getTags()).extracting("tag").containsExactly("spicy", "vegan");
        assertThat(saved.getIngredients()).hasSize(1);
        assertThat(saved.getIngredients().get(0).getName()).isEqualTo("Flour");
        assertThat(saved.getSteps()).hasSize(1);
        assertThat(saved.getSteps().get(0).getStepNumber()).isEqualTo(1);

        assertThat(result.getPostId()).isEqualTo(saved.getId());
    }
}

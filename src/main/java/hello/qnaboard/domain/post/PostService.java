package hello.qnaboard.domain.post;

import hello.qnaboard.web.post.PostUpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    public Post save(Post post) {
        postMapper.save(post);
        return post;
    }

    public Optional<PostDetailDto> findById(Long postId) {
        return postMapper.findById(postId);
    }

    public void updateViewCount(Long postId) {
        postMapper.updateViewCount(postId);
    }

    public void updatePost(PostUpdateDto updateDto) {
        postMapper.updatePost(updateDto);
    }

    public void delete(Long postId) {
        postMapper.delete(postId);
    }
}

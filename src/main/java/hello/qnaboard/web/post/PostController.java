package hello.qnaboard.web.post;

import hello.qnaboard.domain.comment.CommentResponseDto;
import hello.qnaboard.domain.comment.CommentService;
import hello.qnaboard.domain.common.Pagination;
import hello.qnaboard.domain.common.PageRequest;
import hello.qnaboard.domain.member.Member;
import hello.qnaboard.domain.post.*;
import hello.qnaboard.web.login.SessionConst;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    @GetMapping
    public String list(@ModelAttribute PageRequest pageRequest,
                       @ModelAttribute("cond") PostSearchCond cond,
                       @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       Model model) {

        if (loginMember != null) {
            model.addAttribute("member", loginMember);
        }

        List<PostDetailDto> posts = postService.findAll(pageRequest, cond);
        int totalPostCount = postService.countAll();
        Pagination pagination = new Pagination(totalPostCount, pageRequest);

        model.addAttribute("posts", posts);
        model.addAttribute("pagination", pagination);
        return "posts/list";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("post") PostSaveForm form) {
        return "posts/write";
    }

    @PostMapping("/add")
    public String savePost(@Validated @ModelAttribute("post") PostSaveForm form,
                           BindingResult bindingResult,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {

        if (bindingResult.hasErrors()) {
            return "posts/write";
        }

        Post post = new Post(loginMember.getMemberId(), form.getTitle(), form.getContent());
        postService.save(post);

        return "redirect:/posts/" + post.getPostId();
    }

    @GetMapping("/{postId}")
    public String postDetail(@PathVariable("postId") Long postId,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {

        // 사용자 본인이 읽은 글 목록을 담고 있는 쿠키 찾기
        Cookie[] cookies = request.getCookies();
        Cookie viewCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("viewedPosts")) {
                    viewCookie = cookie;
                    break;
                }
            }
        }

        // 쿠키 검증 및 조회수 증가 여부 결정
        if (viewCookie != null) {
            if (!viewCookie.getValue().contains("[" + postId + "]")) {
                // 쿠키는 있지만 지금 접속하려는 글의 postId가 포함되어 있지 않다면 쿠키에 추가
                postService.updateViewCount(postId);
                viewCookie.setValue(viewCookie.getValue() + "[" + postId + "]");
                viewCookie.setPath("/");
                viewCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(viewCookie);
            }
        } else { // 접속하고 처음 읽은 게시글이므로 쿠키 생성 후 추가
            postService.updateViewCount(postId);
            Cookie newCookie = new Cookie("viewedPosts", "[" + postId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }

        // DB에서 게시글 정보 불러오기
        PostDetailDto postDetail = postService.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        List<CommentResponseDto> comments = commentService.findAllByPostId(postId);

        model.addAttribute("post", postDetail);
        model.addAttribute("comments", comments);

        return "posts/detail";
    }

    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable("postId") Long postId,
                           @SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           Model model)
    {
        PostDetailDto post = postService.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (loginMember == null || !post.getMemberId().equals(loginMember.getMemberId()))
            return "redirect:/";

        PostUpdateForm form = new PostUpdateForm();
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());

        model.addAttribute("post", form);
        return "posts/write";
    }

    @PostMapping("/{postId}/edit")
    public String editPost(@PathVariable("postId") Long postId,
                           @Valid @ModelAttribute("post") PostUpdateForm form,
                           BindingResult bindingResult,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           Model model)
    {
        if (bindingResult.hasErrors()) {
            return "posts/write";
        }

        PostDetailDto post = postService.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (loginMember == null || !post.getMemberId().equals(loginMember.getMemberId()))
            return "redirect:/";

        PostUpdateDto updateDto = new PostUpdateDto();
        updateDto.setPostId(postId);
        updateDto.setTitle(form.getTitle());
        updateDto.setContent(form.getContent());

        postService.updatePost(updateDto);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/delete")
    public String deleteForm(@PathVariable("postId") Long postId,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember)
    {
        // 현재 로그인한 사람이 실제 이 글의 작성자가 맞는지 확인
        PostDetailDto post = postService.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 권한이 없는 사람이 URL로 직접 삭제를 시도한 경우 메인 페이지로 보내기
        if (loginMember == null || !post.getMemberId().equals(loginMember.getMemberId()))
            return "redirect:/";

        postService.delete(postId);

        return "redirect:/";
    }
}

package hello.qnaboard.web.comment;

import hello.qnaboard.domain.comment.Comment;
import hello.qnaboard.domain.comment.CommentResponseDto;
import hello.qnaboard.domain.comment.CommentService;
import hello.qnaboard.domain.member.Member;
import hello.qnaboard.web.login.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute("commentForm") CommentSaveForm form,
                       BindingResult bindingResult,
                       @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {

        if (bindingResult.hasErrors()) {
            return "redirect:/{}";
        }

        Comment comment = new Comment();
        comment.setPostId(form.getPostId());
        comment.setContent(form.getContent());
        comment.setMemberId(loginMember.getMemberId());

        commentService.save(comment);
        return "redirect:/posts/" + form.getPostId();
    }

    @PostMapping("{commentId}/delete")
    public String delete(@PathVariable Long commentId, @RequestParam Long postId,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                         RedirectAttributes redirectAttributes) {

        Comment comment = commentService.findById(commentId).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (loginMember == null || !comment.getMemberId().equals(loginMember.getMemberId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 댓글의 삭제 권한이 없습니다.");
            return "redirect:/posts";
        }

        commentService.delete(commentId);
        return "redirect:/posts/" + postId;
    }
}

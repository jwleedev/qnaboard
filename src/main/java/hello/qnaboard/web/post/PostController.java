package hello.qnaboard.web.post;

import hello.qnaboard.domain.member.Member;
import hello.qnaboard.web.login.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/posts")
public class PostController {

    @GetMapping
    public String List(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       Model model) {

        if (loginMember != null) {
            model.addAttribute("member", loginMember);
        }

        return "posts/list";
    }

    @GetMapping("/{postId}")
    public String detail(@PathVariable("postId") Long id) {
        return "posts/detail";
    }

    @GetMapping("/add")
    public String addForm() {
        return "posts/write";
    }

    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable("postId") Long id, Model model) {
        return "posts/write";
    }
}

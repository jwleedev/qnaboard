package hello.qnaboard.web.member;

import hello.qnaboard.domain.member.Member;
import hello.qnaboard.domain.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String addForm(@ModelAttribute("member") Member member) {
        return "members/signup";
    }

    @PostMapping("/signup")
    public String saveMember(@Valid @ModelAttribute("member") Member member, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/signup";
        }

        try {
            memberService.save(member);
        } catch (IllegalStateException e) {
            bindingResult.rejectValue("email", "duplicate");
            return "members/signup";
        }

        return "redirect:/posts";
    }
}

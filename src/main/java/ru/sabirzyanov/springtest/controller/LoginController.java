package ru.sabirzyanov.springtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sabirzyanov.springtest.domain.User;
import ru.sabirzyanov.springtest.service.UserService;

import java.util.Map;


//TODO вывод сообщения о том, что email не активирован
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:user/profile";
        }

        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            BindingResult bindingResult,
                            Model model
    ) {
        User user = userService.findUser(username);

        if (user == null) {
            model.addAttribute("loginError", "login or password is incorrect");
            return "login";
        } else if(!user.getPassword().equals(userService.encodedPassword(password))) {
            model.addAttribute("loginError", "login or password is incorrect");
            return "login";
        }

        if (user.getActivationCode() != null) {
            model.addAttribute("activationCodeError", "Email not activated");
            return "login";
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "login";
        }
        return "redirect:/user/profile";
    }

}

package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Resource(name="authenticationManager")
    private AuthenticationManager authManager;

    @RequestMapping("/login")
    public String login(@RequestParam(name = "error", required = false) Boolean error, Model model) {
        if(Boolean.TRUE.equals(error)) {
            model.addAttribute("error", "true");
        }
        return "login";
    }

    @GetMapping("/driver")
    public String driverPage() {
        return "driver_menu";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin_menu";
    }

    @RequestMapping(value = "/autenticateTheUser", method = RequestMethod.POST)
    public void login(@RequestParam("username") final String username, @RequestParam("password") final String password, final HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
    }

    @GetMapping("/")
    public String redirection(Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        if (user.getAuthority().getAuthority().toString().equals("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else {
            return "redirect:/driver";
        }
    }
}













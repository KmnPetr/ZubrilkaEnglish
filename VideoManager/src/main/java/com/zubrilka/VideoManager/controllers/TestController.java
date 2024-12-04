package com.zubrilka.VideoManager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * тестовый контроллер для проверки связи секьюрити и другого
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String,String>> sayHello(
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails3
            /*,UsernamePasswordAuthenticationToken principal*/
    ){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails2 = (UserDetails) ((Authentication)request.getUserPrincipal()).getPrincipal();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "greeting", "Hello1, %s!".formatted(userDetails),
                        "greeting2", "Hello2, %s!".formatted(userDetails2),
                        "greeting3", "Hello3, %s!".formatted(userDetails2)/*,
                        "greeting4", "Hello4, %s!".formatted(principal.getName())*/
                ));
    }
    @GetMapping("/user")
    public String privacyUser(){
        return "privacyUser";
    }
    @GetMapping("/admin")
    public String privacyAdmin(){
        return "privacyAdmin";
    }
    @GetMapping("/free")
    public String freePage(){
        return "Free Page";
    }
}
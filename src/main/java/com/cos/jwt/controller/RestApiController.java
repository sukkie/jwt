package com.cos.jwt.controller;

import com.cos.jwt.model.UserModel;
import com.cos.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("/token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody UserModel userModel) {
        String rawPassword = userModel.getPassword();
        String encPassrod = bCryptPasswordEncoder.encode(rawPassword);
        userModel.setPassword(encPassrod);
        userModel.setRoles("ROLE_USER");
        userRepository.save(userModel); // 패스워드 암호화 필요.

        return "회원가입완료";
    }

    // admin, manager, user
    @GetMapping("/api/v1/user")
    public String user() {
        return "<h1>user</h1>";
    }

    // admin, manager
    @GetMapping("/api/v1/manager")
    public String manager() {
        return "<h1>user</h1>";
    }

    // admin
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "<h1>user</h1>";
    }
}

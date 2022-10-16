package com.cos.jwt.config.auth;

import com.cos.jwt.model.UserModel;
import com.cos.jwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// formLogin().disable() 로 localhost/login 이 동작 안함.
@Service
@AllArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // Security Session(Authentication(UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUsername(username);
        if (userModel != null) {
            return new PrincipalDetails(userModel);
        }
        return null;
    }
}

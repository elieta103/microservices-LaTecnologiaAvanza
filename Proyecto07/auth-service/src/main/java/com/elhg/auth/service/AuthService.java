package com.elhg.auth.service;

import com.elhg.auth.dto.AuthUserDto;
import com.elhg.auth.dto.NewUserDto;
import com.elhg.auth.dto.RequestDto;
import com.elhg.auth.dto.TokenDto;
import com.elhg.auth.entity.AuthUser;
import com.elhg.auth.repository.AuthUserRepository;
import com.elhg.auth.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public AuthUser save(NewUserDto dto){
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
        if(user.isPresent()){
            return null;
        }
        String password = passwordEncoder.encode(dto.getPassword());
        AuthUser authUser = AuthUser.builder()
                .userName(dto.getUserName())
                .password(password)
                .role(dto.getRole())
                .build();
        return authUserRepository.save(authUser);
    }

    public TokenDto login(AuthUserDto dto){
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
        if(!user.isPresent()){
            return null;
        }
        if(passwordEncoder.matches(dto.getPassword(),user.get().getPassword())){
            return new TokenDto(jwtProvider.createToken(user.get()));
        }
        return null;
    }

    public TokenDto validate(String token, RequestDto requestDto){
        if(!jwtProvider.validate(token,requestDto)){
            return null;
        }
        String userName = jwtProvider.getUserNameFromToken(token);
        if(!authUserRepository.findByUserName(userName).isPresent()){
            return null;
        }
        return new TokenDto(token);
    }
}

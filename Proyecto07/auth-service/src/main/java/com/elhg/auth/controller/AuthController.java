package com.elhg.auth.controller;

import com.elhg.auth.dto.AuthUserDto;
import com.elhg.auth.dto.NewUserDto;
import com.elhg.auth.dto.RequestDto;
import com.elhg.auth.dto.TokenDto;
import com.elhg.auth.entity.AuthUser;
import com.elhg.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AuthUserDto authUserDto){
        if(authUserDto == null){
            return ResponseEntity.badRequest().build();
        }
        TokenDto tokenDto = authService.login(authUserDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDto> validate(@RequestParam String token, @RequestBody RequestDto requestDto){
        TokenDto tokenDto = authService.validate(token,requestDto);
        if(tokenDto == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthUser> create(@RequestBody NewUserDto dto){
        AuthUser authUser = authService.save((dto));
        if(authUser == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authUser);
    }
}

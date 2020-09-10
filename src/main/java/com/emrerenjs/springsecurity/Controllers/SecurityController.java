package com.emrerenjs.springsecurity.Controllers;

import com.emrerenjs.springsecurity.Business.JwtUtilService;
import com.emrerenjs.springsecurity.Business.UserDetailsManager;
import com.emrerenjs.springsecurity.Models.LoginModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    @Value("${spring.application.tokenExpireTimeSeconds}")
    private int tokenExpireTimeSeconds;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginModel loginModel, HttpServletRequest request, HttpServletResponse response){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginModel.getUsername(),loginModel.getPassword())
            );
            //Kullanıcı bilgilerini alıp token yaratmak üzere gönderiyoruz.
            final UserDetails userDetails = userDetailsManager.loadUserByUsername(loginModel.getUsername());
            final String jwt = jwtUtilService.generateToken(userDetails);
            //yaratılmış token'ımızı cookie haline getiriyoruz.
            Cookie cookie = new Cookie("jwt", URLEncoder.encode("Bearer " + jwt,"UTF-8"));
            cookie.setMaxAge(tokenExpireTimeSeconds - 1);
            cookie.setHttpOnly(true);
            cookie.setSecure(request.isSecure());
            cookie.setPath("/");
            response.addCookie(cookie);
            return new ResponseEntity<>("Succesfully Logined",HttpStatus.valueOf(200));
        }catch(BadCredentialsException e){
            return new ResponseEntity<>("Wrong username/password", HttpStatus.valueOf(403));
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.valueOf(403));
        }
    }
    @GetMapping("/login")
    public String login(){
        return "please login!";
    }
}

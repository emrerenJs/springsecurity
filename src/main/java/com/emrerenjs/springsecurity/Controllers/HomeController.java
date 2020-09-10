package com.emrerenjs.springsecurity.Controllers;

import com.emrerenjs.springsecurity.Business.IUserService;
import com.emrerenjs.springsecurity.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private IUserService userService;

    @GetMapping("/")
    public List<User> index(){ return userService.getUsers(); }

    @GetMapping("/get-by-username")
    public User getByUsername(@RequestParam(value = "") String username){
        User user = userService.getUserByUsername(username);
        return user;
    }

    @GetMapping("/user")
    public String user(){ return "Welcome to user"; }
}

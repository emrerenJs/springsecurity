package com.emrerenjs.springsecurity.Business;

import com.emrerenjs.springsecurity.Entities.User;

import java.util.List;

public interface IUserService {
    void addUser(User user);
    void deleteUser(User user);
    List<User> getUsers();
    User getUserByUsername(String username);
}

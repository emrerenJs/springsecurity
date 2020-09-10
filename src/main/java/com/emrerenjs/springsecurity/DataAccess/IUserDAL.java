package com.emrerenjs.springsecurity.DataAccess;

import com.emrerenjs.springsecurity.Entities.User;

import java.util.List;

public interface IUserDAL {
    void addUser(User user);
    void deleteUser(User user);
    User getUserByUsername(String username);
    List<User> getUsers();
}

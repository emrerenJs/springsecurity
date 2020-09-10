package com.emrerenjs.springsecurity.Business;

import com.emrerenjs.springsecurity.DataAccess.IUserDAL;
import com.emrerenjs.springsecurity.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*TR*/
//Bu sınıf, Repository sınıfını enjekte eden bir iş sınıfıdır.
//Datayı çekmeden önce ve çektikten sonra ki işlemleri yapmak için opsiyonlanmıştır.
/*EN*/
//This class is a business class that injects the Repository class.
//You can do something before and after take data.
@Service
public class UserManager implements IUserService {

    @Autowired
    private IUserDAL userDAL;

    @Override
    public void addUser(User user) {
        userDAL.addUser(user);
    }

    @Override
    public void deleteUser(User user) {
        userDAL.deleteUser(user);
    }

    @Override
    public List<User> getUsers() {
        return userDAL.getUsers();
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAL.getUserByUsername(username);
    }
}

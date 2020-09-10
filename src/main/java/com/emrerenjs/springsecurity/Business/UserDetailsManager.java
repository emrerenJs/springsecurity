package com.emrerenjs.springsecurity.Business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*TR*/
//Bu sınıf spring security için kullanıcı bilgilerini alan kısımdır. loadByUsername methodu Spring Security ve JWT gibi uygulamalar için tanımlanmıştır.
/*EN*/
//This class is the part that takes user details for spring security. loadByUsername method is overridden for Spring Security & such as JWT.
@Service
public class UserDetailsManager implements UserDetailsService {

    //TR: Kullanıcıyı almak için İş sınıfımızı Enjekte ediyoruz.
    //EN: We Inject our Business class to get User.
    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TR: İş sınıfımız, Repository sınıfımızdan bir User Entity sınıfı döndürür.
        //EN: Our business class returns User Entity class from Repository class.
        com.emrerenjs.springsecurity.Entities.User tempUser = userService.getUserByUsername(username);
        if(tempUser == null){
            throw new UsernameNotFoundException("Böyle bir kullanıcı bulunamadı..");
        }
        //TR: Kullanıcı bulunduysa Spring Security User sınıfına aktarılır.
        //EN: If user found, User is transferred to Spring Security User class.
        return new User(tempUser.getUsername(), tempUser.getPassword(), tempUser.getRoles());
    }
}

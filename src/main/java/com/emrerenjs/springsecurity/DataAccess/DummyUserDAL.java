package com.emrerenjs.springsecurity.DataAccess;

import com.emrerenjs.springsecurity.Entities.Role;
import com.emrerenjs.springsecurity.Entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//Bu sınıf veri üzerinde işlem yapmamızı sağlar. Siz herhangi bir ORM aracı veya farklı bir veri tabanı kullanabilirsiniz.
//Bunun için tek yapmanız gereken IUserDAL'ı implemente etmek ve o sınıfı @Repository olarak işaretlemenizdir.
//Bu sınıf sadece JSON verileri üzerinde okuma yapıyor. (Data/db.json)
@Repository
public class DummyUserDAL implements IUserDAL {

    @Override
    public void addUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public List<User> getUsers(){
        JSONParser jsonParser = new JSONParser();
        List<User> users = new ArrayList<>();
        try{
            Object obj = jsonParser.parse(new FileReader("src/main/java/com/emrerenjs/springsecurity/Data/db.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray usersList = (JSONArray) jsonObject.get("users");
            Iterator jsonObjectIterator = usersList.iterator();
            while(jsonObjectIterator.hasNext()){
                JSONObject jsonUser = (JSONObject)jsonObjectIterator.next();
                User user = new User();
                user.setUsername(jsonUser.get("username").toString());
                user.setPassword(jsonUser.get("password").toString());
                ArrayList<Role> roles = new ArrayList<>();

                JSONArray rolesList = (JSONArray) jsonUser.get("roles");
                Iterator jsonRolesIterator = rolesList.iterator();
                while(jsonRolesIterator.hasNext()){
                    Role role = new Role();
                    JSONObject jsonRole = (JSONObject)jsonRolesIterator.next();
                    role.setRole(jsonRole.get("role").toString());
                    roles.add(role);
                }

                user.setRoles(roles);
                users.add(user);
            }
        }catch(Exception ignored){}
        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        JSONParser jsonParser = new JSONParser();
        User user = null;
        try{
            Object obj = jsonParser.parse(new FileReader("src/main/java/com/emrerenjs/springsecurity/Data/db.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray usersList = (JSONArray) jsonObject.get("users");
            Iterator jsonObjectIterator = usersList.iterator();
            while(jsonObjectIterator.hasNext()){
                JSONObject jsonUser = (JSONObject)jsonObjectIterator.next();
                if(jsonUser.get("username").toString().equals(username)) {
                    user = new User();
                    user.setUsername(jsonUser.get("username").toString());
                    user.setPassword(jsonUser.get("password").toString());
                    ArrayList<Role> roles = new ArrayList<>();
                    JSONArray rolesList = (JSONArray) jsonUser.get("roles");
                    Iterator jsonRolesIterator = rolesList.iterator();
                    while(jsonRolesIterator.hasNext()){
                        Role role = new Role();
                        JSONObject jsonRole = (JSONObject)jsonRolesIterator.next();
                        role.setRole(jsonRole.get("role").toString());
                        roles.add(role);
                    }
                    user.setRoles(roles);
                    break;
                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return user;
    }
}

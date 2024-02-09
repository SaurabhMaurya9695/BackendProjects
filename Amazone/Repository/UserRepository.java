package Amazone.Repository;

import java.util.*;

import Amazone.Models.User;

public class UserRepository {

    List<User> lst = new ArrayList<>();

    public void addToDb(User user){
        lst.add(user);
        System.out.println("data added to db");
    }

    public User getUser(int userId){
        return new User(2 , "Raunak Verma" , "12445");
    }
}

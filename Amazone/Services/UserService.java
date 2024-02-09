package Amazone.Services;

import Amazone.Models.User;
import Amazone.Repository.UserRepository;

public class UserService {
    
    // add User
    public void addUser(User obj){
        // db 
        // System.out.println("User Added Successfully");
        
        UserRepository repository = new UserRepository();
        repository.addToDb(obj);

    }

    public String deleteUser(int userId){
        return "User Deleted SucccessFully with Id" + userId; 
    }

    public User getUser(int userId){
        UserRepository repository = new UserRepository();
        return repository.getUser(userId);
    }
}

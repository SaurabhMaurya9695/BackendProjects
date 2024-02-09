package Amazone.Controller;

import Amazone.Models.User;
import Amazone.Services.UserService;


public class UserController {
    public static void main(String[] args) {
        User user1 = new User( 1 , "Rauank verma" , "1234");
        User user2 = new User( 2 , "Saurabh" , "12345");

        UserService service = new UserService() ;
        service.addUser(user1);
        service.addUser(user2);

        String resp = service.deleteUser(2);
        System.out.println(resp);

        User user = service.getUser(2);
        System.out.println(user.getName());

    }
}

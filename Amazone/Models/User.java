package Amazone.Models;

public class User {
    private int userId;
    private String name ;
    private String mobileNo;

    User(){

    }

    public User(int userId , String name , String mobileNo){
        this.userId = userId;
        this.name = name ;
        this.mobileNo = mobileNo;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMobileNo() {
        return mobileNo;
    }
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    
}

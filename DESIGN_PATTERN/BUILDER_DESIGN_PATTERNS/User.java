package DESIGN_PATTERN.BUILDER_DESIGN_PATTERNS;

public class User {
    private  String username ;
    private  String email ;
    private  String gender ;

    private User(Builder builder){
        this.username = builder.username;
        this.email = builder.email;
        this.gender = builder.gender;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    static class Builder{
        private  String username ;
        private  String email ;
        private  String gender ;

        public Builder(){

        }
        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setEmail(String mail){
            this.email = mail;
            return this;
        }

        public Builder setGender(String gender){
            this.gender = gender;
            return this;
        }

        public User build(){
            User user = new User(this);
            return user;
        }

    }
}

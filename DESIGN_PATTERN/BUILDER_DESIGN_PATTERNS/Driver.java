package DESIGN_PATTERN.BUILDER_DESIGN_PATTERNS;

public class Driver {
    public static void main(String[] args) {
        User user = new User.Builder().
                setEmail("X@gmail.com").
                setUsername("Raj").
                setGender("Female").
                build();
        System.out.println(user.toString());
    }
}



class Student{
    static{
        String name = "Yash";
        System.out.println("Hello This side static " + name);
    }
    static{
        System.out.println("Hello This side2 static " );
    }
    
}


public class StaticBlock {
    
     // static block
    static{
        System.out.println("static block");
    }
    
    public static void main(String[] args) {
        Student stu = new Student();
        System.out.println("Yeh Worked");
    }
}

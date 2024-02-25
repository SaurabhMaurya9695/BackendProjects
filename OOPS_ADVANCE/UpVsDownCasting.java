package OOPS_ADVANCE;

class Student{
    String name ;

}

class Rauank extends Student{

}

public class UpVsDownCasting {

    public static void main(String[] args) {
        Student student1 = new Student();
        Student s = new Rauank();
        
        Rauank r = (Rauank) new Student(); // downcasting 


        System.out.println(student1 instanceof Student);
    }
}

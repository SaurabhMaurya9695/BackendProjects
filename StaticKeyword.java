

class Student{
    String name ;
    static String college = "SRMCEM";
    
    static void display(){
        // System.out.println("Working" + " " + this.college);  this cant be used here
        college = "BBD"; // static block can change the value of static variable
        System.out.println("Working" + " " + college);  
    }
}

public class StaticKeyword {
    public static void main(String[] args) {
//         Student obj1 = new Student();
//         obj1.name = "Raunak";
//         obj1.display();
        
//          Student obj2 = new Student();
//         obj2.name = "Saurabh";
//         obj2.display();
        
        Student.display();
    }
}


// StaticKeyword.main(); -> 
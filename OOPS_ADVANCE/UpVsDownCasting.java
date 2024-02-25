// package OOPS_ADVANCE;

// class Student{
//     String name ;

// }

// class Rauank extends Student{

// }

// public class UpVsDownCasting {

//     public static void main(String[] args) {
//         Student student1 = new Student();
//         Student s = new Rauank();
        
//         Rauank r = (Rauank) new Student(); // downcasting 


//         System.out.println(student1 instanceof Student);
//     }
// }



interface Printable {
}

class A implements Printable {
    public void a() {
        System.out.println("a method");
    }
}

class B implements Printable {
    public void b() {
        System.out.println("b method");
    }
}

class Call {
    void invoke(Printable p) {// upcasting
        if (p instanceof A) {
            A a = (A) p;// Downcasting
            a.a();
            System.out.println("Ok Downcasting done for A");
        }
        if (p instanceof B) {
            B b = (B) p;// Downcasting
            b.b();
            System.out.println("Ok Downcasting done for B");
        }

    }
}// end of Call class
class Test4 {
    public static void main(String args[]) {
        Printable p = new B();
        Call c = new Call();
        c.invoke(p);
    }
}
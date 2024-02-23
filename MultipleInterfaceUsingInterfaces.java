

/* 
class A{
    void run(){
        System.err.println("Running A");
    }
}

class B{
    void run(){
        System.err.println("Running B");
    }
}

class C extends A , B{ // this is not a syntatically error but this error occured due 
    // to Multiple Inheritance
}
*/


// solve Multiple Interhance using Interfaces 

interface A{
    void run();

    // treated as non - abstract method 
    default void run1(){
        System.out.println("this is Default Method");
    }
}


interface B{
    void run();
}

class C implements A , B{

    @Override
    public void run() {
       System.err.println("C is Running");
    }
    
}


public class MultipleInterfaceUsingInterfaces {
    public static void main(String[] args) {
        C c = new C();
        c.run();
        c.run1();
    }
}

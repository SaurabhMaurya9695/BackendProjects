package OOPS_ADVANCE;


class Parent{
    int Pdata;
    
    public void getObject(){
        System.out.println("Parent Object");
    }
}

class Child extends Parent{
    int Cdata;
    
    public int getObject(){
        System.out.println("Parent Object");
    }
}

public class CovarientType {
    public static void main(String[] args) {
        Parent p = new Parent();
        Child p2 = new Child();
        // Parent p3 = new Child();

        p.getObject();
        p2.getObject();
        // p3.getObject();
    }
}

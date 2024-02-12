
class Parent{
    String sirName ;
    String color;

    void sing(){
        System.out.println("I'm singing");
    }
}

class Children extends Parent{
    String name ;
    String address ;

}



public class Inheritance {
    public static void main(String[] args) {
        Children children = new Children() ;
        children.name ="Raunak";
        children.address ="Matiyari";

        System.out.println(children.name + " " + children.address);

        children.sirName = "verma";
        children.color = "White";
        System.out.println(children.name + " " + children.address + " " + 
        children.sirName + " " + children.color);
    }
}

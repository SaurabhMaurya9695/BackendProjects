
class Dog{
    void bark(){
        System.out.println("Barking");
    }
}

class Bird extends Dog{
    void fly(){
        System.out.println("flying");
    }
}

class Penguin extends Dog{
    void swim(){
        System.out.println("Swim");
    }
}



public class HierarchicalInterface {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.bark() ; // b
        
        Bird bird = new Bird();
        bird.fly(); // f
        bird.bark(); // b
        
        Penguin penguin = new Penguin();
        penguin.swim();
        penguin.bark();
    }
}

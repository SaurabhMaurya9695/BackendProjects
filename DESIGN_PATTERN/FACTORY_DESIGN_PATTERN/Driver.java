package DESIGN_PATTERN.FACTORY_DESIGN_PATTERN;


import java.util.Scanner;

interface IShake{
    void createShake();
}
class Oreo implements  IShake{
    @Override
    public void createShake() {
        System.out.println("we are creating Oreo Shake");
    }
}
class Vanilla implements  IShake{
    @Override
    public void createShake() {
        System.out.println("we are creating Vanilla Shake");
    }
}

class BadamShake implements  IShake{

    @Override
    public void createShake() {
        System.out.println("we are creating BadamShake For you");
    }
}

public class Driver {
    public static void main(String[] args) {

          Scanner scn = new Scanner(System.in);
          String type = scn.nextLine();

          IShake shake = ShakeFactory.getFactory(type);
          shake.createShake();

//        Vanilla vanilla = new Vanilla();
//        vanilla.createShake();
//
//        Oreo oreo = new Oreo();
//        oreo.createShake();

    }
}

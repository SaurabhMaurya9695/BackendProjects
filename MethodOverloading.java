

class Addition{
    public  void add(int x , int y){
        System.out.println(x + y);
    }


    void add(int x , int y , int z , int a){
        System.out.println(x + y + z + a);
    }

    void check(int x , int y){
        System.out.println(x - y);
    }
}

public class MethodOverloading {
    public static void main(String[] args) {
        Addition addition = new Addition();
        addition.add(2, 4); //showing ambiguity
        // addition.add(2, 4, 6);
        // addition.add(2, 4, 6 , 8);
        // addition.check(4 , 2);

        // Addition.add(2, 5);


    }   
}
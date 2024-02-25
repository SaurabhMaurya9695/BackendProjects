
class Cuboid {
    private int length;
    private int breath;
    private int height;

    // explicit 
    Cuboid(){
        this(1 , 1, 1);
    }
    Cuboid(int side){
        this(side , side , side);
    }
    Cuboid(int length , int breath){
        this(length , breath , 1);
    }
    Cuboid(int length , int breath , int height){
        this.length = length ;
        this.breath = breath;
        this.height = height;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public int getBreath() {
        return breath;
    }
    public void setBreath(int breath) {
        this.breath = breath;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

}


public class ConstructorChaining {
    public static void main(String[] args) {
        Cuboid cuboid = new Cuboid() ;
        Cuboid cuboid1 = new Cuboid(1) ;
        Cuboid cuboid2 = new Cuboid(1,2) ;
        Cuboid cuboid3 = new Cuboid(1 ,2 , 3) ;

        System.out.println(cuboid.getLength() + " " + cuboid.getBreath() + " " + cuboid.getHeight()) ;
        System.out.println(cuboid1.getLength() + " " + cuboid1.getBreath() + " " + cuboid1.getHeight()) ;
        System.out.println(cuboid2.getLength() + " " + cuboid2.getBreath() + " " + cuboid2.getHeight()) ;
        System.out.println(cuboid3.getLength() + " " + cuboid3.getBreath() + " " + cuboid3.getHeight()) ;

    }
}

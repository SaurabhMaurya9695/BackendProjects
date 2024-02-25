public class MainFunctionOverLoad {

    public static void main(int x , int y){
        System.out.println("I 'm from main with two parameter");
    }
    public static void main(String[] args) {
        System.out.println("I'm main function");
        main();
        main(1,2);
    }

    public static void main(){
        System.out.println("I 'm from main");
    }

    
}



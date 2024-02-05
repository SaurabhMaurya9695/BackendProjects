public class Student {
    private String name ;    // data varible ;
    
    //encapsultae 
    //getter 
    public String getName(){
        return name ;
    }

    //setter 
    public void setName(String x ){
        name = x ;
    }

    // methods -> without static keyword functions are called methods
    public  void printKro(){
        System.out.println(name);
    }  

}

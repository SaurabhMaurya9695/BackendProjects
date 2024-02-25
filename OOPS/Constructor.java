
class Student{
    private String  name ;
    private int rollNo ;

    Student(){  // explicit constructor 
        setName("Im created");
    }
    
    // paramaterized 
    
    Student(String y){
        name = y ;
    }
    
    Student(String y , int no){
        name = y ;
        rollNo = no ;
    }
    
    Student(int no , String y ){
        name = y ;
        rollNo = no ;
    }
    
    
    Student(int y ){
        rollNo = y ;
    }
    
    public String  getName(){
        return name;
    }
    
    public void setName(String x){
        name = x ;
    }
    
    public int  getRollNo(){
        return rollNo;
    }
}
public class Constructor {
    public static void main(String [] args){
//         Student stu = new Student() ;
//         stu.setName("Saurabh");
        
        // Student stu2 = new Student("Saurabh");
        // System.out.println(stu2.getName());
        
        Student stu2 = new Student(10 , "Saurabh" );
        System.out.println(stu2.getName() + " " + stu2.getRollNo());
        
        Student stu = new Student("Saurabh" , 5);
        System.out.println(stu.getName() + " " + stu.getRollNo());
        
        // Student stu = new Student(10);
        // System.out.println(stu.getRollNo());
        
    }
}
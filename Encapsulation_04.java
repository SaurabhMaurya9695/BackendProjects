
// blueprint 
class Student{
    private String name ;
    private int rollNo ;
    public String getName(){
        return name;
    }
    
    public void setName(String takenName){
        name = takenName;
    }
    
    public int getRollNo(){
        return rollNo;
    }
    
    public void setRollNo(int newRollNo){
        rollNo = newRollNo;
    }
    
}

public class Encapsulation_04 {
    public static void main(String[] args) {
        // int a = 10 ;
        // String s = "rauank";
        // System.out.println(a + s);


        // int [] arr ={10 ,20 ,30} ;
        // String [] s = {"Rauank" , "verma" , "Saurabh"};
        // System.out.println(arr[2] + s[1]);

        // Student random = new Student() ;
        // random.name = "Rauank";
        // random.rollNo = 10;
        // System.out.println(random.name + random.rollNo);
        
        
        // getter and setter
        Student raunak = new Student() ;
        raunak.setName("Hi This side Rauank ");
        raunak.setRollNo(10);
        raunak.setName("Updated Name is :Rauank Verma");
        System.out.println(raunak.getName() + " " + raunak.getRollNo());
        
        Student saurabh = new Student() ;
        saurabh.setRollNo(10);
        saurabh.setName("Hi This side Rauank ");
        System.out.println(saurabh.getName() + " " + saurabh.getRollNo());
        System.out.println(saurabh == raunak);

        
    }
}

package OOPS_ADVANCE;


class Student{
    int p , c,  m ;
    Student(){
        
    }
    Student(int p , int c , int m){
        this.p = p ;
        this.c = c;
        this.m = m ;
    }
}

class Solution
{
    public void customSort (int phy[], int chem[], int math[], int N)
    {
        Student [] stu = new Student[N];
        for(int i = 0 ; i < N ; i ++){
            stu[i] = new Student(phy[i] , chem[i] , math[i]);
        }
        
        // every index has a new Student 
        Arrays.sort(stu , (stu1 , stu2)->{
            // stu1 -> PCM
            // stu2 -> PCM
            
            if(stu1.p != stu2.p ){
                return stu1.p - stu2.p ; // sorted in asesnding order
            }
            if(stu1.c != stu2.c){
                return stu2.c - stu1.c;
            }
            return stu1.m - stu1.m ;
            
        });
        
        for(int i = 0 ; i < N ; i ++){
            phy[i] = stu[i].p ;
            chem[i] = stu[i].c ;  
            math[i] = stu[i].m ; 
        }
        
        
        
    }
}


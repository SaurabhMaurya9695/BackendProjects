package DESIGN_PRINCIPLES.ISP.problems;

public class Robot implements  IWorker {

    @Override
    public void work() {
        
    }

    @Override
    public void eat() {
        // this is invalid 
        // this is not following ISP        
    }
    
}
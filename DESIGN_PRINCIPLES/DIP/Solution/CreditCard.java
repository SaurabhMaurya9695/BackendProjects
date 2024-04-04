package DESIGN_PRINCIPLES.DIP.Solution;

public class CreditCard implements IBankService{

    @Override
    public void doTransaction(long amount){
        System.out.println("Transaction has been via CreditCard : " + amount);
    }
    
    
}

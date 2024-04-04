package DESIGN_PRINCIPLES.DIP.Solution;

public class ShoppingMall {
    
    private IBankService bankService;

    ShoppingMall(){

    }

    public ShoppingMall(IBankService card){
        this.bankService = card;
    }


    public void doTransactionInShoppinMall(long amount){
        this.bankService.doTransaction(amount);
    }



    public static void main(String[] args) {
        
        IBankService card = new DebitCard();
        ShoppingMall mall = new ShoppingMall(card);
        mall.doTransactionInShoppinMall(5000);
    }

}

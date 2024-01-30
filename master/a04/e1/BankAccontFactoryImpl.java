package a04.e1;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class BankAccontFactoryImpl implements BankAccountFactory {

    @Override
    public BankAccount createBasic() {
       return createWithFee((x) -> 0);
    }

    @Override
    public BankAccount createWithFee(UnaryOperator<Integer> feeFunction) {
        return createWithFeeAndCredit(feeFunction, (x) -> x < 1, (x) -> 0);
    }

    @Override
    public BankAccount createWithCredit(Predicate<Integer> allowedCredit, UnaryOperator<Integer> rateFunction) {
        return createWithFeeAndCredit((x) -> 0, allowedCredit, rateFunction);
    }

    @Override
    public BankAccount createWithBlock(BiPredicate<Integer, Integer> blockingPolicy) {
        return new BankAccount() {
            BankAccount account = createBasic();
            boolean isBlock = false;
            @Override
            public int getBalance() {
               return account.getBalance();
            }

            @Override
            public void deposit(int amount) {
                if(!isBlock){
                  account.deposit(amount);  
                }  
            }

            @Override
            public boolean withdraw(int amount) {
               if(!isBlock){
                isBlock = !account.withdraw(amount);
                return !isBlock;  
               }
               return false;         
            }
            
        };
    }

    @Override
    public BankAccount createWithFeeAndCredit(UnaryOperator<Integer> feeFunction, Predicate<Integer> allowedCredit,
            UnaryOperator<Integer> rateFunction) {
        return new BankAccount() {

            private int balance = 0;
            @Override
            public int getBalance() {
                return balance;
            }

            @Override
            public void deposit(int amount) {
                balance += amount;
            }

            @Override
            public boolean withdraw(int amount) {
                var withamount = amount + feeFunction.apply(amount);
                if(balance - withamount < 0){
                    if(canWithdraw(withamount, allowedCredit)){
                        withamount += rateFunction.apply(0);
                        balance -= withamount;
                        return true;
                    }
                    return false;
                }
                balance -= withamount;
                return true;
            }

            private boolean canWithdraw(int amount,Predicate<Integer> allowedCredit){
                return allowedCredit.test(-balance + amount) ;
            }
            
        };
    }
    
}

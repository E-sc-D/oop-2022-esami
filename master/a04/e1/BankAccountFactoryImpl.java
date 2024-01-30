package a04.e1;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class BankAccountFactoryImpl implements BankAccountFactory {

    @Override
    public BankAccount createBasic() {
       return new CreditDecorator(x -> x < 1,(x) -> 0, new BasicBankAccount());
    }

    @Override
    public BankAccount createWithFee(UnaryOperator<Integer> feeFunction) {
        return new FeeDecorator(feeFunction, new CreditDecorator(x -> x < 1,(x) -> 0, new BasicBankAccount()));
    }

    @Override
    public BankAccount createWithCredit(Predicate<Integer> allowedCredit, UnaryOperator<Integer> rateFunction) {
        return new CreditDecorator(allowedCredit, rateFunction, new BasicBankAccount());
    }

    @Override
    public BankAccount createWithBlock(BiPredicate<Integer, Integer> blockingPolicy) {
       return new BlockDecorator(new CreditDecorator(x -> x < 1,(x) -> 0, new BasicBankAccount()));
    }

    @Override
    public BankAccount createWithFeeAndCredit(UnaryOperator<Integer> feeFunction, Predicate<Integer> allowedCredit,
            UnaryOperator<Integer> rateFunction) {
        return new FeeDecorator(feeFunction, new CreditDecorator(allowedCredit, rateFunction, new BasicBankAccount()));
    }

    private abstract class BankAccountDecorator implements BankAccount {

        BankAccount account;
        
        public BankAccountDecorator(BankAccount account){
            this.account = account;
        }

        @Override
        public abstract void deposit(int amount);

        @Override
        public int getBalance() {
            return account.getBalance();
        }

        @Override
        public abstract boolean withdraw(int amount);

    }

    private class BasicBankAccount implements BankAccount{
        private int balance;
        public BasicBankAccount(){
            this.balance = 0;
        }
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
           balance -= amount;
           return true;
        }
    }

    private class FeeDecorator extends BankAccountDecorator{
        UnaryOperator<Integer> feeFunction;
        public FeeDecorator(UnaryOperator<Integer> feeFunction,BankAccount base){
            super(base);
            this.feeFunction = feeFunction;

        }

        @Override
        public void deposit(int amount) {
            this.account.deposit(amount);
        }

        @Override
        public boolean withdraw(int amount) {
            return this.account.withdraw(feeFunction.apply(amount) + amount);
        }

    }

    private class CreditDecorator extends BankAccountDecorator{
        Predicate<Integer> allowedCredit;
        UnaryOperator<Integer> rateFunction;
        public CreditDecorator(Predicate<Integer> allowedCredit, UnaryOperator<Integer> rateFunction, BankAccount base){
            super(base);
            this.allowedCredit = allowedCredit;
            this.rateFunction = rateFunction;
        }
        @Override
        public void deposit(int amount) {
            account.deposit(amount);
        }

        @Override
        public boolean withdraw(int amount) {
            int withdraw = account.getBalance() - amount;
           if(withdraw < 0){
                if(allowedCredit.test(-withdraw)){
                    account.withdraw(amount + rateFunction.apply(0));
                    return true;
                }
                return false;
           }
           account.withdraw(amount);
           return true;
        }

    }

    private class BlockDecorator extends BankAccountDecorator{
        boolean isBlocked = false;

        public BlockDecorator(BankAccount account){
            super(account);
        }

        @Override
        public void deposit(int amount) {
            if(!isBlocked){
                account.deposit(amount);
            }
        }

        @Override
        public boolean withdraw(int amount) {
            if(!isBlocked){
                isBlocked = !account.withdraw(amount);
            }

            return !isBlocked;
        }

    }
    
}

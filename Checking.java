/**
 * The Checking class represents a checking account. It extends the Account class 
 * to inherit common account functionality and adds specific behavior for a checking account.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class Checking extends Account {
    /**The number assigned to the most recently created checking account. Used to generate unique checking account numbers by incrementing this value.*/
    public static int lastCheckingAccountNumber = -1;

    /** Default constructor that initializes a Checking account with default values. */
    public Checking() {
        super();
    }

    /**
     * Constructor with parameters to initialize a Checking account with the given 
     * account number and starting balance.
     * @param accountNumber   the account number for the checking account
     * @param startingBalance the initial balance for the checking account
     */    
    public Checking(int accountNumber, double startingBalance) {
        super(accountNumber, startingBalance);
        if (accountNumber > lastCheckingAccountNumber) {
            lastCheckingAccountNumber = accountNumber;
        }
    }

    /** Displays the checking account information, including the account number and current balance. */
    @Override
    public void displayAccountInfo() {
        System.out.println("Checking Account Number: " + this.accountNumber + ", Balance: $" + this.balance);
    }
}

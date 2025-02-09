/**
 * The Saving class represents a savings account. It extends the Account class 
 * to inherit common account functionality.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */

public class Saving extends Account {
    /** The number assigned to the most recently created savings account. Used to generate unique savings account numbers by incrementing this value.*/
    public static int lastSavingsAccountNumber = -1;

    /** Default constructor that initializes a Saving account with default values. */
    public Saving() {
        super();
    }

    /**
     * Constructor with parameters to initialize a Saving account with the given 
     * account number and starting balance.
     * @param accountNumber   the account number for the savings account
     * @param startingBalance the initial balance for the savings account
     */
    public Saving(int accountNumber, double startingBalance) {
        super(accountNumber, startingBalance);
        if (accountNumber > lastSavingsAccountNumber) {
            lastSavingsAccountNumber = accountNumber;
        }
    }
    
    /** Displays the savings account information, including the account number and current balance. */
    @Override
    public void displayAccountInfo() {
        System.out.println("Savings Account Number: " + accountNumber + ", Balance: $" + balance);
    }
}

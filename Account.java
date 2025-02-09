/**
 * The Account class represents a general bank account with an account number 
 * and a balance. It provides basic functionality for managing the account details.
 * This class serves as a base class for more specific types of accounts like 
 * Checking, Saving, and Credit accounts.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public abstract class Account {
    /** The unique number associated with the account.*/
    protected int accountNumber;

    /**The current balance of the account.*/
    protected double balance;

    /** Default constructor that initializes an empty Account object.*/
    public Account() {
    }

    /**
     * Constructor with parameters to initialize an Account with a specified account 
     * number and balance.
     * @param accountNumber the unique number for the account
     * @param balance       the initial balance of the account
     */
    public Account(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    /**
     * Gets the account number of the account.
     * @return the account number
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number for the account.
     * @param accountNumber the new account number
     */
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Gets the current balance of the account.
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the balance for the account.
     * @param balance the new balance of the account
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Displays detailed information about the customer's accounts, 
     */
    public void displayAccountInfo() {
    }
}

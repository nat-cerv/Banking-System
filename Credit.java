import java.util.Random;

/**
 * The Credit class represents a credit account. It extends the Account class 
 * and includes additional functionality for handling credit limits and payments.
 * This class allows for charges and payments within the credit limit.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class Credit extends Account {
    /**The number assigned to the most recently created credit account. Used to generate unique credit account numbers by incrementing this value.*/
    public static int lastCreditAccountNumber = -1;

    /** The maximum credit limit available for the customer's credit account.*/
    private double creditMax;

    /**
     * Default constructor that initializes a Credit account with a default balance of -10.0 
     * and a default credit limit of 1000.0.
     */
    public Credit() {
        super(-1, -10.0); 
        this.creditMax = 1000.0;
    }

    /**
     * Constructor with parameters to initialize a Credit account with a given account number, 
     * starting balance, and maximum credit limit.
     * @param accountNumber   the account number for the credit account
     * @param startingBalance the initial balance for the credit account
     * @param creditMax       the maximum credit limit for the account
     */
    public Credit(int accountNumber, double startingBalance, double creditMax) {
        super(accountNumber, startingBalance);
        this.creditMax = creditMax;
        if (accountNumber > lastCreditAccountNumber) {
            lastCreditAccountNumber = accountNumber;
        }
    }

    /**
     * Gets the maximum credit limit of the account.
     * @return the maximum credit limit
     */
    public double getCreditMax() {
        return creditMax;
    }

    /**
     * Sets the maximum credit limit for the account.
     * @param creditMax the new maximum credit limit
     */
    public void setCreditMax(double creditMax) {
        this.creditMax = creditMax;
    }

    /**
     * To check if a deposit is valid based on the credit limit
     * @param amount the amount to charge to the credit account
     * @return  true if the amount is valid
     */
    public boolean isDepositWithinLimit(double amount) {
        return amount <= (this.creditMax + this.balance);
    }

    /**
     * Determines the credit limit for a customer based on their credit score.
     *
     * The method categorizes the credit score into ranges, assigning a random credit
     * limit within a specified range for each score bracket.
     * @param creditScore The credit score of the customer.
     * @return A credit limit amount based on the customer's credit score.
     */
    public static double determineCreditLimit(int creditScore) {
        Random rand = new Random();
        if (creditScore <= 580) {
            return 100 + rand.nextInt(600);
        } else if (creditScore <= 669) {
            return 700 + rand.nextInt(4300);
        } else if (creditScore <= 739) {
            return 5000 + rand.nextInt(2500);
        } else if (creditScore <= 799) {
            return 7500 + rand.nextInt(8500); 
        } else {
            return 16000 + rand.nextInt(9000);
        }
    }

    /** Displays the credit account information, including the account number and current balance. */
    @Override
    public void displayAccountInfo() {
        System.out.println("Credit Account Number: " + accountNumber + ", Balance: $" + balance + ", Credit Max: $" + creditMax);
    }
}
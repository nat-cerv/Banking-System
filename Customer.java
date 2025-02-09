import java.util.HashMap;

/**
 * The Customer class represents a customer who has a checking account, savings account, 
 * and credit account. It extends the Person class to inherit personal information.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class Customer extends Person {
    /** The ID assigned to the most recently added customer. Used to generate unique customer IDs by incrementing this value.*/
    public static int lastCustomerId = -1;
    
    /** The checking account associated with the customer. */
    private Checking checkingAccount;

    /** The savings account associated with the customer. */
    private Saving savingsAccount;

    /** The credit account associated with the customer. */
    private Credit creditAccount;

    /** A map of transaction details, where each entry associates a transaction ID or description
    * with the corresponding transaction information.
    */
    private HashMap<String, String> transactions;


    /**
     * Default constructor that initializes a Customer object with default Checking, 
     * Saving, and Credit accounts.
     */
    public Customer() {
        super();
        this.checkingAccount = new Checking();
        this.savingsAccount = new Saving();
        this.creditAccount = new Credit();
        this.transactions = new HashMap<>();
    }

    /**
     * Constructor with parameters to initialize a Customer object with the given 
     * personal information and financial accounts.
     * @param identificationNumber the customer's identification number
     * @param firstName            the customer's first name
     * @param lastName             the customer's last name
     * @param dateOfBirth          the customer's date of birth
     * @param address              the customer's address
     * @param phoneNumber          the customer's phone number
     * @param checkingAccount      the customer's checking account
     * @param savingsAccount       the customer's savings account
     * @param creditAccount        the customer's credit account
     */
    public Customer(int identificationNumber, String firstName, String lastName, String dateOfBirth, String address, String phoneNumber, Checking checkingAccount, Saving savingsAccount, Credit creditAccount) {
        super(identificationNumber, firstName, lastName, dateOfBirth, address, phoneNumber);
        this.checkingAccount = checkingAccount;
        this.savingsAccount = savingsAccount;
        this.creditAccount = creditAccount;
        this.transactions = new HashMap<>();
        if (identificationNumber > lastCustomerId) {
            lastCustomerId = identificationNumber;
        }
    }

    /**
     * Retrieves the customer's checking account.
     * @return the checking account
     */
    public Checking getCheckingAccount() {
        return checkingAccount;
    }

    /**
     * Sets the customer's checking account.
     * @param checkingAccount the new checking account
     */
    public void setCheckingAccount(Checking checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    /**
     * Retrieves the customer's savings account.
     * @return the savings account
     */
    public Saving getSavingsAccount() {
        return savingsAccount;
    }

    /**
     * Sets the customer's savings account.
     * @param savingsAccount the new savings account
     */
    public void setSavingsAccount(Saving savingsAccount) {
        this.savingsAccount = savingsAccount;
    }

    /**
     * Retrieves the customer's credit account.
     * @return the credit account
     */
    public Credit getCreditAccount() {
        return creditAccount;
    }

    /**
     * Sets the customer's credit account.
     * @param creditAccount the new credit account
     */
    public void setCreditAccount(Credit creditAccount) {
        this.creditAccount = creditAccount;
    }

    /**
     * Makes a deposit to an account.
     * @param amount the amount to deposit
     * @param accountType the account
     */
    public void withdraw(double amount, String accountType) {
        double balance;
        switch (accountType) {
            case "1":
                balance = getCheckingAccount().getBalance();
                if (balance >= amount) {
                    getCheckingAccount().setBalance(balance - amount);
                    System.out.println("Withdrew $" + amount + " from Checking.");
                } else {
                    System.out.println("Insufficient funds in Checking.");
                }
                break;

            case "2":
                balance = getSavingsAccount().getBalance();
                if (balance >= amount) {
                    getSavingsAccount().setBalance(balance - amount);
                    System.out.println("Withdrew $" + amount + " from Savings.");
                } else {
                    System.out.println("Insufficient funds in Savings.");
                }
                break;

            default:
                System.out.println("Invalid account selection.");
                break;
        }
    }
    

    /**
     * Makes a deposit to an account.
     * @param amount the amount to deposit
     * @param accountType the account
     */
    public void deposit(double amount, String accountType) {
        switch (accountType) {
            case "1":
                getCheckingAccount().setBalance(getCheckingAccount().getBalance() + amount);
                System.out.println("Deposited $" + amount + " into Checking.");
                break;

            case "2":
                getSavingsAccount().setBalance(getSavingsAccount().getBalance() + amount);
                System.out.println("Deposited $" + amount + " into Savings.");
                break;

            case "3":
                if (amount + getCreditAccount().getBalance() <= 0) {
                    getCreditAccount().setBalance(getCreditAccount().getBalance() + amount);
                    System.out.println("Deposited $" + amount + " into Credit Account.");
                } else {
                    System.out.println("Amount exceeded the balance.");
                }
                break;

            default:
                System.out.println("Invalid account selection.");
                break;
        }
    }


    /**
     * Adds a transaction record for a specified account type, including details such as
     * the starting balance, ending balance, and a transaction description.
     * 
     * If there are existing transactions for the specified account type, the new transaction
     * is appended to the current list.
     * @param accountType            The type of account associated with the transaction (e.g., "Checking", "Savings", "Credit").
     * @param startingBalance        The balance of the account before the transaction.
     * @param endingBalance          The balance of the account after the transaction.
     * @param transactionDescription A description of the transaction.
     */
    public void addTransaction(String accountType, double startingBalance, double endingBalance, String transactionDescription) {
        String text = "Starting Balance: $" + startingBalance + ", " + "Ending Balance: $" + endingBalance + ", " + "Transaction: " + transactionDescription + ".\n";
        String updatedTransactions;
        if (transactions.get(accountType) == null) {
            updatedTransactions = text;
        }
        else {
            updatedTransactions = transactions.get(accountType) + text;
        } 
        transactions.put(accountType, updatedTransactions);
    }

    /**
     * Retrieves the transaction history for a specified account type.
     * @param accountType The type of account whose transaction history is to be retrieved (e.g., "Checking", "Savings", "Credit").
     * @return A string containing the transaction history for the specified account type, or an empty string if no transactions are recorded.
     */
    public String getTransactions(String accountType) {
        return transactions.getOrDefault(accountType, "");
    }

}

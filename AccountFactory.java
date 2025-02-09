/**
 * A factory class for creating different types of accounts.
 * Supports Checking, Saving, and Credit accounts.
 * Throws an exception if an invalid account type is provided.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class AccountFactory {
    /** Default constructor. */
    public AccountFactory() {}

    /**
     * Creates an account based on the specified type.
     * @param accountType  The type of account ("Checking", "Saving", or "Credit").
     * @param accountNumber The account number for the new account.
     * @param creditMax     The credit limit for Credit accounts (ignored for other types).
     * @return A new account of the specified type.
     * @throws InvalidAccountException if the account type is invalid
     */
    public Account createAccount(String accountType, int accountNumber, double creditMax) throws InvalidAccountException {
        switch (accountType) {
            case "Checking":
                return new Checking(accountNumber, 0.0);
            case "Saving":
                return new Saving(accountNumber, 0.0);
            case "Credit":
                return new Credit(accountNumber, -10.0, creditMax);
            default:
                throw new InvalidAccountException("Invalid account type: " + accountType);
        }
    }
}

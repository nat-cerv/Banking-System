/**
 * The Staff interface defines common responsibilities and behaviors for bank staff roles,
 * including BankManager and BankTeller.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public interface Staff {
    /**
     * Displays detailed information about a specified customer.
     * 
     * This method should be implemented to show customer details, such as identification,
     * personal information, and account summaries.
     * @param customer The customer whose information is to be displayed.
     */
    public abstract void viewCustomerInfo(Customer customer);  
}
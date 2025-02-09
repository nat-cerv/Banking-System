/**
 * The BankTeller class represents a bank teller.
 * It extends the Person class to inherit personal information.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class BankTeller extends Person implements Staff {
    /**Creates a new BankTeller.*/
    public BankTeller() {
        super();
    }

    /**
     * Displays the personal information of a given customer, including their
     * identification number, name, date of birth, address, phone number, and password.
     * @param customer The customer whose information is to be displayed.
     */
    @Override
    public void viewCustomerInfo(Customer customer) {
        System.out.println("\n\nCustomer Personal Information:");
        String fullName = customer.getFirstName() + " " + customer.getLastName();
        System.out.println("---------------------");
        System.out.println("Identification Number: " + customer.getIdentificationNumber());
        System.out.println("Name: " + fullName);
        System.out.println("Date of Birth: " + customer.getDateOfBirth()); 
        System.out.println("Address: " + customer.getAddress());
        System.out.println("Phone Number: " + customer.getPhoneNumber());

        PasswordManager passwordManager = PasswordManager.getInstance();
        String password = passwordManager.generatePassword(fullName);
        System.out.println("Password: " + password);
    }

    /**
     * Processes a deposit transaction of the customer.
     * @param customer The customer making the deposit.
     */
    public void processDeposit(Customer customer) {
        System.out.println("\nDeposit has been processed for " + customer.getFirstName() + " " + customer.getLastName() + "...");
    }

    /**
     * Processes a withdrawal transaction of the customer.
     * @param customer The customer making the withdrawal.
     */
    public void processWithdrawal(Customer customer) {
        System.out.println("\nWithdrawal has been processed for " + customer.getFirstName() + " " + customer.getLastName() + "...");
    }
}


import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Manages customer passwords in a banking system.
 * This class provides functionalities for password management, including generating,
 * updating, and verifying customer passwords. It uses a singleton design pattern to
 * ensure a single instance of the password manager is used throughout the system.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class PasswordManager {
    /** Singleton instance.*/
    private static PasswordManager instance; 
    /** Map of customer names to passwords.*/
    private HashMap<String, String> passwords; 

    /** Private constructor to prevent instantiation.*/
    private PasswordManager() {
        passwords = new HashMap<>();
    }
    
    /**
     * Retrieves the singleton instance of the PasswordManager.
     * @return The singleton instance of PasswordManager.
     */
    public static PasswordManager getInstance() {
        if (instance == null) {
            instance = new PasswordManager();
        }
        return instance;
    }

    /**
     * Retrieves the password for the specified customer.
     * @param customerName The name of the customer whose password is being retrieved.
     * @return The customer's password, or null if no password exists for the given name.
     */
    public String getPassword(String customerName) {
        return passwords.get(customerName);
    }

     /**
     * Updates the password for a specific customer after verifying its validity.
     * The new password must be 8 characters long and different from the old password.
     * @param customerName The name of the customer whose password is being updated.
     * @param scanner      A Scanner object to read user input for the new password.
     */
    public void updatePassword(String customerName, Scanner scanner) {
        String newPassword;
        String oldPassword = passwords.get(customerName);
        while (true) {
            System.out.print("\nEnter a new password (length of 8): ");
            newPassword = scanner.nextLine();
            
            if (newPassword.length() != 8) {
                System.out.println("\nInvalid input. Password must be exactly 8 characters long.");
                continue;
            }
            if (newPassword.equals(oldPassword)) {
                System.out.println("\nInvalid input. New password cannot be the same as the old password.");
                continue;
            }
            break;
        }
        passwords.put(customerName, newPassword);
        System.out.println("\nPassword updated successfully for " + customerName + ".");
    }

    /**
     * Handles the login process for a customer by validating their password.
     * Allows three attempts to enter the correct password, with an option to reset
     * the password after failing all attempts.
     * @param customerName The name of the customer attempting to log in.
     * @param scanner      A Scanner object to read user input for the password.
     * @return True if the login is successful, false otherwise.
     */
    public boolean loginCustomer(String customerName, Scanner scanner) {
        String storedPassword = getPassword(customerName);
        System.out.println("Enter your password: ");
        for (int attempts = 3 ; attempts > 0 ; attempts--) {
            String enteredPassword = scanner.nextLine();
            if (enteredPassword.equals(storedPassword)) {
                System.out.println("\nLogin successful. Welcome, " + customerName + "!");
                return true;
            } else {
                System.out.println("Incorrect password. Attempts remaining: " + (attempts - 1));
                if (attempts - 1 == 0) {
                    System.out.println("\nToo many failed attempts.");
                    System.out.println("Don't remember your password? Would you like to change it? (yes/no): ");
                    String response = scanner.nextLine().trim().toLowerCase();
                    switch (response) {
                        case "yes":
                            updatePassword(customerName, scanner);
                            return false;
                        default:
                            System.out.println("\nReturning to Main Menu...");
                            return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Generates and assigns a random password to a specific customer.
     * @param customerName The name of the customer for whom the password is being generated.
     * @return The generated password.
     */
    public String generatePassword(String customerName) {
        String password = RandomPasswordGenerator.generate();
        passwords.put(customerName, password);
        return password;
    }
    
    /**
     * Inner class for generating random passwords.
     * The generated passwords are 8 characters long and consist of uppercase,
     * lowercase, and numeric characters.
     */
    private static class RandomPasswordGenerator {
        /** The only length a password can have. */
        private static int passwordLength = 8;

        /**
         * Generates a random password of the specified length.
         * @return The generated password as a string.
         */
        public static String generate() {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            StringBuilder password = new StringBuilder();

            for (int i = 0; i < passwordLength; i++) {
                password.append(characters.charAt(random.nextInt(characters.length())));
            }
            return password.toString();
        }
    }
}

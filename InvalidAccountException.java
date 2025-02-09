/**
 * Exception thrown when an invalid account type is provided to the AccountFactory.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class InvalidAccountException extends Exception {
    /**
     * Constructs a new InvalidAccountException with the specified error message.
     * @param message The detail message explaining the reason for the exception.
     */
    public InvalidAccountException(String message) {
        super(message);
    }
}

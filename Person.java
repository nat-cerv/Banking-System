
/**
 * The Person class represents an individual with personal information such as 
 * identification number, name, date of birth, address, and phone number.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public abstract class Person {
    /** The unique identification number assigned to the customer or bank employee.*/
    private int identificationNumber;
    /** The first name of the customer or bank employee.*/
    private String firstName;
    /** The last name of the customer or bank employee.*/
    private String lastName;
    /** The date of birth of the customer or bank employee.*/
    private String dateOfBirth;
    /** The address of the customer or bank employee.*/
    private String address;
    /** The phone number of the customer or bank employee. */
    private String phoneNumber;

    /**
     * Default constructor that initializes an empty Person object.
     */
    public Person() {
    }

   /**
     * Constructor with parameters to initialize a Person object with the given personal details.
     *
     * @param identificationNumber the person's identification number
     * @param firstName            the person's first name
     * @param lastName             the person's last name
     * @param dateOfBirth          the person's date of birth
     * @param address              the person's address
     * @param phoneNumber          the person's phone number
     */
    public Person(int identificationNumber, String firstName, String lastName, String dateOfBirth, String address, String phoneNumber) {
        this.identificationNumber = identificationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Retrieves the person's identification number.
     * 
     * @return the identification number
     */
    public int getIdentificationNumber() {
        return identificationNumber;
    }

    /**
     * Sets the person's identification number.
     * 
     * @param identificationNumber the new identification number
     */
    public void setIdentificationNumber(int identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    /**
     * Retrieves the person's first name.
     * 
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the person's first name.
     * 
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the person's last name.
     * 
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the person's last name.
     * 
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the person's date of birth.
     * 
     * @return the date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    /**
     * Sets the person's date of birth.
     * 
     * @param dateOfBirth the new date of birth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Retrieves the person's address.
     * 
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the person's address.
     * 
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves the person's phone number.
     * 
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Sets the person's phone number.
     * 
     * @param phoneNumber the new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

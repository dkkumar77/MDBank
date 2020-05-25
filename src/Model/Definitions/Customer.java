package Model.Definitions;

/**
 *   THIS CLASS SHOULD DEFINE CUSTOMER
 */
public class Customer
{
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String dateOfBirth;
	private long customerID;

	public Customer(String username, String fullName, String email, String dateOfBirth,
	                long customerID)
	{
		this.username = username;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.customerID = customerID;
		setNames(fullName);
	}

	public String getUsername()
	{
		return username;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public String getDateOfBirth()
	{
		return dateOfBirth;
	}

	public long getCustomerID()
	{
		return customerID;
	}

	private void setNames(String fullName)
	{
		String[] name = fullName.split(" ");
		this.firstName = name[0];
		this.lastName = name[1];
	}
}

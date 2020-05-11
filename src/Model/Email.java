package Model;

/**
 * THIS CLASS SHOULD DEFINE AN EMAIL
 */
public class Email
{
	private String emailAddress;
	private String subject;
	private String content;

	public Email(String emailAddress, String subject, String content)
	{
		this.emailAddress = emailAddress;
		this.subject = subject;
		this.content = content;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
}

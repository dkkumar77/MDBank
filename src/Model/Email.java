package Model;

import java.util.List;

/**
 * THIS CLASS SHOULD DEFINE AN EMAIL
 */
public class Email
{
	private String emailAddress;
	private String subject;
	private String content;
	private List<String> attachments;

	public Email(String emailAddress, String subject, String content, List<String> attachments)
	{
		this.emailAddress = emailAddress;
		this.subject = subject;
		this.content = content;
		this.attachments = attachments;
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

	public List<String> getAttachments()
	{
		return this.attachments;
	}

	public int getNumOfAttachments()
	{
		return this.attachments.size();
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
}

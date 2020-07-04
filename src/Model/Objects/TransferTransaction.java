package Model.Objects;

public class TransferTransaction
{
	private final String amountToTransfer;
	private final String senderUsername;
	private final String recipientUsername;

	public TransferTransaction(String senderUsername, String recipientUsername, String amountToTransfer)
	{
		this.senderUsername = senderUsername;
		this.recipientUsername = recipientUsername;
		this.amountToTransfer = amountToTransfer;
	}

	public String getAmountToTransfer()
	{
		return amountToTransfer;
	}

	public String getSenderUsername()
	{
		return senderUsername;
	}

	public String getRecipientUsername()
	{
		return recipientUsername;
	}

}

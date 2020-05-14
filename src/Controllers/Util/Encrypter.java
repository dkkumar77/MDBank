package Controllers.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypter
{
	public static String getEncryptedPassword(String password)
	{
		return hashPassword(password);
	}

	private static String hashPassword(String password)
	{
		try{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = messageDigest.digest(password.getBytes());
			StringBuilder tempString = new StringBuilder();
			for(int i = 0; i<bytes.length;i++){
				tempString.append(Integer.toString((bytes[i]&0xff)+0x100,16).substring(1));
			}
			return tempString.toString();
		}
		catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return null;
	}
}

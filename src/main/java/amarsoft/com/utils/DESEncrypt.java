package amarsoft.com.utils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESEncrypt {
	public static byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
		return encrypt(paramArrayOfByte1, 1, paramArrayOfByte2);
	}

	public static byte[] encrypt(byte[] paramArrayOfByte) {
		return encrypt(paramArrayOfByte, null);
	}

	public static byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
		return encrypt(paramArrayOfByte1, 2, paramArrayOfByte2);
	}

	public static byte[] decrypt(byte[] paramArrayOfByte) {
		return decrypt(paramArrayOfByte, null);
	}

	public static String encrypt(String paramString1, String paramString2) {
		if (paramString1 == null)
			return null;
		byte[] arrayOfByte1 = string2Bytes(paramString1);
		byte[] arrayOfByte2 = encrypt(arrayOfByte1, paramString2 == null ? null : string2Bytes(paramString2));
		StringBuffer localStringBuffer = new StringBuffer(1024);
		if ((arrayOfByte2 == null) || (arrayOfByte2.length < 1))
			return null;
		Random localRandom = new Random(paramString1.length());
		for (int i = 0; i < arrayOfByte2.length; i++) {
			char c = (char) (localRandom.nextInt(10) + 71);
			localStringBuffer.append(c);
			if (arrayOfByte2[i] < 0) {
				c = (char) (localRandom.nextInt(10) + 81);
				arrayOfByte2[i] = ((byte) -arrayOfByte2[i]);
				localStringBuffer.append(c);
			}
			localStringBuffer.append(Integer.toString(arrayOfByte2[i], 16).toUpperCase());
		}
		localStringBuffer.deleteCharAt(0);
		return localStringBuffer.toString();
	}

	public static String encrypt(String paramString) {
		return encrypt(paramString, null);
	}

	public static String decrypt(String paramString1, String paramString2) {
		if (paramString1.length() < 1)
			return null;
		String[] arrayOfString = paramString1.split("[G-Pg-p]");
		byte[] arrayOfByte1 = new byte[arrayOfString.length];
		for (int j = 0; j < arrayOfByte1.length; j++) {
			int i = arrayOfString[j].charAt(0);
			if (((i >= 81) && (i <= 90)) || ((i >= 113) && (i <= 122)))
				arrayOfByte1[j] = ((byte) -Byte.parseByte(arrayOfString[j].substring(1), 16));
			else
				arrayOfByte1[j] = Byte.parseByte(arrayOfString[j], 16);
		}
		byte[] arrayOfByte2 = decrypt(arrayOfByte1, paramString2 == null ? null : string2Bytes(paramString2));
		if ((arrayOfByte2 == null) || (arrayOfByte2.length < 1))
			return null;
		return bytes2String(arrayOfByte2);
	}

	public static String decrypt(String paramString) {
		return decrypt(paramString, null);
	}

	private static byte[] encrypt(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2) {
		Object localObject;
		if ((paramArrayOfByte2 == null) || (paramArrayOfByte2.length < 8)) {
			localObject = new byte[] { -57, 115, 33, -116, 126, -56, -18, -103 };
			if (paramArrayOfByte2 != null)
				for (int i = 0; i < paramArrayOfByte2.length; i++)
					((byte[]) localObject)[i] = paramArrayOfByte2[i];
			paramArrayOfByte2 = (byte[]) localObject;
		}
		byte[] arrayOfByte;
		try {
			SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec localDESKeySpec = new DESKeySpec(paramArrayOfByte2);
			localObject = localSecretKeyFactory.generateSecret(localDESKeySpec);
			Cipher localCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			localCipher.init(paramInt, (Key) localObject);
			arrayOfByte = localCipher.doFinal(paramArrayOfByte1);
		} catch (Exception localException) {
			arrayOfByte = null;
		}
		return arrayOfByte;
	}

	private static byte[] string2Bytes(String paramString) {
		byte[] arrayOfByte = null;
		try {
			arrayOfByte = paramString.getBytes("UTF-8");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			arrayOfByte = paramString.getBytes();
		}
		return arrayOfByte;
	}

	private static String bytes2String(byte[] paramArrayOfByte) {
		String str = null;
		try {
			str = new String(paramArrayOfByte, "UTF-8");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			str = new String(paramArrayOfByte);
		}
		return str;
	}
}

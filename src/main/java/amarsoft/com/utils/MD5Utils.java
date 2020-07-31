package amarsoft.com.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public MD5Utils() {
    }

    public static String crypt(String str) {
        if (str != null && str.length() != 0) {
            try {
                MessageDigest digester = MessageDigest.getInstance("MD5");
                digester.update(str.getBytes());
                byte[] hash = digester.digest();
                StringBuilder hexString = new StringBuilder();

                for (int i = 0; i < hash.length; ++i) {
                    if ((255 & hash[i]) < 16) {
                        hexString.append("0").append(Integer.toHexString(255 & hash[i]));
                    } else {
                        hexString.append(Integer.toHexString(255 & hash[i]));
                    }
                }

                return hexString.toString();
            } catch (NoSuchAlgorithmException var5) {
                var5.printStackTrace();
                return null;
            }
        } else {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }
    }
}

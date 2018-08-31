package audit_method;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

public class AH {

    public BigInteger p;
    public BigInteger location_number;
    public BigInteger[] arr;
    public BigInteger AH;

    public AH() {
        arr = new BigInteger[1024];
        p = new BigInteger(256, new Random());
        location_number = new BigInteger("1024");
        AH = BigInteger.ONE;
        for (int i = 0; i < 1024; i++) {
            arr[i] = sha256(i + "");
        }
        for (BigInteger i : arr) {
            AH = AH.multiply(i).mod(p);
        }
        System.out.println(AH);
    }

    public void delete(int proof) {
        BigInteger temp = sha256(proof+"");
        temp = temp.modInverse(p);
        AH = AH.multiply(temp).mod(p);
        arr[proof-1]=BigInteger.ONE;
        

    }

    public void add(String proof) {
        BigInteger temp = sha256(proof);
        AH = AH.multiply(temp).mod(p);
        
    }

    public void print() {
        for (BigInteger i : arr) {
            System.out.println(i + " ");
        }
    }

    private BigInteger sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            return new BigInteger(hash);
//            StringBuffer hexString = new StringBuffer();
//
//            for (int i = 0; i < hash.length; i++) {
//                String hex = Integer.toHexString(0xff & hash[i]);
//                if (hex.length() == 1) {
//                    hexString.append('0');
//                }
//                hexString.append(hex);
//            }
//
//            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

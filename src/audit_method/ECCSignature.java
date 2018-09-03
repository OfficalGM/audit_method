package audit_method;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ECCSignature {

    public static void main(String args[]) {
        for (int i = 0; i < 11; i++) {
            jdkECDSA();
        }

    }

    public static void jdkECDSA() {
        try {
            //init
            String src = "AA";
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyPairGenerator.initialize(256, random);
            KeyPair pair = keyPairGenerator.generateKeyPair();
            ECPublicKey ecPublicKey = (ECPublicKey) pair.getPublic();
            ECPrivateKey ecPrivateKey = (ECPrivateKey) pair.getPrivate();
            //sign
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());

            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] res = signature.sign();
//            System.out.println("sign:" + HexBin.encode(res));
            //verify
            long a = System.nanoTime();
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean bool = signature.verify(res);
//            System.out.println("verify:" + bool);
            long b = System.nanoTime();
            System.out.println((double) (b - a) / 1000000);
        } catch (Exception ex) {

        }
    }
}

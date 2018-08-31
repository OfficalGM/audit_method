package audit_method;

import java.security.MessageDigest;
import java.util.List;

public class Block {

    public String hash;
    public String previousHash;
    public List tx;
    public long timeStamp;
    public String merkle_root;

    public Block(String previousHash, String merkle_root, List<String> traction) {
        this.previousHash = previousHash;
        this.merkle_root = merkle_root;
        this.tx = traction;
        this.timeStamp = System.currentTimeMillis();
        this.hash = sha256(timeStamp + timeStamp + merkle_root);
    }

    private String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

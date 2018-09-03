package audit_method;

import java.math.BigInteger;
import java.security.MessageDigest;

public class JavaTest2 {

    public static int[] random = {9353, 84769, 46569, 127170, 93812, 10584, 64014, 93570, 16714, 87483};

    public static void main(String args[]) {

        for (int j = 0; j < 11; j++) {
            long a = System.nanoTime();
            AH ah = new AH();
            for (int i = 0; i < (1 << 17); i++) {
                ah.Add(i + "", i + "");
//                System.out.println(i);
            }
            ah.updateAH();
            long b = System.nanoTime();
//            System.out.println(ah.AH);

            long c = System.nanoTime();
            for (int i = 0; i < 10; i++) {
//                ah.Delete(random[i]+"", random[i]+"");
                ah.audit(random[i] + "");
            }
            long d = System.nanoTime();
            long e = System.nanoTime();
            for (int i = 0; i < 1; i++) {
//                ah.Delete(random[i]+"", random[i]+"");
                ah.audit(random[0] + "");
            }
            long f = System.nanoTime();
            long g = System.nanoTime();
            for (int i = 0; i < 10; i++) {
                ah.Delete(random[i] + "", random[i] + "");
            }
            long h = System.nanoTime();
            System.out.println("build:" + (double) (b - a) / 1000000);
            System.out.println("audit10:" + (double) (d - c) / 1000000);
            System.out.println("audit:" + (double) (f - e) / 1000000);
            System.out.println("del:" + (double) (h - g) / 1000000);
        }

//            System.out.println(ah.array[i]);
//        for(int i=0;i<1024;i++){
//            System.out.println(ah.array[i]);
//        }
    }

    private static BigInteger sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));

            return new BigInteger(hash);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

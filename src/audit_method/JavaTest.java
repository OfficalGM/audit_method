package audit_method;

import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class JavaTest {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static void main(String args[]) {
        List<String> list = new ArrayList<>();
        Merkle_tree mk = new Merkle_tree(3);
        int number = 13;
        int k = 0;
        System.out.println((1 << number) / 1024);
        long a = System.nanoTime();

        for (int i = 1; i <= (1 << number) / 1024; i++) {
            for (int j = k; j < (1024 * i); j++) {
//                list.add(j+"");
                list.add(sha256(j + ""));
            }
            mk.create(list);
            if (i == 1) {
                blockchain.add(new Block("0", mk.list.get(0), list));
            } else {
                blockchain.add(new Block(blockchain.get(i - 2).hash, mk.list.get(0), list));
            }
            mk.clear();
            list=new ArrayList<>();
            k = 1024 * i;

        }
        long b = System.nanoTime();
        System.out.println((double) (b - a) / 1000000);
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);

        write_file(blockchainJson);

    }

    private static String sha256(String base) {
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

    public static void write_file(String data) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("./block.json"));
            os.write(data.getBytes());
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

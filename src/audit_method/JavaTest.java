package audit_method;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class JavaTest {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static void main(String args[]) {
        List<String> list = new ArrayList<>();
        Merkle_tree mk = new Merkle_tree(10);
        long a = System.nanoTime();
//        Run_AddBlock_Test(mk, list);
        String json = Read_file();

        json_analysis(json);
        long b = System.nanoTime();
        System.out.println((double) (b - a) / 1000000);
//        String blockchainJson = new Gson().toJson(blockchain);
//        System.out.println(blockchainJson);
//        Run_AddBlock_Test(mk, list);
//        write_file(blockchainJson);
    }

    public static void json_analysis(String json) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        JsonArray jsonArray = element.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            String previous= jsonArray.get(i).getAsJsonObject().get("previousHash").getAsString();
            String hash = jsonArray.get(i).getAsJsonObject().get("hash").getAsString();
            System.out.println(previous+" "+hash);
        }

    }

    public static void Run_AddBlock_Test(Merkle_tree mk, List list) {
        int number = 17;
        int k = 0;
        System.out.println((1 << number) / 1024);

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
            list = new ArrayList<>();
            k = 1024 * i;

        }
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

    public static String Read_file() {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("./block.json"));
//            System.out.println(new String(encoded, "UTF-8"));
            return new String(encoded, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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

package audit_method;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class JavaTest {

    public static ArrayList<Block> blockchain;
    public static int[] random = {9353, 84769, 46569, 127170, 93812, 10584, 64014, 93570, 16714, 87483};
    public static int[] block_number = {10, 83, 46, 125, 92, 11, 63, 92, 17, 86};
    public static String[] block_hash = {
        "1b7c5612545c7b241c4bdef53c91016bcf4fdc087dd582936984641f00cd452f",
        "46969202c0207674fc2c49c1f6c85f18eeab3313adfafc3fba6357d51842b709",
        "fe1b7e7b02b2a4c59dc2197ff8579a836bc5a9f5297ae69588b5ed50c2c2fd6d",
        "103d8b822bafd2b7d83432416e06ef0899e83206526c20bbe6373818f536716b",
        "3a6d9b4d6a82626a8bb6c71660c4a61cfbc7d2a6eb1948cc8aad277760b0ece0",
        "2e8becf7b76902d0b4f0a68f846d611dc2527b969c4b0a6f812e1cb43582eaf0",
        "f3edf8de7705fe25fcc7f478e610ef9c29e125f04b1348ccd630b1296a7ac5e5",
        "3a6d9b4d6a82626a8bb6c71660c4a61cfbc7d2a6eb1948cc8aad277760b0ece0",
        "f834c4b658f2f2f321c427e0196120a1e377b9ba504e7d39cb57db4f5644af60",
        "67fb59e482b29243fd42ee2862317878de8525146c14d7b4e3914c9b7a67ad1e"
    };
    public static String[] block_hash2 = new String[10];

    public static void main(String args[]) {
        for (int i = 0; i < 11; i++) {
            blockchain = new ArrayList<Block>();
            write();
//            for (int j = 0; j < 10; j++) {
//                block_hash2[j] = blockchain.get(block_number[j] - 1).hash;
//            }
//            audit();
        }

//        String json = Read_file();
//        for(int i=0;i<11;i++)
//            json_analysis(json);
    }

    public static void write() {
        List<String> list = new ArrayList<>();
        Merkle_tree mk = new Merkle_tree(11);
        long a = System.nanoTime();
        Run_AddBlock_Test(mk, list);
        long b = System.nanoTime();
//        System.out.println("build:" + (double) (b - a) / 1000000);
        System.out.println((double) (b - a) / 1000000);
//        for (int i = 0; i < 128; i++) {
//            String blockchainJson = new Gson().toJson(blockchain.get(i));
//            write_file(blockchainJson, (i + 1) + "");
//        }
//        String blockchainJson = new Gson().toJson(blockchain);
//        write_file(blockchainJson);
    }

    public static void audit() {
        long a = System.nanoTime();
        int index = 0;
        for (int i = 0; i < 10; i++) {
            index = block_number[i] - 1;
            String hash = blockchain.get(index).hash;
            Merkle_tree mk = new Merkle_tree(11);
            ArrayList<String> list = (ArrayList<String>) blockchain.get(index).tx;
            mk.create(list);
            mk.Verify(random[i] + "");
            for (int j = index; j < blockchain.size(); j++) {
                String previous_hash = blockchain.get(j).previousHash;
                String root = blockchain.get(j).merkle_root;
                String ts = blockchain.get(j).timeStamp + "";
                String sha = sha256(ts + previous_hash + root);
                hash = blockchain.get(j).hash;

                sha.equals(hash);
            }
        }
        long b = System.nanoTime();
        System.out.println((double) (b - a) / 1000000);

    }

    public static void json_analysis(String json) {
        long a = System.nanoTime();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
//        JsonObject jsonObject = element.getAsJsonObject();

        int index = 0;
        JsonArray jsonArray = element.getAsJsonArray();
        for (int i = 0; i < 10; i++) {
            String hash = jsonArray.get(block_number[i] - 1).getAsJsonObject().get("hash").getAsString();
            if (hash.equals(block_hash[i])) {
                index = block_number[i] - 1;

                Merkle_tree mk = new Merkle_tree(10);
                JsonArray tx = jsonArray.get(index).getAsJsonObject().get("tx").getAsJsonArray();
                ArrayList<String> list = new Gson().fromJson(tx.toString(), new TypeToken<List<String>>() {
                }.getType());
                mk.create(list);
                mk.Verify(random[i] + "");
                list.clear();

                for (int j = index; j < jsonArray.size(); j++) {
                    String previous_hash = jsonArray.get(j).getAsJsonObject().get("previousHash").getAsString();
                    String root = jsonArray.get(j).getAsJsonObject().get("merkle_root").getAsString();
                    String ts = jsonArray.get(j).getAsJsonObject().get("timeStamp").getAsString();
                    String sha = sha256(ts + previous_hash + root);
                    hash = jsonArray.get(j).getAsJsonObject().get("hash").getAsString();
//                    System.out.println(sha.equals(hash));
                }
            }

        }
        long b = System.nanoTime();
        System.out.println((double) (b - a) / 1000000);
    }

    public static void Run_AddBlock_Test(Merkle_tree mk, List list) {
        int number = 17;
        int k = 0;
        for (int i = 1; i <= (1 << number) / 2048; i++) {
            for (int j = k; j < (2048 * i); j++) {
//                list.add(j + "");
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
            k = 2048 * i;

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

    public static String Read_file(String number) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("./block/block" + number + ".json"));
            return new String(encoded, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String Read_file() {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("./block.json"));
            return new String(encoded, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void write_file(String data) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("./block" + ".json"));
            os.write(data.getBytes());
            os.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public static void write_file(String data, String index) {

        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("./block/block" + index + ".json"));
            os.write(data.getBytes());
            os.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }
}

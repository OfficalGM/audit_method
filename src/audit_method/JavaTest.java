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

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int[] random = {9353, 84769, 46569, 127170, 93812, 10584, 64014, 93570, 16714, 87483};
    public static int[] block_number = {10, 83, 46, 125, 92, 11, 63, 92, 17, 86};
    public static String[] block_hash = {
        "5ca0ef1305cc097b355c920e0c66da12b91ce605119465909c807fc34dc6fffa",
        "3cc0af3ba123abbf751ac2e8f4b6dda9c05e0aadcb3b84fb946ba9da14c4ee63",
        "380e79acd537aae852741d8855d094217ef87c923a1d1a5ec45085e15b4aaa12",
        "b44ea78024e614701c37cf89a82f9f3510751ac85235cebd2358b7314422a4e2",
        "4d8c659de50eeaf1fc746fbfb0b1a45b1ba96e2694568b69eeeee793cd9340df",
        "39515ebd8d6ba885b4afc9417e407aca378f6242da692e872ee1aca4dd346923",
        "278f9c0480b5645023494239ceb9ad5eb21574de19e9e7449910f4061fb360d3",
        "4d8c659de50eeaf1fc746fbfb0b1a45b1ba96e2694568b69eeeee793cd9340df",
        "ca83c5360e99ef07903ab31fb902846974865472a4e57f459b4a631e34b22efd",
        "d374f84cc2cf8d6e4b8ef3aba83b39788529dfec73094960ba6e13947958054e"
    };

    public static void main(String args[]) {
//        write();
        String json = Read_file();
        json_analysis(json);

    }

    public static void write() {
        List<String> list = new ArrayList<>();
        Merkle_tree mk = new Merkle_tree(10);
        Run_AddBlock_Test(mk, list);
        for (int i = 0; i < 128; i++) {
            String blockchainJson = new Gson().toJson(blockchain.get(i));
            write_file(blockchainJson, (i + 1) + "");
        }
        String blockchainJson = new Gson().toJson(blockchain);
        write_file(blockchainJson);
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
                
                
                for (int j = index+1; j < jsonArray.size(); j++) {
                   
                }
            }

        }
        long b = System.nanoTime();
        System.out.println((double) (b - a) / 1000000);
    }

    public static void Run_AddBlock_Test(Merkle_tree mk, List list) {
        int number = 17;
        int k = 0;
        for (int i = 1; i <= (1 << number) / 1024; i++) {
            for (int j = k; j < (1024 * i); j++) {
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

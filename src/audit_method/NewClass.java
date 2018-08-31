package audit_method;

import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class NewClass {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static void main(String args[]) {
        // 1<<18;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            list.add(i + "");
        }
        blockchain.add(new Block("0", "0", list));
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
//        write_file(blockchainJson);
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

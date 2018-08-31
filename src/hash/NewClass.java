package hash;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class NewClass {

    public static void main(String args[]) {
        int height = 10;
        List<String> list = new ArrayList<>();
//        BigInteger arr[]=new BigInteger[1024];
        for (int i = 0; i < (1 << height); i++) {
            list.add(i + "");
//            arr[i]=new BigInteger(i+"");
        }
        long a = System.currentTimeMillis();
        Merkle_tree m = new Merkle_tree(list, height);
        
        m.Verify("7");
//        for (int i = 0; i < 100; i++) {
//            ;
//        }
        long b = System.currentTimeMillis();
        System.out.println(m.list);
        System.out.println((double) (b - a) / 1000);
        long c = System.currentTimeMillis();
        AH ah = new AH();
        ah.delete(7);
        long e = System.currentTimeMillis();
        System.out.println((double) (e - c) / 1000);
        ah.add("7");

    }

}

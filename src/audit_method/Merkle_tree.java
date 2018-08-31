package audit_method;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Merkle_tree {

    public List<String> list = new ArrayList<>();
    private int height;

    public Merkle_tree(int height) {
        this.height = height;
    }

    public void create(List traction) {
        for (int i = traction.size() - 1; i >= 0; i--) {
            this.list.add((String) traction.get(i));
//            this.list.add(sha256((String) traction.get(i)));
        }
          
        List<String> tmp_list = compute(list);
        
        list.addAll(tmp_list);
      
        while (tmp_list.size() != 1) {
           
            tmp_list = compute(tmp_list);
           
            list.addAll(tmp_list);
            
        }
      
        Collections.reverse(list);

    }

    public void clear() {
        this.list.clear();
    }

    private List<String> compute(List list2) {
        int index = 0;
        List<String> tmp_list = new ArrayList();
        while (index < list2.size()) {
            String sha = sha256((String) list2.get(index + 1) + (String) list2.get(index));
//            String sha = (String) list2.get(index + 1) + (String) list2.get(index);
            tmp_list.add(sha);
            index++;
            index++;
        }

        return tmp_list;
    }

    private Boolean Verify_tree(String node, int index) {
        return node.equals(list.get(index));
    }

    public Boolean Verify(String proof) {
        proof = sha256(proof);
        int index = 0;

        for (int i = this.list.size() - 1; i >= (1 << height - 1) - 1; i--) {

            if (proof.equals(list.get(i))) {
                index = i;
                break;
            }
        }

        Boolean b = false;
        while (index > 0) {

            if (index % 2 == 0) {
                proof = sha256(list.get(index - 1) + list.get(index));
//                proof = list.get(index - 1) + list.get(index);
            } else {
                proof = sha256(list.get(index) + list.get(index + 1));
//                proof = list.get(index) + list.get(index + 1);
            }
            index = (index - 1) / 2;
            b = Verify_tree(proof, index);

        }
        return b;
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

package audit_method;

import java.math.BigInteger;

public class FBHTree {

    private int height;
    public Node[] nodes;
    public int leaf_height;

    public FBHTree(int treeHeight) {
        if (treeHeight < 0) {
            throw new IllegalArgumentException("The minimum value for tree height is 1.");
        }
        this.height = treeHeight;
        this.leaf_height = (1 << (height - 1));
        this.nodes = new Node[1 << treeHeight];
        for (int i = nodes.length - 1; i > 0; i--) {
            if (i >= (1 << (height - 1))) {//leaf node
                nodes[i] = new Node(i, null, null);
            } else {//internal node
                nodes[i] = new Node(i, nodes[i * 2], nodes[(i * 2) + 1]);
            }
        }

    }

    public int calcLeafIndex(String path) {
        BigInteger index = HashUtil.sha256_BigInteger(path);
        index = index.mod(new BigInteger(leaf_height + ""));
        return leaf_height + index.intValue();
    }

    public void put(String path, String file) {
        int index = calcLeafIndex(path);
        String f = HashUtil.sha256(file);
        String p = HashUtil.sha256(path);
        nodes[index].hashmap.put(p, f);
        multiply(index);
        update_node(index);
    }

    private void multiply(int index) {
        String hash = null;
        for (Object i : nodes[index].hashmap.keySet()) {
            String key = i.toString();
            String value = nodes[index].hashmap.get(i).toString();
            String sha = HashUtil.sha256(key + "" + value + "");
            hash += sha;
        }

        nodes[index].hash = HashUtil.sha256(hash + "");
    }

    private void update_node(int index) {
        int root_height = 1;
        int tree_height = height;
        while (tree_height > root_height) {
            if (index % 2 == 1) {
                nodes[index / 2].hash = HashUtil.sha256(nodes[index - 1].hash + nodes[index].hash);
            } else {
                nodes[index / 2].hash = HashUtil.sha256(nodes[index].hash + nodes[index + 1].hash);
            }
            index = index / 2;
            tree_height--;
        }
    }

    public boolean audit(String path) {
        Boolean flag = false;
        int index = calcLeafIndex(path);
        String hash = null;
        for (Object i : nodes[index].hashmap.keySet()) {
            String key = i.toString();
            String value = nodes[index].hashmap.get(i).toString();
            String sha = HashUtil.sha256(key + "" + value + "");
            hash += sha;
        }
        flag = nodes[index].hash.equals(HashUtil.sha256(hash + ""));
        if (flag) {
            return audit_internal_node(index);
        }
        return flag;
    }

    private boolean audit_internal_node(int index) {
        Boolean flag = false;
        int root_height = 1;
        int tree_height = height;

        while (tree_height > root_height) {
            tree_height--;
            if (index % 2 == 1) {

                flag = HashUtil.sha256(nodes[index - 1].hash + nodes[index].hash).equals(nodes[index / 2].hash);
            } else {
                flag = HashUtil.sha256(nodes[index].hash + nodes[index + 1].hash).equals(nodes[index / 2].hash);
            }
            if (flag) {
                index = index / 2;
            } else {
                return flag;
            }
        }
        return flag;
    }

    public void remove(String path) {
        int index = calcLeafIndex(path);
        String p = HashUtil.sha256(path);
        String hash = null;
        nodes[index].hashmap.remove(p);
        for (Object i : nodes[index].hashmap.keySet()) {
            String key = i.toString();
            String value = nodes[index].hashmap.get(i).toString();
            String sha = HashUtil.sha256(key + "" + value + "");
            hash += sha;
        }

        nodes[index].hash = HashUtil.sha256(hash + "");
        update_node(index);
    }

    public void node_println() {
        for (int i = 1; i < nodes.length; i++) {
            System.out.println(i + " " + nodes[i].hash);
        }
    }
}

package audit_method;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AH {
    
    public ArrayList<HashMap<BigInteger, BigInteger>> list = new ArrayList();
    private int arrlength = 128;
    public BigInteger[] array = new BigInteger[arrlength];
    private BigInteger prime;
    public BigInteger AH = BigInteger.ONE;
    
    public AH() {
        prime = BigInteger.probablePrime(256, new Random());
        for (int i = 0; i < arrlength; i++) {
            HashMap<BigInteger, BigInteger> map = new HashMap();
            list.add(map);
        }
        
    }
    
    public boolean audit(String path) {
        BigInteger index = index_function(path);
        BigInteger ah = BigInteger.ONE;
        for (Object i : list.get(index.intValue()).keySet()) {
            BigInteger key = (BigInteger) i;
            BigInteger value = (BigInteger) list.get(index.intValue()).get(i);
            BigInteger sha = sha256(key + "" + value + "");
            ah = ah.multiply(sha).mod(prime);
        }
        
        return ah.equals(array[index.intValue()]);
    }
    
    public void Delete(String path, String file) {
        BigInteger index = index_function(path);
        BigInteger f = sha256(file);
        BigInteger p = sha256(path);
        BigInteger AH_inverse = sha256(f + "" + p).modInverse(prime);
        list.get(index.intValue()).remove(p, f);
        array[index.intValue()] = array[index.intValue()].multiply(AH_inverse).mod(prime);
    }
    
    public void Add(String path, String file) {
        BigInteger index = index_function(path);
        BigInteger f = sha256(file);
        BigInteger p = sha256(path);
        list.get(index.intValue()).put(p, f);
        multiply(list.get(index.intValue()), index);
    }
    
    private void multiply(HashMap hashmap, BigInteger index) {
        BigInteger ah = BigInteger.ONE;
        for (Object i : hashmap.keySet()) {
            BigInteger key = (BigInteger) i;
            BigInteger value = (BigInteger) hashmap.get(i);
            BigInteger sha = sha256(key + "" + value + "");
            ah = ah.multiply(sha).mod(prime);
        }
        array[index.intValue()] = ah;
    }
    
    public void updateAH() {
        for (int i = 0; i < array.length; i++) {
            AH = AH.multiply(array[i]).mod(prime);
        }
        
    }
    
    public BigInteger index_function(String proof) {
        return sha256(proof).mod(new BigInteger(arrlength + ""));
    }
    
    private BigInteger sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            
            return new BigInteger(hash);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
}

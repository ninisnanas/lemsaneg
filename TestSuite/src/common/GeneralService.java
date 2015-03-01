/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;
import java.util.BitSet;
import javax.xml.bind.DatatypeConverter;

/**
 * this class contains some generic functions related for test suite, bit operation etc
 * @author ASUS
 */
public class GeneralService {
    /**
     * get array of byte from specific bit set
     * @param bitset
     * @return 
     */
    public static byte[] getBytesFromBitSet(BitSet bitset) {
        byte[] bytes = new byte[(bitset.size() + 7) / 8];
        for (int i=0; i<bitset.length(); i++) {
            if (bitset.get(i)) {
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
            }
        }
        return bytes;
    }
    
    /**
     * get bit set object from specific hex
     * @param hex
     * @return 
     */
    public static BitSet getBitSetFromHex(String hex) {
        return BitSet.valueOf(getBytesFromHex(hex));
    }
    
    /**
     * get array of byte from specific hex string
     * @param hex
     * @return 
     */
    public static byte[] getBytesFromHex(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }
    
    /**
     * get hex string from specific bit set
     * @param bitset
     * @return 
     */
    public static String getHexFromBitSet(BitSet bitset) {
        return Long.toHexString(bitset.toLongArray()[0]);
    }
    
    /**
     * generate random hex text with specific size 
     * @param size
     * @return 
     */
    public static String generateRandomHexText(int size) {
        String result = "";
        for(int i = 0; i < size; i++) {
            int base = (int) (Math.random() * 15);
            switch(base) {
                case 10:
                    result += "A";
                    break;
                case 11:
                    result += "B";
                    break;
                case 12:
                    result += "C";
                    break;
                case 13: 
                    result += "D";
                    break;
                case 14:
                    result += "E";
                    break;
                case 15:
                    result += "F";
                    break;
                default: 
                    result += base;
                    break;
            }
        }
        
        return result;
    }
}

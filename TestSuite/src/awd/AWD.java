/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package awd;

import blockcipher.BlockCipherFactory;
import blockcipher.CipherType;
import java.util.BitSet;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author ASUS
 */
public class AWD {
    private final int N  = 128;
    private int numOfSample;
    private BitSet baseInput;
    private BitSet baseOutput;
    private BitSet flippedInput;
    private BitSet flippedOutput;
    private BitSet xorResult;
    private BlockCipherFactory cipherFactory;
    private byte[] plainText;
    
    private int[] awd_result = new int[N + 1];
    
    public static void main(String ar[]) {
        AWD awd = new AWD();
        awd.runTest();
        awd.printResult();
    }
    
    public AWD() {
        this(10000, "0123456789ABCDEFFEDCBA9876543210");
    }
    
    public AWD(int numOfSample, String strKey) {
        this.numOfSample = numOfSample;
        byte[] key_byte = toByteArray(transformFromHex(strKey));
        cipherFactory = new BlockCipherFactory(key_byte);
    }
    
    public void runTest() {
        String inputStr;
        int flipIndex;
        int hammingDistance;
        for(int i = 0; i < numOfSample; i++) {
            inputStr = generateRandomInput();
            baseInput = transformFromHex(inputStr);
            plainText = toByteArray(baseInput);
            
            baseOutput = BitSet.valueOf(cipherFactory.getEncrypt(plainText, CipherType.AES));
            flipIndex = (int) (Math.random() * N);
            
            flippedInput = BitSet.valueOf(toByteArray(baseInput));
            flippedInput.flip(flipIndex);
            plainText = toByteArray(flippedInput);
            
            xorResult = BitSet.valueOf(cipherFactory.getEncrypt(plainText, CipherType.AES));            
            xorResult.xor(baseOutput);
            
            hammingDistance = getHammingDistance(xorResult);
            awd_result[hammingDistance]++;
        }
    }
    
    private void printResult() {
        int max = 0;
        int maxIdx = -1;
        for(int i = 0; i < awd_result.length; i++) {
            if(awd_result[i] >= max) {
                max = awd_result[i];
                maxIdx = i;
            }
            System.out.println(i + ": " + awd_result[i]);
        }
        
        System.out.println("Max value: " + max);
        System.out.println("Max value index: " + maxIdx);
    }
    
    public static byte[] toByteArray(BitSet bits) {
        byte[] bytes = new byte[(bits.size() + 7) / 8];
        for (int i=0; i<bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
            }
        }
        return bytes;
    }
    
    private int getHammingDistance(BitSet bitset) {
        int result = 0;
        for(int i = 0; i < bitset.size(); i++) {
            if(bitset.get(i))
                result++;
        }
        
        return result;
    }
    
    private BitSet transformFromHex(String hex) {
        return BitSet.valueOf(transformFromHexToByte(hex));
    }
    
    private byte[] transformFromHexToByte(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }
    
    private String transformFromBitSet(java.util.BitSet bs) {
        return Long.toHexString(bs.toLongArray()[0]);
    }
    
    private String generateRandomInput() {
        String input = "";
        
        for(int i = 0; i < 32; i++) {
            int base = (int) (Math.random() * 15);
            switch(base) {
                case 10:
                    input += 'A';
                    break;
                case 11:
                    input += 'B';
                    break;
                case 12:
                    input += 'C';
                    break;
                case 13:
                    input += 'D';
                    break;
                case 14:
                    input += 'E';
                    break;
                case 15:
                    input += 'F';
                    break;
                default:
                    input += base;
                    break;
            }
        }
        return input;
    }
}

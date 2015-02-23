/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sac;

import blockcipher.BlockCipherFactory;
import blockcipher.CipherType;
import java.util.BitSet;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author nisrinarahmah
 */
public class SAC {
    private BitSet input;
    private BitSet output;
    private BitSet temp;
    private BitSet baseOutput;
    private BitSet xor_result;
    private BlockCipherFactory cipherFactory;
    private byte[] plainText;
    private String key = "0123456789ABCDEFFEDCBA9876543210";
    
    private int N = 128;
    private final int RUN = 1048576;
    private int[][] sac_matrix = new int[N][N];
    
    private int[] chi_table = {523857, 524158, 524417, 524718, 1048576};
    private double[] prob_chi_table = {0.200224, 0.199937, 0.199677, 0.199937, 0.200224};
    private int[] observed_freq = new int[5];
    private int[] expected_freq = new int[5];
    
    public SAC() {
        byte[] key_byte = transformFromHex(key).toByteArray();
        cipherFactory = new BlockCipherFactory(key_byte);
    }
    
    public double runTest() {
        String inputStr;
        for(int r = 0; r < RUN; r++) {
            inputStr = generateRandomInput();
            input = transformFromHex(inputStr);
            
            plainText = toByteArray(input);
            baseOutput = BitSet.valueOf(cipherFactory.getEncrypt(plainText, CipherType.AES));
  
            for(int i = 0; i < N; i++) {
                temp = BitSet.valueOf(input.toByteArray());
                temp.flip(i);

                plainText = toByteArray(temp);
                output = BitSet.valueOf(cipherFactory.getEncrypt(plainText, CipherType.AES));
                output.xor(baseOutput);

                for(int j = 0; j < N; j++) {
                    if(output.get(j))
                        sac_matrix[i][j]++;
                }
            }
            
            System.out.println(r);
            printSACMatrix();
        }
        
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                for(int k = 0; k < 5; k++) {
                    if(sac_matrix[i][j] <= chi_table[k]) {
                        observed_freq[k]++;
                    }
                }
            }
        }
        
        for(int i = 0; i < 5; i++) {
            expected_freq[i] = (int) (prob_chi_table[i] * N * N);
        }
        
        double cs = getChiSquare(5, observed_freq, expected_freq);
        return cs;
    }
    
    private byte[] generateRandomInputNew() {
        byte[] result = new byte[16];
        for(int i = 0; i < 16; i++) {
            result[i] = (byte) (Math.random() * 256);
        }
        return result;
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
    
    private void printSACMatrix()
    {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                System.out.print(sac_matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    private double getChiSquare(int i, int[] observed, int[] expected) {
        double cs = 0;
        for(int j = 0; j < i; j++) {
            int temp = observed[j] - expected[j];
            cs += Math.pow(cs, 2) / expected[j];
        }
        return cs;
    }
    
    private BitSet transformFromHex(String hex) {
        return BitSet.valueOf(transformFromHexToByte(hex));
    }
    
    private byte[] transformFromHexToByte(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }
    
    private String transformFromBitSet(BitSet bs) {
        return Long.toHexString(bs.toLongArray()[0]);
    }
    
    public static void main(String ar[]) {
        SAC sac = new SAC();
        sac.runTest();
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
}

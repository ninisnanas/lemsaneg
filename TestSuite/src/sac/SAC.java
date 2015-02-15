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
    private int N = 128;
    private int[][] sac_matrix = new int[N][N];
    private String key = "0123456789ABCDEFFEDCBA9876543210";
    
    public SAC() {
        byte[] key_byte = transformFromHex(key).toByteArray();
        cipherFactory = new BlockCipherFactory(key_byte);
    }
    
    public BitSet[] runTest() {
        input = transformFromHex("00000000000000000123456789ABCDEF");
        plainText = input.toByteArray();
        baseOutput = BitSet.valueOf(cipherFactory.getEncrypt(plainText, CipherType.AES));
        
        System.out.println("SAC Matrix init...");
        printSACMatrix();
        
        for(int i = 0; i < N; i++) {
            temp = BitSet.valueOf(input.toByteArray());
            temp.flip(i);
            
            plainText = temp.toByteArray();
            output = BitSet.valueOf(cipherFactory.getEncrypt(plainText, CipherType.AES));
            output.xor(baseOutput);
            
            for(int j = 0; j < N; j++) {
                if(output.get(j))
                    sac_matrix[i][j]++;
            }
            
            System.out.println("Perubahan bit ke " + i);
            printSACMatrix();
        }
        return null;
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
}

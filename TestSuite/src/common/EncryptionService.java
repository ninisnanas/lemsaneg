/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 *
 * @author ASUS
 */
public class EncryptionService {
    
    public static byte[] runEncryption(BufferedBlockCipher cipherBlock, byte[] plainText, KeyParameter key) throws Exception {
        cipherBlock.init(true, key);
        byte[] cipher = new byte[cipherBlock.getOutputSize(plainText.length)];
        int cipherLength = cipherBlock.processBytes(plainText, 0, plainText.length, cipher, 0);
        cipherLength += cipherBlock.doFinal(cipher, cipherLength);
        
        return cipher;
    }
    
    public static byte[] runEncryptionWithIV(BufferedBlockCipher cipherBlock, byte[] plainText, ParametersWithIV iv) throws Exception {
        cipherBlock.init(true, iv);
        byte[] cipher = new byte[cipherBlock.getOutputSize(plainText.length)];
        int cipherLength = cipherBlock.processBytes(plainText, 0, plainText.length, cipher, 0);
        cipherLength += cipherBlock.doFinal(cipher, cipherLength);
        
        return cipher;
    }
    
    public static byte[] runDecryption(BufferedBlockCipher cipherBlock, byte[] cipherText, KeyParameter key) throws Exception {
        cipherBlock.init(false, key);
        byte[] plain = new byte[cipherBlock.getOutputSize(cipherText.length)];
        int plainLength = cipherBlock.processBytes(cipherText, 0, cipherText.length, plain, 0);
        plainLength += cipherBlock.doFinal(plain, plainLength);
        
        return plain;
    }
}

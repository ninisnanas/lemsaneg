/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockcipher;

/**
 *
 * @author nisrinarahmah
 */
public class BlockCipherFactory {
    private byte[] key;
    
    public BlockCipherFactory() {}
    
    public BlockCipherFactory(byte[] key) {
        this.key = key;
    } 
    
    public byte[] getEncrypt(byte[] plain, CipherType type) {
        byte[] result = null;
        switch(type) {
            case AES:
                result = EncryptAES(plain);
                break;
            case DES:
                result = EncryptDES(plain);
                break;
            case RANDOM:
                break;
        }
        return result;
    }
    
    private byte[] EncryptAES(byte[] plain) {
        AES cipher = new AES();
        return cipher.encryptProcess(key, plain);
    }
    
    private byte[] EncryptDES(byte[] plain) {
        DES cipher = new DES();
        return cipher.encryptProcess(key, plain);
    }
}

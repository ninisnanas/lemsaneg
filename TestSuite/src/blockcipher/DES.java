/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockcipher;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * DES Class
 *
 * @author Aditya Try Anggoro
 * @author Harish Muhammad Nazief
 * @author Yehezkiel Chrisby Gulo
 */
public class DES {

    private byte[] key;
    private byte[] plain;
    private byte[] encrypt;
    private byte[] decrypt;

    /**
     * Constructor
     * @param null
     */
    public DES() {
        this.key = null;
        this.plain = null;
        this.encrypt = null;
        this.decrypt = null;
    }


    /**
     * Proses enkripsi AES memanfaatkan library JAVA
     *
     * @param keys
     * @param plains
     * @return byte[] encrypted
     */
    public byte[] encryptProcess(byte[] keys, byte[] plains) {
        try {
            Cipher c = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKey s = new SecretKeySpec(keys, "DES");
            c.init(Cipher.ENCRYPT_MODE, s);
            return c.doFinal(plains);
        } catch (Exception ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Proses dekripsi AES memanfaatkan library JAVA
     * 
     * @param keys
     * @param plains
     * @return byte[] decrypted
     */
    public byte[] decryptProcess(byte[] keys, byte[] plains) {
        try {
            Cipher c = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKey s = new SecretKeySpec(keys, "DES");
            c.init(Cipher.DECRYPT_MODE, s);
            return c.doFinal(plains);
        } catch (Exception ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Set nilai dari array decrypted
     * 
     * @param decrypt
     */
    public void setDecrypt(byte[] decrypt) {
        this.decrypt = decrypt;
    }

    /**
     * Set nilai dari array encrypted
     *
     * @param encrypt
     */
    public void setEncrypt(byte[] encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * Set nilai dari key
     * 
     * @param key
     */
    public void setKey(byte[] key) {
        this.key = key;
    }

    /**
     * Set nilai untuk array plaintext
     * 
     * @param plain
     */
    public void setPlain(byte[] plain) {
        this.plain = plain;
    }

    public String getResultDecrypt() {
        return DatatypeConverter.printHexBinary(decrypt);
    }

    public String getResultEncrypt() {
        return DatatypeConverter.printHexBinary(encrypt);
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getPlain() {
        return plain;
    }

    public byte[] getDecrypt() {
        return decrypt;
    }

    public byte[] getEncrypt() {
        return encrypt;
    }
}

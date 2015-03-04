
import common.EncryptionService;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Remmy
 */
public class TimePerformanceTest {
    private byte[] plaintext;
    private CipherParameters cipherParam;
    private BufferedBlockCipher cipher;
    
    public TimePerformanceTest(byte[] plaintext, CipherParameters cipherParam, BufferedBlockCipher cipher) {
        this.plaintext = plaintext;
        this.cipherParam = cipherParam;
        this.cipher = cipher;
    }
    
    public long countEncryption() throws DataLengthException, InvalidCipherTextException {
        long start = System.currentTimeMillis();
        byte[] ciphertext = EncryptionService.runEncryption(plaintext, cipherParam, cipher);
        long elapsed = System.currentTimeMillis() - start;
        return elapsed;
    }
    
    
}

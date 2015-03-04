/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 *
 * @author ASUS
 */
public class EncryptionService {

    public static byte[] runEncryption(byte[] plaintext, CipherParameters param, BufferedBlockCipher cipherBlock) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
		// Inisialisasi algo block cipher
        // true karena enkripsi, parameter param
        cipherBlock.init(true, param);
        // Mengambil ukuran plain text
        int ukuranPlainText = plaintext.length;
        // Mengambil ukuran cipher 
        int ukuranCipherText = cipherBlock.getOutputSize(ukuranPlainText);
        // Membuat array hasil ciphertext
        byte[] ciphertext = new byte[ukuranCipherText];

        // Memproses blok plaintext
        int cipherLength = cipherBlock.processBytes(plaintext, 0, ukuranPlainText, ciphertext, 0);
        // Memproses blok terakhir dari plaintext
        cipherBlock.doFinal(ciphertext, cipherLength);

        return ciphertext;

    }

    public static byte[] runDecryption(byte[] ciphertext, CipherParameters param, BufferedBlockCipher cipherBlock) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
		// Inisialisasi algo block cipher
        // false karena dekripsi, parameter param
        cipherBlock.init(false, param);
        // Mengambil ukuran plain text
        int ukuranCipherText = ciphertext.length;
        // Mengambil ukuran cipher 
        int ukuranPlainText = cipherBlock.getOutputSize(ukuranCipherText);
        // Membuat array hasil ciphertext
        byte[] plaintext = new byte[ukuranPlainText];

        // Memproses blok plaintext
        int plainLength = cipherBlock.processBytes(ciphertext, 0, ukuranCipherText, plaintext, 0);
        // Memproses blok terakhir dari plaintext
        cipherBlock.doFinal(plaintext, plainLength);

        return plaintext;

    }
}

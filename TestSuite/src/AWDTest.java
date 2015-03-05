
import common.CommonService;
import common.EncryptionService;
import java.util.BitSet;
import java.util.Random;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.util.Arrays;

public class AWDTest {

    private BufferedBlockCipher blockCipher;
    private CipherParameters param;
    private byte[] cipher;
    private int blocksize;
    private int numOfRound;
    private BitSet bitSetBasePlain;
    private BitSet bitSetTempPlain;
    private BitSet bitSetBaseCipher;
    private BitSet bitSetTempCipher;
    private int[] awdResults;

    public AWDTest(BufferedBlockCipher blockCipher, CipherParameters param, int numOfRound) {
        this.blockCipher = blockCipher;
        this.param = param;
        this.blocksize = blockCipher.getBlockSize();
        this.numOfRound = numOfRound;
        awdResults = new int[blocksize * 8 + 1];

    }

    public void test() throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        // untuk setiap ronde
        for (int ronde = 0; ronde < numOfRound; ronde++) {
            // Random Plain
            byte[] plain = CommonService.
                    generateRandomInput(blocksize);
            // Enkripsi plain
            cipher = EncryptionService.
                    runEncryption(plain, param, blockCipher);
            cipher = Arrays.copyOf(cipher, blockCipher.getBlockSize());

            // Konversi ke Bitset 
            bitSetBasePlain = BitSet.valueOf(plain);
            bitSetBaseCipher = BitSet.valueOf(cipher);

            // Random index
            Random rand = new Random();
            int randIndex = rand.nextInt(blocksize * 8);

            // Flip bit
            bitSetBasePlain.flip(randIndex);

            // Kembalikan plain ke byte[]
            plain = CommonService.bitsetToByte(bitSetBasePlain);

            // Enkripsi plain yang sudah diflip
            cipher = EncryptionService.
                    runEncryption(plain, param, blockCipher);
            cipher = Arrays.copyOf(cipher, blockCipher.getBlockSize());

            // Konversi ke Bitset 
            bitSetTempCipher = BitSet.valueOf(cipher);

            // Xor
            bitSetBaseCipher.xor(bitSetTempCipher);
            // Hitung jumlah 1

            int jumlahSatu = bitSetBaseCipher.cardinality();

            // Masukkin hamming distance
            awdResults[jumlahSatu]++;
        }
    }

    public void print() {
        for (int i = 0; i < awdResults.length; i++) {
            System.out.print(awdResults[i] + "\t");
        }
    }

}

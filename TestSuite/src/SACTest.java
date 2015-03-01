
import common.EncryptionService;
import common.GeneralService;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import java.util.BitSet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
public class SACTest implements UnitTestInterface {
    private BufferedBlockCipher blockCipher;
    private KeyParameter paramKey;
    private ParametersWithIV paramIV;
    private boolean isIV;
    
    private byte[] key;
    private byte[] iv;
    private byte[] plain;
    private byte[] cipher;
    private int blockSize;
    private int numOfRound;
    private BitSet bitSetBasePlain;
    private BitSet bitSetBaseCipher;
    private BitSet bitSetTempPlain;
    private BitSet bitSetTempCipher;
    private BitSet bitSetXOR;
    
    private int[][] sac_matrix = new int[blockSize][blockSize];
    private int[] chi_table = {523857, 524158, 524417, 524718, 1048576};
    private double[] prob_chi_table = {0.200224, 0.199937, 0.199677, 0.199937, 0.200224};
    private int[] observed_freq = new int[5];
    private int[] expected_freq = new int[5];
    private double chi_square;
    
    public SACTest(BufferedBlockCipher blockCipher) {
        this.blockCipher = blockCipher;
        this.blockSize = 128;
        this.numOfRound = 100;
        this.key = GeneralService.getBytesFromHex(GeneralService.generateRandomHexText(blockSize/4));
        this.paramKey = new KeyParameter(key); 
        this.isIV = false;
    }
    
    public SACTest(BufferedBlockCipher blockCipher, byte[] key, int numOfRound, int blockSize) {
        this.blockCipher = blockCipher;
        this.key = key;
        this.numOfRound = numOfRound;
        this.blockSize = blockSize;
        this.paramKey = new KeyParameter(key);
        this.isIV = false;
    }
    
    public SACTest(BufferedBlockCipher blockCipher, byte[] key, byte[] iv, int numOfRound, int blockSize) {
        this.blockCipher = blockCipher;
        this.key = key;
        this.iv = iv;
        this.numOfRound = numOfRound;
        this.blockSize = blockSize;
        this.paramKey = new KeyParameter(key);
        this.paramIV = new ParametersWithIV(paramKey, iv);
        this.isIV = true;
    }

    @Override
    public void runTest() throws Exception {
        String plainStr;
        for(int round = 0; round < numOfRound; round++) {
            plainStr = GeneralService.generateRandomHexText(blockSize/4);
            bitSetBasePlain = GeneralService.getBitSetFromHex(plainStr);
            plain = GeneralService.getBytesFromBitSet(bitSetBasePlain);
            
            if(isIV) {
                cipher = EncryptionService.runEncryptionWithIV(blockCipher, plain, paramIV);
            } else {
                cipher = EncryptionService.runEncryption(blockCipher, plain, paramKey);
            }
            
            bitSetBaseCipher = BitSet.valueOf(cipher);
            
            for(int idx = 0; idx < blockSize; idx++) {
                bitSetTempPlain = BitSet.valueOf(GeneralService.getBytesFromBitSet(bitSetBasePlain));
                bitSetTempPlain.flip(idx);
                
                plain = GeneralService.getBytesFromBitSet(bitSetTempPlain);
                
                if(isIV) {
                cipher = EncryptionService.runEncryptionWithIV(blockCipher, plain, paramIV);
                } else {
                    cipher = EncryptionService.runEncryption(blockCipher, plain, paramKey);
                }
                
                bitSetTempCipher = BitSet.valueOf(cipher);
                bitSetTempCipher.xor(bitSetBaseCipher);
                
                for(int j = 0; j < blockSize; j++) {
                    if(bitSetTempCipher.get(j))
                        sac_matrix[idx][j]++;
                }
            }
        }
        
        for(int i = 0; i < blockSize; i++) {
            for(int j = 0; j < blockSize; j++) {
                for(int k = 0; k < 5; k++) {
                    if(sac_matrix[i][j] <= chi_table[k]) {
                        observed_freq[k]++;
                    }
                }
            }
        }
        
        for(int i = 0; i < 5; i++) {
            expected_freq[i] = (int) (prob_chi_table[i] * blockSize * blockSize);
        }
        
        chi_square = getChiSquare(5, observed_freq, expected_freq);
    }

    @Override
    public String getResult() {
        return chi_square + "";
    }
    
    private double getChiSquare(int i, int[] observed, int[] expected) {
        double cs = 0;
        for(int j = 0; j < i; j++) {
            int temp = observed[j] - expected[j];
            cs += Math.pow(cs, 2) / expected[j];
        }
        return cs;
    }
}

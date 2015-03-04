
import common.EncryptionService;
import common.GeneralService;
import java.util.BitSet;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
public class AWDTest implements UnitTestInterface {
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
    
    private int[] awd_result;
    
    public AWDTest(BufferedBlockCipher blockCipher) {
        this.blockCipher = blockCipher;
        this.blockSize = 128;
        this.numOfRound = 1000;
        this.key = GeneralService.getBytesFromHex(GeneralService.generateRandomHexText(blockSize/4));
        this.paramKey = new KeyParameter(key); 
        this.isIV = false;
        awd_result = new int[blockSize + 1];
    }
    
    public AWDTest(BufferedBlockCipher blockCipher, byte[] key, int numOfRound, int blockSize) {
        this.blockCipher = blockCipher;
        this.key = key;
        this.numOfRound = numOfRound;
        this.blockSize = blockSize;
        this.paramKey = new KeyParameter(key);
        this.isIV = false;
        awd_result = new int[blockSize + 1];
    }
    
    public AWDTest(BufferedBlockCipher blockCipher, byte[] key, byte[] iv, int numOfRound, int blockSize) {
        this.blockCipher = blockCipher;
        this.key = key;
        this.iv = iv;
        this.numOfRound = numOfRound;
        this.blockSize = blockSize;
        this.paramKey = new KeyParameter(key);
        this.paramIV = new ParametersWithIV(paramKey, iv);
        this.isIV = true;
        awd_result = new int[blockSize + 1];
    }

    @Override
    public void runTest() throws Exception {
        String plainStr;
        int randomFlipIndex;
        int hammingDistance;
        for(int round = 0; round < numOfRound; round++) {
            plainStr = GeneralService.generateRandomHexText(blockSize/4);
            bitSetBasePlain = GeneralService.getBitSetFromHex(plainStr);
            plain = GeneralService.getBytesFromBitSet(bitSetBasePlain);
            
            if(isIV) {
                cipher = EncryptionService.runEncryption(plain, paramIV, blockCipher);
            } else {
                cipher = EncryptionService.runEncryption(plain, paramKey, blockCipher);
            }
            
            bitSetBaseCipher = BitSet.valueOf(cipher);
            randomFlipIndex = (int) (Math.random() * blockSize);
            
            bitSetTempPlain = BitSet.valueOf(GeneralService.getBytesFromBitSet(bitSetBasePlain));
            bitSetTempPlain.flip(randomFlipIndex);

            plain = GeneralService.getBytesFromBitSet(bitSetTempPlain);

            if(isIV) {
                cipher = EncryptionService.runEncryption(plain, paramIV, blockCipher);
            } else {
                cipher = EncryptionService.runEncryption(plain, paramKey, blockCipher);
            }

            bitSetTempCipher = BitSet.valueOf(cipher);
            bitSetTempCipher.xor(bitSetBaseCipher);
            
            hammingDistance = getHammingDistance(bitSetTempCipher);
            awd_result[hammingDistance]++;
        }
    }

    @Override
    public String getResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    private int getHammingDistance(BitSet bitset) {
        int result = 0;
        for(int i = 0; i < bitset.size(); i++) {
            if(bitset.get(i))
                result++;
        }
        
        return result;
    }
}

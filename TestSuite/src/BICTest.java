
import common.EncryptionService;
import common.CommonService;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
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
public class BICTest implements UnitTestInterface {
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
    
    private Data[][] arrayOfRawData;
    private double[][] arrOfCoeffData;
    double maxCoef = 0.0;
    
    public BICTest(BufferedBlockCipher blockCipher) {
        this.blockCipher = blockCipher;
        this.blockSize = 128;
        this.numOfRound = 16;
        this.key = CommonService.getBytesFromHex(CommonService.generateRandomHexText(blockSize/4));
        this.paramKey = new KeyParameter(key); 
        this.isIV = false;
        arrOfCoeffData = new double[blockSize][blockSize];
        arrayOfRawData = new Data[blockSize][blockSize];
    }
    
    public BICTest(BufferedBlockCipher blockCipher, byte[] key, int numOfRound, int blockSize) {
        this.blockCipher = blockCipher;
        this.key = key;
        this.numOfRound = numOfRound;
        this.blockSize = blockSize;
        this.paramKey = new KeyParameter(key);
        this.isIV = false;
        arrOfCoeffData = new double[blockSize][blockSize];
        arrayOfRawData = new Data[blockSize][blockSize];
    }
    
    public BICTest(BufferedBlockCipher blockCipher, byte[] key, byte[] iv, int numOfRound, int blockSize) {
        this.blockCipher = blockCipher;
        this.key = key;
        this.iv = iv;
        this.numOfRound = numOfRound;
        this.blockSize = blockSize;
        this.paramKey = new KeyParameter(key);
        this.paramIV = new ParametersWithIV(paramKey, iv);
        this.isIV = true;
        arrOfCoeffData = new double[blockSize][blockSize];
        arrayOfRawData = new Data[blockSize][blockSize];
    }
    
    private double getAverage(Integer[] input) {
        int sum = 0;
        for(int i = 0; i < input.length; i++) {
            sum += input[i];
        }
        
        return (double) sum / input.length;
    }
    
    private double getCorrelationCoefficient(Integer[] x, Integer[] y) {
        double avgX = getAverage(x);
        double avgY = getAverage(y);
        
        if(x.length != y.length)
            return 0.0;
        
        int index = x.length;
        double covX = 0;
        double covY = 0;
        double diffXY = 0;
        for(int i = 0; i < index; i++) {
            double tempX = x[i] - avgX;
            double tempY = y[i] - avgY;
            covX += Math.pow(tempX, 2);
            covY += Math.pow(tempY, 2);
            diffXY += (tempX * tempY);
        }
        
        double divider = Math.sqrt(covX) * Math.sqrt(covY);
        return diffXY / divider;
    }

    @Override
    public void runTest() throws Exception {
        String plainStr;
        for(int round = 0; round < numOfRound; round++) {
            plainStr = CommonService.generateRandomHexText(blockSize/4);
            bitSetBasePlain = CommonService.getBitSetFromHex(plainStr);
            plain = CommonService.bitsetToByte(bitSetBasePlain);
            
            if(isIV) {
                cipher = EncryptionService.runEncryption(plain, paramIV, blockCipher);
            } else {
                cipher = EncryptionService.runEncryption(plain, paramKey, blockCipher);
            }
            
            bitSetBaseCipher = BitSet.valueOf(cipher);
            
            for(int idx = 0; idx < blockSize; idx++) {
                bitSetTempPlain = BitSet.valueOf(CommonService.bitsetToByte(bitSetBasePlain));
                bitSetTempPlain.flip(idx);
                
                plain = CommonService.bitsetToByte(bitSetTempPlain);
                
                if(isIV) {
                cipher = EncryptionService.runEncryption(plain, paramIV, blockCipher);
                } else {
                    cipher = EncryptionService.runEncryption(plain, paramKey, blockCipher);
                }
                
                bitSetTempCipher = BitSet.valueOf(cipher);
                bitSetTempCipher.xor(bitSetBaseCipher);
                
                for(int k = 0; k < blockSize; k++) {
                    for(int l = 0; l < blockSize; l++) {
                        if(k != l) {
                            int x = bitSetTempCipher.get(k) ? 1 : 0;
                            int y = bitSetTempCipher.get(l) ? 1 : 0;

                            if(arrayOfRawData[k][l] == null)
                                arrayOfRawData[k][l] = new Data();

                            arrayOfRawData[k][l].add(x, y);
                        }
                    }
                }
            }
        }
        
        for(int i = 0; i < blockSize; i++) {
            for(int j = 0; j < blockSize; j++) {
                if(i != j) {
                    Integer[] xData = arrayOfRawData[i][j].getXData();
                    Integer[] yData = arrayOfRawData[i][j].getYData();

                    double coeficient = getCorrelationCoefficient(xData, yData);
                    arrOfCoeffData[i][j] = coeficient * coeficient;
                    if(arrOfCoeffData[i][j] > maxCoef) {
                        maxCoef = arrOfCoeffData[i][j];
                    }
                }
            }
        }
    }

    @Override
    public String getResult() {
        return maxCoef + "";
    }
    
    private class Data {
        List<Integer> xData = new ArrayList<Integer>();
        List<Integer> yData = new ArrayList<Integer>();
        
        public void add(int x, int y) {
            xData.add(x);
            yData.add(y);
        }
        
        public Integer[] getXData() {
            return xData.toArray(new Integer[xData.size()]);
        }
        
        public Integer[] getYData() {
            return yData.toArray(new Integer[yData.size()]);
        }
        
        @Override
        public String toString() {
            String result = "";
            for(int i = 0; i < xData.size(); i++) {
                result += ("" + xData.get(i) + "|" + yData.get(i) + "\n");
            }
            return result;
        }
    }
    
    public static void main(String ar[]) throws Exception {
        BlockCipher engine = new AESEngine();
	BufferedBlockCipher cipher = new BufferedBlockCipher(new CBCBlockCipher(engine));
        BICTest bic = new BICTest(cipher);
        bic.runTest();
        System.out.println(bic.getResult());
    }
}

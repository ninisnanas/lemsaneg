/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bic;

import blockcipher.BlockCipherFactory;
import blockcipher.CipherType;
import java.util.ArrayList;
import javax.xml.bind.DatatypeConverter;
import java.util.BitSet;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class BIC {
    private int numOfSample;
    private BitSet randomInput;
    private BitSet baseOutput;
    private BitSet flipInput;
    private BitSet xorResult;
    private BlockCipherFactory cipherFactory;
    private int N = 16;
    private byte[] plainText;
    
    private Data[][] arrayOfRawData = new Data[N][N];
    private double[][] arrOfCoeffData = new double[N][N];
    
    public BIC(int numOfSample, String keyStr) {
        this.numOfSample = numOfSample;
        byte[] key_byte = toByteArray(transformFromHex(keyStr));
        cipherFactory = new BlockCipherFactory(key_byte);
    }
    
    public BIC() {
        this(128, "0123456789ABCDEFFEDCBA9876543210");
    }
    
    public double runTest() {
        String inputStr;
        for(int i = 0; i < numOfSample; i++) {
            System.out.println(i);
            inputStr = generateRandomInput();
            randomInput = transformFromHex(inputStr);
            
            plainText = toByteArray(randomInput);
            baseOutput = BitSet.valueOf(cipherFactory.getEncrypt(plainText, CipherType.AES));
            
            for(int j = 0; j < N; j++) {
                flipInput = BitSet.valueOf(toByteArray(randomInput));
                flipInput.flip(j);

                plainText = toByteArray(flipInput);
                xorResult = BitSet.valueOf(cipherFactory.getEncrypt(plainText, CipherType.AES));
                xorResult.xor(baseOutput);
                
                for(int k = 0; k < N; k++) {
                    for(int l = 0; l < N; l++) {
                        if(k != l) {
                            int x = xorResult.get(k) ? 1 : 0;
                            int y = xorResult.get(l) ? 1 : 0;

                            if(arrayOfRawData[k][l] == null)
                                arrayOfRawData[k][l] = new Data();

                            arrayOfRawData[k][l].add(x, y);
                        }
                    }
                }
           }
        }
        
        double maxCoef = 0.0;
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
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
        
        return maxCoef;
    }
    
    public void printArray(Integer[] arr) {
        for(Integer i : arr) {
            System.out.print("" + i + " ");
        }
        System.out.println();
    }
    
    public static void main(String ar[]) {
        BIC bic = new BIC();
        double result = bic.runTest();
        
        System.out.println(result);
    }
    
    private String generateRandomInput() {
        String input = "";
        
        for(int i = 0; i < 32; i++) {
            int base = (int) (Math.random() * 15);
            switch(base) {
                case 10:
                    input += 'A';
                    break;
                case 11:
                    input += 'B';
                    break;
                case 12:
                    input += 'C';
                    break;
                case 13:
                    input += 'D';
                    break;
                case 14:
                    input += 'E';
                    break;
                case 15:
                    input += 'F';
                    break;
                default:
                    input += base;
                    break;
            }
        }
        return input;
    }
    
    public double getAverage(Integer[] input) {
        int sum = 0;
        for(int i = 0; i < input.length; i++) {
            sum += input[i];
        }
        
        return (double) sum / input.length;
    }
    
    public double getCorrelationCoefficient(Integer[] x, Integer[] y) {
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
    
    //TODO move this to common?
    private BitSet transformFromHex(String hex) {
        return BitSet.valueOf(transformFromHexToByte(hex));
    }
    
    private byte[] transformFromHexToByte(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }
    
    private String transformFromBitSet(java.util.BitSet bs) {
        return Long.toHexString(bs.toLongArray()[0]);
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
    
    public static byte[] toByteArray(BitSet bits) {
        byte[] bytes = new byte[(bits.size() + 7) / 8];
        for (int i=0; i<bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
            }
        }
        return bytes;
    }
}

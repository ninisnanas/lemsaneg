/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author ASUS
 */
public class ByteOperation {

    public static byte[] operationAnd(byte[] a, byte[] b) {
        byte[] result = new byte[Math.max(a.length, b.length)];
        
        int maxIndex = Math.min(a.length, b.length);
        for(int i = 0; i < maxIndex; i++) {
            result[i] = (byte) (a[i] & b[i]);
        }
        
        return result;
    }
    
    public static byte[] operationOr(byte[] a, byte[] b) {
        byte[] result = new byte[Math.max(a.length, b.length)];
        
        int maxIndex = Math.min(a.length, b.length);
        for(int i = 0; i < maxIndex; i++) {
            result[i] = (byte) (a[i] | b[i]);
        }
        
        if(a.length > maxIndex) {
            for(int i = maxIndex; i < a.length; i++) {
                result[i] = a[i];
            }
        } else if(b.length > maxIndex){
            for(int i = maxIndex; i < b.length; i++) {
                result[i] = b[i];
            }
        }
        
        return result;
    }
    
    public static byte[] operationXor(byte[] a, byte[] b) {
        byte[] result = new byte[Math.max(a.length, b.length)];
        
        int maxIndex = Math.min(a.length, b.length);
        for(int i = 0; i < maxIndex; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        
        if(a.length > maxIndex) {
            for(int i = maxIndex; i < a.length; i++) {
                result[i] = a[i];
            }
        } else if(b.length > maxIndex){
            for(int i = maxIndex; i < b.length; i++) {
                result[i] = b[i];
            }
        }
        return result;
    }
    
    public static byte[] operationShiftRight(byte[] a, int numOfShift) {
        return null;
    }
    
    public static byte[] operationShiftLeft(byte[] a, int numOfShift) {
        return null;
    }
    
    public static byte[] operationNot(byte[] a) {
        return null;
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
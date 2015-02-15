/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author ASUS
 */
public class ByteOperationTest {
    
    public static void main(String ar[]) {
        byte[] dataA = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        byte[] dataB = {0x11, 0x12, 0x13, 0x14, 0x15};
        
        byte[] resultAnd = ByteOperation.operationAnd(dataA, dataB);
        byte[] resultOr = ByteOperation.operationOr(dataA, dataB);
        byte[] resultXor = ByteOperation.operationXor(dataA, dataB);
        for(byte result : resultXor) {
            System.out.println(result);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.Random;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.util.encoders.Hex;

/**
 * this class contains some generic functions related for test suite, bit
 * operation etc
 *
 * @author ASUS
 */
public class GeneralService {

    /**
     * get array of byte from specific bit set
     *
     * @param bitset
     * @return
     */
    public static byte[] getBytesFromBitSet(BitSet bitset) {
        byte[] bytes = new byte[(bitset.size() + 7) / 8];
        for (int i = 0; i < bitset.length(); i++) {
            if (bitset.get(i)) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }
        return bytes;
    }

    /**
     * get bit set object from specific hex
     *
     * @param hex
     * @return
     */
    public static BitSet getBitSetFromHex(String hex) {
        return BitSet.valueOf(getBytesFromHex(hex));
    }

    /**
     * get array of byte from specific hex string
     *
     * @param hex
     * @return
     */
    public static byte[] getBytesFromHex(String hex) {
        return Hex.decode(hex);
        //return DatatypeConverter.parseHexBinary(hex);
    }

    /**
     * get hex string from specific bit set
     *
     * @param bitset
     * @return
     */
    public static String getHexFromBitSet(BitSet bitset) {
        return Long.toHexString(bitset.toLongArray()[0]);
    }

    /**
     * generate random hex text with specific size
     *
     * @param size
     * @return
     */
    public static String generateRandomHexText(int size) {
        String result = "";
        for (int i = 0; i < size; i++) {
            int base = (int) (Math.random() * 15);
            switch (base) {
                case 10:
                    result += "A";
                    break;
                case 11:
                    result += "B";
                    break;
                case 12:
                    result += "C";
                    break;
                case 13:
                    result += "D";
                    break;
                case 14:
                    result += "E";
                    break;
                case 15:
                    result += "F";
                    break;
                default:
                    result += base;
                    break;
            }
        }

        return result;
    }

    public static byte[] generateZeroInput(int jumlahByte) {
        byte[] hasil = new byte[jumlahByte];

        for (int i = 0; i < jumlahByte; i++) {
            hasil[i] = 0;
        }

        return hasil;
    }

    public static byte[] generateRandomInput(int jumlahByte) {
        byte[] hasil = new byte[jumlahByte];
        Random rand = new Random();
        rand.nextBytes(hasil);

        return hasil;
    }

    public static byte[] generateRandomSBox(int m, int n) {
        int ukuranSbox = (int) Math.pow(2, m);
        int ukuranIsi = (int) Math.pow(2, n);
        byte[] sbox = new byte[ukuranSbox];
        Random rand = new Random();

        for (int i = 0; i < ukuranSbox; i++) {
            sbox[i] = (byte) rand.nextInt(ukuranIsi);
        }

        return sbox;
    }

    public static byte[] hexToByte(String hex) {
        return Hex.decode(hex);
    }

    public static String byteToHex(byte[] bytes) {
        return Hex.toHexString(bytes);
    }

    public static int[] copyByteToInt(byte[] arr) {
        int[] hasil = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            hasil[i] = arr[i] & 0xff;
        }
        return hasil;
    }

    public static void writeToCSV(int[] arr, String location) throws FileNotFoundException {
        File file = new File(location);
        PrintWriter pw = new PrintWriter(file);
        for (int i = 0; i < arr.length; i++) {
            pw.print(arr[i] + ",");
        }
        pw.flush();
        pw.close();
    }

    public static void writeToCSV(int[][] arr, String location) throws FileNotFoundException {
        File file = new File(location);
        PrintWriter pw = new PrintWriter(file);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                pw.print(arr[i][j] + ",");
            }
        }
        pw.flush();
        pw.close();
    }
}

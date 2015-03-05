/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Random;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.util.encoders.Hex;

/**
 * this class contains some generic functions related for test suite, bit
 * operation etc
 *
 * @author ASUS
 */
public class CommonService {

    /**
     * get array of byte from specific bit set
     *
     * @param bitset
     * @return
     */
    public static byte[] bitsetToByte(BitSet b) {
        byte[] hasil = new byte[(b.size() + 7) / 8];
        for (int i = 0; i < b.length(); i++) {
            if (b.get(i)) {
                hasil[hasil.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }
        return hasil;
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

    public static byte[] generateInput(int jumlahByte, byte nilai) {
        // Menginisialisasi variabel hasil
        byte[] hasil = new byte[jumlahByte];

        // Loop untuk mengisi hasil
        for (int i = 0; i < jumlahByte; i++) {
            hasil[i] = nilai;
        }

        // Mengembalikan hasil
        return hasil;
    }

    public static byte[] generateRandomInput(int jumlahByte) {
        // Menginisialisasi variabel hasil
        byte[] hasil = new byte[jumlahByte];

        // Mengisi hasil array
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(hasil);

        // Mengembalikan hasil
        return hasil;
    }

    // Metode 1
    public static byte[] generateSBox(int m, int n) {
        // Kemungkinan input dan output
        int kemungkinanInput = (int) Math.pow(2, m);
        int kemungkinanOutput = (int) Math.pow(2, n);
        if (kemungkinanOutput > kemungkinanInput) {
            byte[] hasil = new byte[kemungkinanInput];
            // Membuat array list Byte dan mengisinya berurut dari 0 sampai
            // kemungkinan input % kemungkinan output
            ArrayList<Byte> sboxList = new ArrayList<Byte>();
            for (int i = 0; i < kemungkinanOutput; i++) {
                sboxList.add((byte) (i % kemungkinanOutput));
            }

            // Menshuffle sbox
            Collections.shuffle(sboxList, new Random(new Random().nextLong()));

            // Kopi ke array of byte
            Byte[] hasilArr = sboxList.toArray(new Byte[kemungkinanInput]);
            for (int i = 0; i < kemungkinanInput; i++) {
                hasil[i] = hasilArr[i];
            }

            return hasil;
        } else {
            byte[] hasil = new byte[kemungkinanInput];
            // Membuat array list Byte dan mengisinya berurut dari 0 sampai
            // kemungkinan input % kemungkinan output
            ArrayList<Byte> sboxList = new ArrayList<Byte>();
            for (int i = 0; i < kemungkinanInput; i++) {
                sboxList.add((byte) (i % kemungkinanOutput));
            }

            // Menshuffle sbox
            Collections.shuffle(sboxList, new Random(new Random().nextLong()));

            // Kopi ke array of byte
            Byte[] hasilArr = sboxList.toArray(new Byte[sboxList.size()]);
            for (int i = 0; i < kemungkinanInput; i++) {
                hasil[i] = hasilArr[i];
            }

            return hasil;
        }
    }

    // Metode 2
    public static int[] generateSBoxInt(int m, int n) {
        // Kemungkinan input dan output
        int kemungkinanInput = (int) Math.pow(2, m);
        int kemungkinanOutput = (int) Math.pow(2, n);
        if (kemungkinanInput > kemungkinanOutput) {
            int[] hasil = new int[kemungkinanInput];
            // Membuat array list Byte dan mengisinya berurut dari 0 sampai
            // kemungkinan input % kemungkinan output
            ArrayList<Integer> sboxList = new ArrayList<Integer>();
            for (int i = 0; i < kemungkinanInput; i++) {
                sboxList.add(i % kemungkinanOutput);
            }

            // Menshuffle sbox
            Collections.shuffle(sboxList, new Random(new Random().nextLong()));

            // Kopi ke array of byte
            Integer[] hasilArr = sboxList.toArray(new Integer[sboxList.size()]);
            for (int i = 0; i < kemungkinanInput; i++) {
                hasil[i] = hasilArr[i];
            }

            return hasil;
        } else {
            ArrayList<Integer> hasil = new ArrayList<Integer>();
            Random rand = new Random();
            for (int i = 0; i < kemungkinanInput; i++) {
                int r = rand.nextInt(kemungkinanOutput);
                while (hasil.contains(r)) {
                    r = rand.nextInt(kemungkinanOutput);
                }
                hasil.add(r);
            }

            Integer[] hasilArr = hasil.toArray(new Integer[hasil.size()]);
            int[] sbox = new int[kemungkinanInput];
            for (int i = 0; i < kemungkinanInput; i++) {
                sbox[i] = hasilArr[i];
            }
            return sbox;
        }
    }

    public static String byteArrayToHexa(byte[] arr) {
        return Hex.toHexString(arr);
    }

    public static String byteToHexa(byte b) {
        byte[] arr = {b};
        return Hex.toHexString(arr);
    }

    public static byte[] hexaToByteArray(String str) {
        return Hex.decode(str);
    }

    public static int[] byteToInt(byte[] arr) {
        int[] hasil = new int[arr.length];
        for (int i = 0; i < hasil.length; i++) {
            hasil[i] = arr[i] & 0xff;

        }
        return hasil;
    }

    public static byte[] intArrToByteArr(int[] arr) {
        byte[] hasil = new byte[arr.length * 4];
        int j = 0;
        for (int i = 0; i < hasil.length; i = i + 4) {
            hasil[i] = (byte) (arr[j] & 0xff);
            hasil[i + 1] = (byte) (arr[j] >> 8 & 0xff);
            hasil[i + 2] = (byte) (arr[j] >> 16 & 0xff);
            hasil[i + 3] = (byte) (arr[j] >> 24 & 0xff);
            j++;
        }
        return hasil;
    }

    public static byte[] intToByte(int arr) {
        byte[] hasil = new byte[4];
        hasil[0] = (byte) (arr & 0xff);
        hasil[1] = (byte) (arr >> 8 & 0xff);
        hasil[2] = (byte) (arr >> 16 & 0xff);
        hasil[3] = (byte) (arr >> 24 & 0xff);

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
            for (int j = 0; j < arr[i].length; j++) {
                pw.print(arr[i][j] + ",");
            }
            pw.println();
        }
        pw.flush();
        pw.close();
    }
}

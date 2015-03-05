
import common.CommonService;
import java.awt.BorderLayout;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Remmy
 */
public class XORTableTest {

    private int[] sbox;
    private int[][] xorTable;
    private int m;
    private int n;

    public XORTableTest(byte[] sbox, int m, int n) {
        this.sbox = CommonService.byteToInt(sbox);
        this.m = m;
        this.n = n;
        int ukuranSbox = (int) Math.pow(2, m);
        int ukuranIsi = (int) Math.pow(2, n);
        xorTable = new int[ukuranSbox][ukuranIsi];
    }

    public XORTableTest(int[] sbox) {
        byte[] b = CommonService.intArrToByteArr(sbox);

        this.sbox = CommonService.byteToInt(b);
        int ukuranSbox = sbox.length;

        int ukuranIsi = findMax(this.sbox);
        xorTable = new int[ukuranSbox][16];
        System.out.println(ukuranSbox);
        System.out.println(ukuranIsi);
    }

    public XORTableTest(byte[] sbox) {

        this.sbox = CommonService.byteToInt(sbox);
        int ukuranSbox = sbox.length;

        int ukuranIsi = findMax(this.sbox) + 1;
        xorTable = new int[ukuranSbox][ukuranIsi];
    }

    private int findMax(int[] sbox2) {
        int max = sbox2[0];
        for (int i = 1; i < sbox2.length; i++) {
            if (max < sbox2[i]) {
                max = sbox2[i];
            }
        }
        return max;
    }

    public void test() {
        for (int i = 0; i < sbox.length; i++) {
            for (int j = 0; j < sbox.length; j++) {
                xorTable[i ^ j][sbox[i] ^ sbox[j]]++;
            }
        }

    }

    public void printTable() {
        for (int i = 0; i < xorTable.length; i++) {
            for (int j = 0; j < xorTable[i].length; j++) {
                System.out.printf("%2d ", xorTable[i][j]);
            }
            System.out.printf("\n");
        }
    }

    public int[][] getXorTable() {
        return xorTable;
    }

}

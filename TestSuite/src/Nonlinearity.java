/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
public class Nonlinearity {
    private boolean[][] booleanFunctions;
    private int blockSize;
    private int[][] unexpectedDistance;
    private boolean[][] tempBooleanFunction;
    private int[][] hadamardMatrix;
    private int[] result;
    
    public Nonlinearity(boolean[][] booleanFunctions, int blockSize) {
        this.booleanFunctions = booleanFunctions;
        this.blockSize = blockSize;
        this.result = new int[booleanFunctions.length];
    }
    
    public void run() {
        hadamardMatrix = generateHadamard(blockSize);
        int index = (int) Math.pow(2, blockSize);
        int max = 0;
        for(int i = 0; i < booleanFunctions.length; i++) {
            tempBooleanFunction = rotateMatrix(booleanFunctions[i]);
            unexpectedDistance = multiplyMatrix(hadamardMatrix, tempBooleanFunction);
//            printMatrix(unexpectedDistance);
            int ud = findSecondMax(unexpectedDistance);
            if(ud > max) {
                max = ud;
            }
        }
        System.out.println(index - max);
    }
    
    private int findSecondMax(int[][] data) {
        int max = 0;
        int secondMax = 0;
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[i].length; j++) {
                if(data[i][j] > max) {
                    secondMax = max;
                    max = data[i][j];
                } else if(data[i][j] > secondMax) {
                    secondMax = data[i][j];
                }
            }
        }
        
        return secondMax;
    }
    
    private void printMatrix(int[][] data) {
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                System.out.print(data[i][j]);
            }
            System.out.println();
        }
    }
    
    private int[][] generateHadamard(int n) {
        int size = n;
        int[][] hadamard = new int[size][size];
        hadamard[0][0] = 1;
        
        for(int i = 1; i < size; i+=i) {
            for(int j = 0; j < i; j++) {
                for(int k = 0; k < i; k++) {
                    hadamard[j][k+i] = hadamard[j][k];
                    hadamard[j+i][k] = hadamard[j][k];
                    hadamard[j+i][k+i] = -1 * hadamard[j][k];
                }
            }
        }
        
        return hadamard;
    }
    
    private boolean[][] rotateMatrix(boolean[] data) {
        boolean[][] result = new boolean[data.length][1];
        for(int i = 0; i < data.length; i++) {
            result[i][0] = data[i];
        }
        
        return result;
    }
    
    private int[][] multiplyMatrix(
            int[][] x, boolean[][] y) {
        int rowsX = x.length;
        int colsX = x[0].length;
        int colsY = y[0].length;
        int data;
        
        int[][] result = new int[rowsX][colsY];
        for(int i = 0; i < rowsX; i++) {
            for(int j = 0; j < colsY; j++) {
                for(int k = 0; k < colsX; k++) {
                    data = y[k][j] ? 1 : 0;
                    result[i][j] = result[i][j] + (x[i][k] * data);
                }
            }
        }
        return result;
    }
    
    public static void main(String ar[]) {
        boolean[][] data = new boolean[1][4];
        data[0] = new boolean[] {true, false, false, true, true, true, false, false};
        Nonlinearity nl = new Nonlinearity(data, 8);
        nl.run();
    }
}

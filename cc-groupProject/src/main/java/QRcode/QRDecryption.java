package QRcode;

public class QRDecryption {
    /** The 2-D QR code matrix */
    private boolean[][] orgMatrix;
    private boolean[][] matrix;
    private int[] matrixBytes;

    /** The 2-D logistic map. */
    private int[] logisticMap;

    /** Store the input string. */
    private String input;
    private String decodedstr;

    /** The size of the matrix, determined by the input length. */
    private int N;

    /** The size of the logistic map, determined by the matrix size. */
    private int MAP_N;

    /** The version1  QR matrix. */
    private static final int VERSION1 = 21;
    private static final int PAYLOAD_Len1 = 224;
    
    /** The version2  QR matrix. */
    private static final int VERSION2 = 25;
    private static final int PAYLOAD_Len2 = 370;
    
    /** Zigzap movement */
    private static final int UP = -1;
    private static final int DOWN = 1;
    
    int[][] positionCenters;
    
    
    public void decode(String input) {
        
    }
    
    public void extractMatrix() {
        
    }
    
    public void getFromColumn(int startX, int startY, 
            int move, StringBuilder binaryStr, int expetcedlen) {
        
        int curX = startX;
        int curY = startY;
        
        while (binaryStr.length() < expetcedlen) {
            if (matrix[curX][curY] = true) {
                binaryStr.append('1');
            } else {
                binaryStr.append('0');
            }
            
            if (matrix[curX][curY-1] = true) {
                binaryStr.append('1');
            } else {
                binaryStr.append('0');
            }
            curX += move;
        }
    }
 
    
    public String decodeBinary(String str){
        StringBuilder decodestr = new StringBuilder();
        int len = binaryStrToInt(str.substring(0, 8));
        int curptr = 8;
        for (int i = 0; i<len; i++) {
            int strlen = binaryStrToInt(str.substring(curptr, curptr + 8));
            curptr += 8;
            for (int j = 0; j<strlen; j++) {
                int chASCII = binaryStrToInt(str.substring(curptr, curptr + 8));
                decodestr.append((char) chASCII);
                curptr += 8;
            }
        }
        
        return decodestr.toString();
    }
    
    
    public void zigzagDecode() {
        String zigzagstr;
        
        if (N == VERSION1) {
            zigzagstr = zigzagVersion1();
        } else {
            zigzagstr = zigzagVersion2();
        }
        
        decodedstr = decodeBinary(zigzagstr);
    }
    
    
    public void getMarix() {

        int min_x = Math.min(positionCenters[0][0], Math.min(positionCenters[1][0], positionCenters[2][0]));
        int min_y = Math.min(positionCenters[0][1], Math.min(positionCenters[1][1], positionCenters[2][1]));
        int max_x = Math.max(positionCenters[0][0], Math.max(positionCenters[1][0], positionCenters[2][0]));
        int max_y = Math.max(positionCenters[0][1], Math.max(positionCenters[1][1], positionCenters[2][1]));
        
        int remain_x = positionCenters[0][0] ^ positionCenters[1][0] ^ positionCenters[2][0];
        int remain_y = positionCenters[0][1] ^ positionCenters[1][1] ^ positionCenters[2][1];
        
        if (max_x - min_x + 1 == VERSION1) {
            N = VERSION1;
            matrix = new boolean[VERSION1][VERSION1];
        } else {
            N = VERSION2;
            matrix = new boolean[VERSION2][VERSION2];
        }
        
        if (remain_x == positionCenters[0][0]) {
            if (remain_y == min_y) {
                rotateMatrix180(min_x, min_y);
            } else {
                rotateMatrix90((min_x, min_y);
            }
        } else {
            if (remain_y == min_y) {
                rotateMatrix270((min_x, min_y);
            } 
        }
    }
    
    public String  zigzagVersion1() {
        StringBuilder binaryStr = new StringBuilder();
        
        getFromColumn(20, 20, UP, binaryStr, 26);
        getFromColumn(8, 18, DOWN, binaryStr, 52);
        getFromColumn(20, 16, UP, binaryStr, 78);
        getFromColumn(8, 14, DOWN, binaryStr, 104);
        getFromColumn(20, 12, UP, binaryStr, 132);
        getFromColumn(5, 12, UP, binaryStr, 144);
        getFromColumn(0, 10, DOWN, binaryStr, 156);
        getFromColumn(7, 10, DOWN, binaryStr, 184);
        getFromColumn(12, 8, UP, binaryStr, 194);
        getFromColumn(8, 5, DOWN, binaryStr, 204);
        getFromColumn(12, 3, UP, binaryStr, 214);
        getFromColumn(8, 1, DOWN, binaryStr, 224);
        
        return binaryStr.toString();
    }
    
    
    public String  zigzagVersion2() {
        StringBuilder binaryStr = new StringBuilder();
        
        getFromColumn(24, 24, UP, binaryStr, 34);
        getFromColumn(8, 22, DOWN, binaryStr, 68);
        getFromColumn(24, 20, UP, binaryStr, 76);
        getFromColumn(15, 20, UP, binaryStr, 92);
        getFromColumn(8, 18, DOWN, binaryStr, 108);
        getFromColumn(21, 18, DOWN, binaryStr, 116);
        getFromColumn(24, 16, UP, binaryStr, 124);
        getFromColumn(15, 16, UP, binaryStr, 142);
        getFromColumn(5, 16, UP, binaryStr, 154);
        getFromColumn(0, 14, DOWN, binaryStr, 166);
        getFromColumn(7, 14, DOWN, binaryStr, 202);
        getFromColumn(24, 12, UP, binaryStr, 238);
        getFromColumn(5, 12, UP, binaryStr, 250);
        getFromColumn(0, 10, DOWN, binaryStr, 262);
        getFromColumn(7, 10, DOWN, binaryStr, 298);
        getFromColumn(16, 8, UP, binaryStr, 316);
        getFromColumn(8, 5, DOWN, binaryStr, 334);
        getFromColumn(16, 3, UP, binaryStr, 352);
        getFromColumn(8, 1, DOWN, binaryStr, 370);
        
        return binaryStr.toString();
    }
    
    public int binaryStrToInt(String binarystr) {
        return Integer.parseInt(binarystr, 2);
    } 
}

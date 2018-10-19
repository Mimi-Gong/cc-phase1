package QRcode;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class QREncryption {

    /** The 2-D QR code matrix */
    private boolean[][] matrix;
    private int[] matrixBytes;

    /** The 2-D logistic map. */
    private int[] logisticMap;

    /** Store the input string. */
    private String input;

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
    
    
    /**
     * Constructor.
     * 
     * @param input input string
     * @throws Exception exception
     */
    public QREncryption(String input) throws Exception {
        if (input.length() <= 14) {
            N = VERSION1;
            matrix = new boolean[VERSION1][VERSION1];
        } else if (input.length() <= 23) {
            N = VERSION2;
            matrix = new boolean[VERSION2][VERSION2];
        } else {
            throw new Exception("Invalid input!");
        }

        MAP_N = N * N / 8 + 1;
        logisticMap = new int[MAP_N];
        this.input = input;
    }


    /**
     * Generate the QR code. This is the main API of the class.
     */
    public String generateQRCode() {
        addPositionPattern();
        addTimingPattern();
        addAlignmentPattern();
        fillPayload();

        MatrixToBytes();
        encode();
        String res = getResStr();
        System.out.println(res);
        return res;
    }

 
    /**
     * Add position patterns to the matrix.
     */
    public void addPositionPattern() {
        // Add left-up.
        addPos(3, 3);
        for (int i = 0; i <= 7; ++i) {
            matrix[i][7] = false;
        }
        for (int j = 0; j <= 6; ++j) {
            matrix[7][j] = false;
        }
        
        // Add right-up.
        addPos(3, N - 4);
        for (int i = 0; i <= 7; ++i) {
            matrix[i][N - 8] = false;
        }
        for (int j = N - 7; j <= N - 1; ++j) {
            matrix[7][j] = false;
        }

        // Add left-down.
        addPos(N - 4, 3);
        for (int i = N - 8; i <= N - 1; ++i) {
            matrix[i][7] = false;
        }
        for (int j = 0; j <= 6; ++j) {
            matrix[N - 8][j] = false;
        }
    }


    /**
     * Add one position pattern to the matrix.
     * 
     * @param centralX the x-axis value of central point
     * @param centralY the y-axis value of central point
     */
    private void addPos(int centralX, int centralY) {
        for (int i = centralX - 3; i <= centralX + 3; ++i) {
            for (int j = centralY - 3; j <= centralY + 3; ++j) {
                if ((i == centralX - 2 || i == centralX + 2) &&
                    (j != centralY - 3 && j != centralY + 3)) {
                    matrix[i][j] = false;
                } else if((j == centralY - 2 || j == centralY + 2) &&
                          (i != centralX - 3 && i != centralX + 3)) {
                    matrix[i][j] = false;
                } else {
                    matrix[i][j] = true;
                }
            }
        }
    }


    /**
     * Add timing patterns to the matrix.
     */
    public void addTimingPattern() {
        addTiming(6, 8, 0, N - 16);
        addTiming(8, 6, 1, N - 16);
    }


    /**
     * Add timing pattern to the matrix.
     * 
     * @param startX the x-axis value of central point
     * @param startY the y-axis value of central point
     * @param dir the direction to fill
     * @param len the length to fill
     */
    private void addTiming(int startX, int startY, int dir, int len) {
        // In x direction
        if (dir == 0) {
            for (int j = 0; j < len; j += 2) {
                matrix[startX][startY + j] = true;
            }

        // In y direction.
        } else {
            for (int i = 0; i < len; i += 2) {
                matrix[startX + i][startY] = true;
            }            
        }
    }


    /**
     * Add alignment pattern to the matrix.
     */
    public void addAlignmentPattern() {
        if (N == VERSION1) {
            return;
        }
        addAlignment(18, 18);
    }
    

    /**
     * Get padding.
     * 
     * @param num the padding number
     * @return padding string
     */
    public String getPaddingBinaryString(int num){
        return String.format("%08d", new BigInteger(Integer.toBinaryString(num)));
    }
    

    /**
     * Fill one column.
     * 
     * @param startX start x pos
     * @param startY start y pos
     * @param endX end x pos
     * @param endY end y pos
     * @param move firection to move
     * @param str string to fill
     */
    private void fillColumn(int startX, int startY,
            int endX, int endY, int move, String str) {
        int curX = startX;
        int curY = startY;
        
        for (int i = 0; i<str.length(); i += 2) {
            if (str.charAt(i) == '1') {
                matrix[curX][curY] = true;
            }
            if (str.charAt(i+1) == '1') {
                matrix[curX][curY-1] = true;
            }
            curX += move;
        }
    }


    /**
     * Fill the payload for version1.
     * 
     * @param playload input string
     */
    public void zigzagVersion1(String playload) {
        fillColumn(20, 20, 8, 19, UP, playload.substring(0,26));
        fillColumn(8, 18, 20, 17, DOWN, playload.substring(26,52));
        fillColumn(20, 16, 8, 15, UP, playload.substring(52,78));
        fillColumn(8, 14, 20, 13, DOWN, playload.substring(78, 104));
        fillColumn(20, 12, 7, 11, UP, playload.substring(104, 132));
        fillColumn(5, 12, 0, 11, UP, playload.substring(132, 144));
        fillColumn(0, 10, 5, 9, DOWN, playload.substring(144, 156));
        fillColumn(7, 10, 20, 9, DOWN, playload.substring(156,184));
        fillColumn(12, 8, 8, 7, UP, playload.substring(184,194));
        fillColumn(8, 5, 12, 4, DOWN, playload.substring(194,204));
        fillColumn(12, 3, 8, 2, UP, playload.substring(204,214));
        fillColumn(8, 1, 12, 0, DOWN, playload.substring(214,224));
    }


    /**
     * Fill the payload for version2.
     * 
     * @param playload input string
     */
    public void zigzagVersion2(String playload) {
        fillColumn(24, 24, 8, 23, UP, playload.substring(0,34));
        fillColumn(8, 22, 24, 21, DOWN, playload.substring(34,68));
        fillColumn(24, 20, 21, 19, UP, playload.substring(68,76));
        fillColumn(15, 20, 8, 19, UP, playload.substring(76,92));
        fillColumn(8, 18, 15, 17, DOWN, playload.substring(92, 108));
        fillColumn(21, 18, 24, 17, DOWN, playload.substring(108, 116));
        fillColumn(24, 16, 21, 15, UP, playload.substring(116, 124));
        fillColumn(15, 16, 7, 15, UP, playload.substring(124, 142));
        fillColumn(5, 16, 0, 15, UP, playload.substring(142, 154));
        fillColumn(0, 14, 5, 13, DOWN, playload.substring(154, 166));
        fillColumn(7, 14, 24, 13, DOWN, playload.substring(166, 202));
        fillColumn(24, 12, 7, 11, UP, playload.substring(202, 238));
        fillColumn(5, 12, 0, 11, UP, playload.substring(238, 250));
        fillColumn(0, 10, 5, 9, DOWN, playload.substring(250,262));
        fillColumn(7, 10, 24, 9, DOWN, playload.substring(262,298));
        fillColumn(16, 8, 8, 7, UP, playload.substring(298,316));
        fillColumn(8, 5, 16, 4, DOWN, playload.substring(316,334));
        fillColumn(16, 3, 8, 2, UP, playload.substring(334,352));
        fillColumn(8, 1, 16, 0, DOWN, playload.substring(352,370));
    }
 

    /**
     * Add the alignment pattern.
     * 
     * @param centralX the x-axis value of central point
     * @param centralY the y-axis value of central point
     */
    private void addAlignment(int centralX, int centralY) {
        // Add center point.
        matrix[centralX][centralY] = true;
 
        // Add the outer circle.
        for (int j = centralY - 2; j <= centralY + 2; ++j) {
            matrix[centralX - 2][j] = true;
        }
        for (int i = centralX - 1; i <= centralX + 2; ++i) {
            matrix[i][centralY + 2] = true;
        }
        for (int j = centralY - 2; j <= centralY + 1; ++j) {
            matrix[centralX + 2][j] = true;
        }
        for (int i = centralX - 1; i <= centralX + 1; ++i) {
            matrix[i][centralY - 2] = true;
        }
     }


    /**
     * Fill the payload to the matrix.
     */
    public void fillPayload() {
        StringBuilder payload = new StringBuilder();
        String inputlen = getPaddingBinaryString(input.length());
        payload.append(inputlen);
        
        for (int i = 0; i<input.length(); i++) {
            String binaryStr = getPaddingBinaryString(input.charAt(i));
            payload.append(binaryStr);
            int checksum = 0;
            for (int j = 0; j<binaryStr.length(); j++) {
                checksum ^= Integer.valueOf(binaryStr.charAt(j));
            }
            String errorcode = getPaddingBinaryString(checksum);
            payload.append(errorcode);
        }
        
        if (N == VERSION1) {
            while (payload.length() < PAYLOAD_Len1) {
                payload.append("1110110000010001");
            }
            zigzagVersion1(payload.toString());
        } else {
            while (payload.length() < PAYLOAD_Len2) {
                payload.append("1110110000010001");
            }
            zigzagVersion2(payload.toString());
        }
    }


    /**
     * Print the 2-D matrix for debug use.
     */
    public void printHelper() {
        System.out.println(N);
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (matrix[i][j] == true) {
                    System.out.print("1");
                } else {
                    System.out.print("0");
                }
            }
            System.out.println();
        }
    }
    

    /**
     * Encode the QRcode to logistic map.
     */
    private void encode() {
        int padding = 32 - N * N % 32;
        int lastBytesCnt = 4;
        if (N == VERSION2) {
            padding = 24 - N * N % 32;
            lastBytesCnt = 3;
        }

        // Initial logistic number.
        double x = 0.1;
        double r = 4.0;
        for (int i = 0; i < MAP_N - lastBytesCnt; i++) {
            int tmpNum = Integer.reverse((int)(x * 255.0)) >>> 24;
            logisticMap[i] = tmpNum ^ matrixBytes[i];
            x = logictic(x, r);
        }

        // Get reverse logistic mask for last 4 bytes.
        int logisNum = 0;
        for (int i = 0; i < lastBytesCnt; ++i) {
            int tmpNum = Integer.reverse((int)(x * 255.0)) >>> 24;
            logisNum = (logisNum << 8) | tmpNum;
            x = logictic(x, r);
        }
        logisNum = logisNum >>> padding;

        
        // Handle the last 4 bytes.
        int mask = 0x000000ff;
        for (int i = lastBytesCnt - 1; i >= 0; --i) {
            int idx = MAP_N - lastBytesCnt + i;
            logisticMap[idx] = matrixBytes[idx] ^ (mask & logisNum);
            logisNum = logisNum >>> 8;
        }
    }

    /**
     * Calculate the logistic value.
     * @param x the value.
     * @param r the coefficient
     * @return the logistic value.
     */
    static double logictic(double x, double r) {
        return r * x * (1.0 - x);
    }


    /**
     * Convert the logistic map to hex string.
     * 
     * @return the hex string
     */
    private String getResStr() {
        // Calculate the padding and last bytes.
        int lastBytesCnt = 4;
        if (N == VERSION2) {
            lastBytesCnt = 3;
        }

        StringBuilder res = new StringBuilder();

        // Handle the prefix.
        byte[] buffer = new byte[4];
        for (int i = 0; i < MAP_N - lastBytesCnt; i++) {
            if (i != 0 && i % 4 == 0) {
                res.append("0x");
                res.append(Integer.toHexString(ByteBuffer.wrap(buffer).getInt()));
            }
            buffer[i % 4] = (byte)logisticMap[i];
        }
        res.append("0x");
        res.append(Integer.toHexString(ByteBuffer.wrap(buffer).getInt()));

        // Handle the last bytes.
        byte[] lastBuffer = new byte[lastBytesCnt];
        for (int i = 0; i <= lastBytesCnt - 1; ++i) {
            int idx = MAP_N - lastBytesCnt + i;
            lastBuffer[i] = (byte)logisticMap[idx];
        }
        res.append("0x");
        res.append(Integer.toHexString(ByteBuffer.wrap(lastBuffer).getInt()));
 
        return res.toString();
    }

    /**
     * Print the result of logistic encoding.
     */
    private void printRes() {
        System.out.println("++++++++++ The encode result +++++++");
        for (int i : logisticMap) {
            System.out.println(" 0x" + Integer.toHexString(i));
        }
    }


    /**
     * Convert matrix to bytes array.
     */
    public void MatrixToBytes() {
        StringBuffer binaryStr = new StringBuffer();
        if (N == VERSION1) {
            matrixBytes = new int[56];
        } else {
            matrixBytes = new int[79];
        }
        
        for (int i = 0; i< matrix.length; i++) {
            for (int j = 0; j<matrix[0].length; j++) {
                if (matrix[i][j]) {
                    binaryStr.append("1");
                } else {
                    binaryStr.append("0");
                }
            }
        }
        
        int cnt = 0;
        int i = 0;
        for (; i + 32<binaryStr.length(); i += 32) {
            for (int j = i; j < i+32; j += 8) {
                int end = Math.min(binaryStr.length(), j+8);
                String binaryByte = binaryStr.substring(j, end).toString();
                matrixBytes[cnt++] = (byte) Integer.parseInt(binaryByte, 2) & 255;
            }
        }
        cnt = matrixBytes.length-1;
        for (int j = binaryStr.length(); j>i; j -= 8) {
            int begin = Math.max(j-8, i);
            String binaryByte = binaryStr.substring(begin, j).toString();
            matrixBytes[cnt--] = (byte) Integer.parseInt(binaryByte, 2) & 255;
        }
    }
    

    public static void main(String[] args) throws Exception {
        QREncryption ins = new QREncryption("CC Team");
        ins.generateQRCode();
    }
}
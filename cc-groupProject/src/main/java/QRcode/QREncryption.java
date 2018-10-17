package QRcode;

import java.math.BigInteger;

public class QREncryption {

    /** The 2-D QR code matrix */
    private boolean[][] matrix;

    /** Store the input string. */
    private String input;

    /** The size of the matrix, determined by the input length. */
    private int N;

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

        this.input = input;
    }

    /**
     * Add position pattern to the matrix.
     * 
     * @param centralX the x-axis value of central point
     * @param centralY the y-axis value of central point
     */
    private void addPositionPattern(int centralX, int centralY) {
        return;
    }


    /**
     * Add timing pattern to the matrix.
     * 
     * @param startX the x-axis value of central point
     * @param startY the y-axis value of central point
     * @param dir the direction to fill
     * @param len the length to fill
     */
    private void addTimingPattern(int startX, int startY, int dir, int len) {
        return;
    }


    /**
     * Add alignment pattern to the matrix.
     */
    private void addAlignmentPattern() {
        return;
    }
    
     
    public String getPaddingBinaryString(int num){
        return String.format("%08d", new BigInteger(Integer.toBinaryString(num)));
    }
    
    private void fillColumn(int startX, int startY, int endX, int endY, int move, String str) {
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
    
    public void zigzagVersion1(String playload) {
        fillColumn(20, 20, 8, 19, UP, playload.substring(0,26));
        fillColumn(8, 27, 20, 17, DOWN, playload.substring(26,52));
        fillColumn(20, 16, 8, 15, UP, playload.substring(52,78));
        fillColumn(8, 14, 20, 13, DOWN, playload.substring(78, 104));
        fillColumn(20, 12, 7, 11, UP, playload.substring(104, 132));
        fillColumn(5, 12, 0, 11, DOWN, playload.substring(132, 144));
        fillColumn(0, 10, 5, 9, UP, playload.substring(144, 156));
        fillColumn(7, 10, 20, 9, DOWN, playload.substring(156,184));
        fillColumn(12, 8, 8, 7, UP, playload.substring(184,194));
        fillColumn(8, 6, 12, 4, DOWN, playload.substring(194,204));
        fillColumn(12, 3, 8, 2, UP, playload.substring(204,214));
        fillColumn(8, 1, 12, 0, DOWN, playload.substring(214,224));
    }
    
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
}

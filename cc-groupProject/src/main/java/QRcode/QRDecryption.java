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
    
    
    public void decode(String input) {
        
    }
    
    public void extractMatrix() {
        
    }
    
    public void zigzagDecode() {
        
    }
}

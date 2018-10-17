package QRcode;

public class QREncryption {

    /** The 2-D QR code matrix */
    private boolean[][] matrix;

    /** Store the input string. */
    private String input;

    /** The size of the matrix, determined by the input length. */
    private int N;

    /** The version1  QR matrix. */
    private static final int VERSION1 = 21;

    /** The version2  QR matrix. */
    private static final int VERSION2 = 25;
    
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


    /**
     * Fill the payload to the matrix.
     */
    private void fillPayload() {
        
    }
    
    
    public static void main(String[] args) {
        System.out.println("QR project");
    }
}

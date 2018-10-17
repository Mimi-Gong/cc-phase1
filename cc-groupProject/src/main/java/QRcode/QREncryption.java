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
     * Add position patterns to the matrix.
     */
    private void addPositionPattern() {
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
    private void addTimingPattern() {
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
    private void addAlignmentPattern() {
        if (N == VERSION1) {
            return;
        }
        addAlignment(18, 18);
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
    private void fillPayload() {
        return;
    }

    /**
     * Print the 2-D matrix for debug use.
     */
    private void printHelper() {
        System.out.println(N);
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (matrix[i][j] == true) {
                    System.out.print("1");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        QREncryption ins = new QREncryption("dsFFFFFFFFFFFFFW");
        ins.addPositionPattern();
        ins.addTimingPattern();
        ins.addAlignmentPattern();
        ins.printHelper();
    }
}

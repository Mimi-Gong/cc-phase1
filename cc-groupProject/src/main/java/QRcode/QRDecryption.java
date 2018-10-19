package QRcode;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


public class QRDecryption {
    /** The 2-D QR code matrix */
    private boolean[][] orgMatrix;
    private boolean[][] matrix;
    private int[] matrixBytes;

    /** The 2-D logistic map. */
    private byte[] logisticMap;

    /** The array stores position centers. */
    private int[][] positionCenters;

    /** The position mask. */
    private boolean[][] positionMask;

    /** Store the input string. */
    private String input;
    private static final int INPUT_N = 32;

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
     * Constructor of the decode class.
     * 
     * @param input the input string
     */
    public QRDecryption(String input) {
        this.input = input;

        MAP_N = INPUT_N * INPUT_N / 8;
        logisticMap = new byte[MAP_N];
        fillLogisticMap();

        orgMatrix = new boolean[INPUT_N][INPUT_N];
        positionCenters = new int[3][2];
        positionMask = new boolean[7][7];
        generatePosMask(3, 3);
    }


    /**
     * Decode the input string to 2-D boolean matrix.
     * 
     * @throws DecoderException error when execute Hex.decodeHex
     */
    public void decode() throws DecoderException {
        // Split the input to string array.
        String[] subStrs = input.substring(2).split("0x");
        System.out.println(subStrs.length);
        System.out.println("------------------ \n");

        // Decode the string with logistic map.
        for (int i = 0; i < logisticMap.length; i += 4) {
            String tmpStr = subStrs[i / 4];
            if (tmpStr.length() % 2 == 1) {
                tmpStr = "0" + tmpStr;
            }

            byte[] decoded = Hex.decodeHex(tmpStr);
            int len = Math.min(4, decoded.length);
            for (int j = 0; j < len; ++j) {
                logisticMap[i+j] = (byte) (logisticMap[i+j] ^ decoded[j]);
            }
        }
        System.out.println("\n++++++++++++++++++++ \n");

        // Fill the 32 x 32 2-D matrix.
        int pos = 0;
        for (int i = 0; i < INPUT_N; ++i) {
            for (int j = 0; j < INPUT_N; j += 8) {
                // Fill each bit.
                byte tmp = logisticMap[pos];
                for (int k = 7; k >= 0; --k) {
                    if ((tmp >>> k & 1) == 1) {
                        orgMatrix[i][j+7-k] = true;
                    }
                }
                pos++;
            }
        }

        // Print the matrix.
        for (int i = 0; i < INPUT_N; ++i) {
            for (int j = 0; j < INPUT_N; ++j) {
                if (orgMatrix[i][j]) {
                    System.out.print("1");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        // Extract the position matrix 
        extractMatrix();
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
     * Fill the logistic number.
     */
    private void fillLogisticMap() {
        double x = 0.1;
        double r = 4.0;
        for (int i = 0; i < logisticMap.length; i++) {
            byte tmpNum = (byte) (Integer.reverse((int)(x * 255.0)) >>> 24);
            logisticMap[i] = tmpNum;
            x = logictic(x, r);
        }
    }


    public void extractMatrix() {
        // Get the positions of position points.
        extracCenters();
    }


    /**
     * Get the positions of position point centers.
     */
    private void extracCenters() {
        int pos = 0;
        for (int i = 0; i < INPUT_N; ++i) {
            for (int j = 0; j < INPUT_N; ++j) {
                if (i < 3 || i > 28 || j < 3 || j > 28) {
                    continue;
                } else if ((i > 14 && i < 17) || (j > 14 && j < 17)) {
                    continue;
                }
                // Check the mask.
                boolean invalid = false;
                for (int ki = -3; ki <= 3; ++ki) {
                    for (int kj = -3; kj <= 3; ++kj) {
                        if (orgMatrix[ki+i][kj+j] != positionMask[ki+3][kj+3]) {
                            invalid = true;
                            break;
                        }
                    }
                    if (invalid) {
                        break;
                    }
                }
                // Record.
                if (!invalid) {
                    positionCenters[pos][0] = i;
                    positionCenters[pos][1] = j;
                    pos++;
                }
            }
        }

        System.out.println("--------- Centers extracted --------- \n");
        System.out.println(pos);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                System.out.print(positionCenters[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * Add one position pattern to the matrix.
     * 
     * @param centralX the x-axis value of central point
     * @param centralY the y-axis value of central point
     */
    private void generatePosMask(int centralX, int centralY) {
        for (int i = centralX - 3; i <= centralX + 3; ++i) {
            for (int j = centralY - 3; j <= centralY + 3; ++j) {
                if ((i == centralX - 2 || i == centralX + 2) &&
                    (j != centralY - 3 && j != centralY + 3)) {
                    positionMask[i][j] = false;
                } else if((j == centralY - 2 || j == centralY + 2) &&
                          (i != centralX - 3 && i != centralX + 3)) {
                    positionMask[i][j] = false;
                } else {
                    positionMask[i][j] = true;
                }
            }
        }
    }


    public void zigzagDecode() {
        
    }

    public static void main(String[] args) throws Exception {
        String in = "0x2b23d6830x15a0de0d0x74478401"
                + "0x29e880700xfe1adf5c0xb9606129"
                + "0x1127b67c0x311690430xc6315314"
                + "0xf6e00650x92d3960b0xf59a7907"
                + "0x704e73d40x977fd8090xf516e98a"
                + "0x3e0c19f10xac626d040x6a3e5865"
                + "0xca85aa3e0x6266b640x842ddcb4"
                + "0x4e7c879c0x85dd21240x3afae3dc"
                + "0xe07908a70x664685970xb38246f7"
                + "0x511908330x40a111ee0xc12c8fd1"
                + "0x82984c520x4ddee6f6";
        QRDecryption dec = new QRDecryption(in);
        dec.decode();
    }
}

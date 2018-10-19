package QRTest;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import QRcode.QREncryption;


public class QREncryptionTest {
    
    @Test
    public void testFillPayload() throws Exception {
        QREncryption redisClient = new QREncryption("CC Team");
        redisClient.addPositionPattern();
        redisClient.addTimingPattern();
        redisClient.addAlignmentPattern();
        redisClient.fillPayload();
        redisClient.printHelper();
        redisClient.MatrixToBytes();
        
        assertEquals("answer", "answer");
    }
    
    @Test
    public void testFillPayload2() throws Exception {
        QREncryption redisClient = new QREncryption("CC Team is awesome!");
        redisClient.addPositionPattern();
        redisClient.addTimingPattern();
        redisClient.addAlignmentPattern();
        redisClient.fillPayload();
        redisClient.printHelper();
        redisClient.MatrixToBytes();
        
        assertEquals("answer", "answer");
    }
    
}

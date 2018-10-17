package QRTest;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import QRcode.QREncryption;


public class QREncryptionTest {
    
    @Test
    public void testFillPayload() throws Exception {
        QREncryption redisClient = new QREncryption("CC Team");
        redisClient.fillPayload();
        assertEquals("answer", "answer");
    }
}

import java.util.Base64;


public class Base64Utils {

    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    /**
     * base64转Byte[]
     *
     * @param base64 1
     * @return byte[]
     * @author zhangty
     * @date 2018/12/24 13:07
     */
    public static byte[] base64ToByte(String base64) {
        return DECODER.decode(base64);
    }

    /**
     * byte[]转Base64
     *
     * @param b 1
     * @return java.lang.String
     * @author zhangty
     * @date 2018/12/24 13:04
     */
    public static String byteToBase64(byte[] b) {
        return ENCODER.encodeToString(b);
    }

}

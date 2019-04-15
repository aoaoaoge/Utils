import java.io.UnsupportedEncodingException;

/**
 * 
 */



public class CharToCode {

    /**
     * 获得单个汉字的Ascii，并用"-"连接成一个字符串
     * @param cn char 汉字字符
     * @return string 错误返回 空字符串,否则返回ascii
     * @throws UnsupportedEncodingException
     */
	public static String getCnAscii(char cn){
		byte[] bytes = null;
		try {
			bytes = (String.valueOf(cn)).getBytes("gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (bytes == null || bytes.length > 2 || bytes.length <= 0) { // ����
			return "";
		}
		else{
			if (bytes.length == 1) { // Ӣ���ַ�
				return new String(bytes);
			}
			if (bytes.length == 2) { // �����ַ�
				int hightByte = 256 + bytes[0];
				int lowByte = 256 + bytes[1];
				String ascii = String.valueOf(hightByte) + "-" + String.valueOf(lowByte);
				return ascii;
			}
			return "";
		}
	}

}

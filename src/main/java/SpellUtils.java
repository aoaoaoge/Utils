
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * ConvertStringToSpell用于获取指定字符串的拼音， 目前存在的问题是还不支持获取字符串中所有多音字的组合，
 *         如果是多音字则之采用其第一个读音
 */
public class SpellUtils {

	/**
	 * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换，如果无法找到的汉字用"_"代替
	 *
	 * @param inputString
	 *            String 待转换的字符窜
	 * @return String[] 转换成全拼后的字符串数组
	 */
	public static String[] convertStringToSpell(String inputString) {
		inputString = qToB(inputString);
		if (null == inputString || "".equals(inputString.trim())) {
			return new String[0];
		}
		String[] stringSpells = new String[inputString.length()];
		char[] chars = inputString.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			String ascii = CharToCode.getCnAscii(chars[i]);
			String[] charSpells = getSpellByAscii(ascii);
			if (charSpells.length != 0) {
				stringSpells[i] = charSpells[0]; // 如果查询到该字符拼音，则记录该字符的第一个读音
			} else {
				stringSpells[i] = "_"; // 如果字符的拼音无法找到则用占位符代替，在查询数据库时应使用LIKE关键字
			}
		}
		return stringSpells;
	}

	/**
	 * 根据ASCII码连接成的字符串到SpellMap中查找对应的拼音
	 *
	 * @param ascii
	 *            字符对应的ASCII连接的字符串
	 * @return String 拼音,首先判断是否是中文如果是英文直接返回字符,
	 *         否则到SpellMap中查找,如果没有找到拼音,则返回空数组,如果找到则返回该汉字所有读音的数组.
	 */
	public static String[] getSpellByAscii(String ascii) {
		String[] spells;
		if (ascii.indexOf("-") > -1) {
			// 如果找到对应汉字的拼音则返回该汉字所有读音的数组
			if (CodeSpellMap.getSpellMap().get(ascii) != null) {
				spells = CodeSpellMap.getSpellMap().get(ascii).toString()
						.split(",");
			} else {
				spells = new String[0];
			}
		} else {
			// 如果ASCII不是汉字直接返回原字符
			spells = new String[1];
			spells[0] = ascii;
		}
		return spells;
	}

	/**
	 * 返回单个汉字的拼音首字母，如果是多音字以第一个拼音为准
	 *
	 * @param inputChar
	 *            单个汉字
	 * @return 拼音首字母
	 */
	public static String getFirstLetter(String inputChar) {

		String spellTemp = "";
		String asciiCode = CharToCode.getCnAscii(inputChar.charAt(0)); // 得到该汉字的ASC码
		String[] charSpells = getSpellByAscii(asciiCode);
		if (charSpells.length != 0) {
			spellTemp = charSpells[0]; // 如果查询到该字符拼音，则记录该字符的第一个读音
		} else {
			return "";
		}
		return spellTemp.substring(0, 1);

	}

	/**
	 * 返回单个汉字的拼音首字母，如果是多音字以第一个拼音为准
	 *
	 * @param inputChar
	 *            单个汉字
	 * @return 拼音首字母
	 */
	public static String getFirstLetter(char c) {
		String spellTemp = "";
		String asciiCode = CharToCode.getCnAscii(c); // 得到该汉字的ASC码
		String[] charSpells = getSpellByAscii(asciiCode);
		if (charSpells.length != 0) {
			spellTemp = charSpells[0]; // 如果查询到该字符拼音，则记录该字符的第一个读音
		} else {
			return "";
		}
		return spellTemp.substring(0, 1);

	}

	/**
	 * 得到某个字符串的拼音简码，由该字符串中所有汉字的拼音首字母组成
	 */
	public static String getPyjm(String inputStr) {
		inputStr = qToB(inputStr);
		StringBuilder results = new StringBuilder();
		if (StringUtils.isBlank(inputStr)) {
			return null;
		}
		for (int i = 0; i < inputStr.length(); i++) {
			char c = inputStr.charAt(i);
			results.append(getFirstLetter(c));
		}
		String xmjp = "";
		char c[] = results.toString().toCharArray();
		for(char cc: c){
			String s = String.valueOf(cc);
			if(Pattern.matches("[0-9a-zA-Z]*", s)){
				xmjp += s;
			}
		}
		return xmjp.toUpperCase();
	}


	public static String getPy(String input){
		String xmpy = "";
		String[] lettersOfName =  convertStringToSpell(input);
		if (lettersOfName != null) {
			for (String letter : lettersOfName) {
				char c[] = letter.toCharArray();
				for(char cc: c){
					String s = String.valueOf(cc);
					if(Pattern.matches("[0-9a-zA-Z]*", s)){
						xmpy += s;
					}
				}
			}
		}
		return xmpy;
	}
	//全角转半角
	public static String qToB(String input){
		char c[] = input.toCharArray();
		for(int i=0; i < c.length ; i++){
			if(c[i]=='\u3000'){
				c[i]=' ';
			}else if(c[i] >'\uFF00' && c[i] < '\uFF5F'){
				c[i] = (char)(c[i]-65248);
			}
		}
		String ss = new String(c);
		return ss;
	}
}

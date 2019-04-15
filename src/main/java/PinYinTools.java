 /*
  *
  *	Copyright [2025] [WuTing]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *   http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  *
  *
  */

 import net.sourceforge.pinyin4j.PinyinHelper;
 import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
 import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
 import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
 import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

 import java.util.HashMap;
 import java.util.Map;

 /**
  * @author WuTing q10_9986@163.com
  * @version [V1.0, 2015年8月21日]
  * @Title: PinYinTools
  * @Description: 获取拼音的工具  需引入包 pinyin4j.jar
  * @date 2015年8月21日 下午3:02:42
  * @see [相关类/方法]
  * @since [产品/模块版本]
  */
 public class PinYinTools {

     /**
      * 汉字转换为汉语拼音首字母，英文字符不变
      *
      * @param chines 汉字
      * @return 拼音
      */
     public static String converterToFirstSpell(String chines) {
         chines = chines.replaceAll("（", "(").replaceAll("）", ")");
         String pinyinName = "";
         char[] nameChar = chines.toCharArray();
         HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
         //大小写
         defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
         //声调显示与否
         defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
         for (int i = 0; i < nameChar.length; i++) {
             if (nameChar[i] > 128) {
                 try {
                     pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
                 } catch (BadHanyuPinyinOutputFormatCombination e) {
                     e.printStackTrace();
                 }
             } else {
                 pinyinName += nameChar[i];
             }
         }
         return pinyinName;
     }

     /**
      * 汉字转换为汉语拼音，英文字符不变
      *
      * @param chines 汉字
      * @return 拼音
      */
     public static String converterToSpell(String chines) {
         chines = chines.replaceAll("（", "(").replaceAll("）", ")");
         String pinyinName = "";
         char[] nameChar = chines.toCharArray();
         HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
         //大小写
         defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
         //声调显示与否
         defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
         for (int i = 0; i < nameChar.length; i++) {
             if (nameChar[i] > 128) {
                 try {
                     pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
                 } catch (Exception e) {
                     e.printStackTrace();
                     throw new RuntimeException("请输入符合规范的姓名");
                 }
             } else {
                 pinyinName += nameChar[i];
             }
         }
         return pinyinName;
     }

     //姓名拼音 例：{"qp":"zhangsan","jp":"zs"} ，即全拼和简拼
     public static Map<String,String> converterToMap(String cn){
         Map<String,String> map = new HashMap<>();
         map.put("qp",converterToSpell(cn));
         map.put("jp",converterToFirstSpell(cn));
         return map;
     }

     public static void main(String[] args) {
         String a = converterToSpell("魔兽世界 %￥……&搞个ss123（）");
         System.out.println(a);
     }
 }

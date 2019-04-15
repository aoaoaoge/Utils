
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 此类包含了对日期进行处理的各种方法
 */
public class DateUtil {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public static final int PERIOD_TYPE_YEAR = 1;

	public static final int PERIOD_TYPE_MONTH = 2;

	public static final int PERIOD_TYPE_DAY = 3;

	/**
	 * 判断一个日期是否在另一个日期之前
	 *
	 * @param fowardDate
	 *            被判断的日期
	 * @param afterwardDate
	 *            是否在此日期之前
	 * @return 判断结果
	 * @throws Exception
	 *             如果输入的日期格式不满足"yyyyMMdd",会抛出此异常.
	 */
	public static boolean isBefore(String fowardDate, String afterwardDate)
			throws Exception {
		try {
			SimpleDateFormat sfForDB = new SimpleDateFormat("yyyyMMdd");
			Date aDate = sfForDB.parse(fowardDate);
			Date otherDate = sfForDB.parse(afterwardDate);
			// 小于等于时返回true
			if (aDate.before(otherDate) || aDate.equals(otherDate)) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			throw new Exception("日期格式错误,正确格式为YYYYMMdd");
		}
	}

	/**
	 * 将Date类型的日期按指定的日期格式转换为字符串
	 *
	 * @param date
	 *            进行转换的Date类实例
	 * @param pattern
	 *            转换的日期格式
	 * @return 转换后的字符串类型的日期
	 */
	public static String getDateString(Date date, String pattern) {
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		String dateString = sf.format(date);
		return dateString;
	}

	/**
	 * 将String类型的日期按指定的日期格式转换为字符串
	 *
	 * @param date
	 *            进行转换的Date类实例
	 * @param pattern
	 *            转换的日期格式
	 * @return 转换后的字符串类型的日期
	 */
	public static String getDateString(String date, String pattern) {
		String type = "";
		if (date == null || date.length() == 0)
			return "";
		else if (date.length() == 6) {
			type = "yyyyMM";
		} else if (date.length() == 8) {
			type = "yyyyMMdd";
		} else if (date.length() == 12) {
			type = "yyyyMMddHHmm";
		}
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat(type).parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		String dateString = sf.format(cal.getTime());
		return dateString;
	}

	/**
	 * 将字符串类型的日期按指定日期格式进行格式化
	 *
	 * @param data
	 *            需格式化的日期字符串
	 * @param stringInputPattern
	 *            输入的日期格式
	 * @param pattern
	 *            输出的日期格式
	 * @return 转换后的日期字符串
	 */
	public static String formatDate(String data, String stringInputPattern,
									String pattern) {

		String dataAfterFormat = "";
		if (data == null || data.equals("")) {
			return dataAfterFormat;
		}
		int validLength = Math.min(data.length(), stringInputPattern.length());
		// 构造输出格式器
		SimpleDateFormat outputSf = new SimpleDateFormat(pattern);
		// 构造输入格式器
		SimpleDateFormat inputSf = new SimpleDateFormat(
				stringInputPattern.substring(0, validLength));
		try {
			dataAfterFormat = outputSf.format(inputSf.parse(data.substring(0,
					validLength)));
		} catch (ParseException e) {
			throw new RuntimeException("日期格式化发生错误！" + "source:" + data);
		}
		return dataAfterFormat;
	}

	/**
	 * 对日期进行加减操作
	 *
	 * @param orgDate
	 *            起始日期，格式为yyyyMMdd
	 * @param section
	 *            期限，格式为yyMMdd,如010000代表一年
	 * @param isAdd
	 *            为true时执行加法，否则执行减法
	 * @return 加减运算后的日期
	 */
	public static String addDate(String orgDate, String section, boolean isAdd) {

		int yearSec = 0;
		int monthSec = 0;
		int daySec = 0;

		Calendar calendar = Calendar.getInstance();
		try {
			Date aDate = new SimpleDateFormat("yyyyMMdd").parse(orgDate);
			calendar.setTime(aDate);
			yearSec = Integer.parseInt(section.substring(0, 2));
			monthSec = Integer.parseInt(section.substring(2, 4));
			daySec = Integer.parseInt(section.substring(4, 6));

		} catch (Exception e) {
			throw new RuntimeException("日期或期限格式错误,请输入正确的格式!");
		}
		// 如果是相减就对期限的值取负
		if (!isAdd) {
			yearSec = -yearSec;
			monthSec = -monthSec;
			daySec = -daySec;
		}

		calendar.add(Calendar.YEAR, yearSec);
		calendar.add(Calendar.MONTH, monthSec);
		calendar.add(Calendar.DAY_OF_MONTH, daySec);
		// 取得年份
		String returnYear = String.valueOf(calendar.get(Calendar.YEAR));
		String returnMonth = "";
		// 取得月份时，如果是1－9月就格式化为0x的形式
		if (calendar.get(Calendar.MONTH) <= 8) {
			returnMonth = "0"
					+ String.valueOf(calendar.get(Calendar.MONTH) + 1);
		} else {
			returnMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		}
		// 取得天数
		String returnDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		if (returnDay.length() == 1) {
			returnDay = "0" + returnDay;
		}
		return returnYear + returnMonth + returnDay;
	}

	/**
	 * 按指定日期格式返回当前日期
	 *
	 * @param pattern
	 *            格式化字符串
	 * @return 格式化后的当前日期
	 */
	public static String getCurrentDate(String pattern) {
		return getDateString(new Date(), pattern);
	}

	/**
	 * 计算年龄
	 *
	 * @param birthday
	 *            出生日期的字符串表示形式
	 * @param pattern
	 *            出生日期的输入格式
	 * @return 年龄
	 * @exception ParseException
	 *                输入出生日期格式有错
	 */
	public static int computeAge(String birthday, String pattern)
			throws ParseException {
		Calendar nowCal = Calendar.getInstance();
		Calendar bdCal = Calendar.getInstance();
		nowCal.setTime(new Date());
		bdCal.setTime(new SimpleDateFormat(pattern).parse(birthday));
		int age = nowCal.get(Calendar.YEAR) - bdCal.get(Calendar.YEAR);
		int offsetMonth = nowCal.get(Calendar.MONTH)
				- bdCal.get(Calendar.MONTH);
		int offsetDay = nowCal.get(Calendar.DAY_OF_MONTH)
				- bdCal.get(Calendar.DAY_OF_MONTH);
		if (offsetMonth < 0 || offsetDay < 0) {
			age--;
		}
		return age;
	}

	/**
	 * @param date
	 *            某个日期
	 * @return 这个日期是星期几 如：星期三，星期五
	 */
	public static String getCNDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String day_of_week = "";
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case 1:
				day_of_week = "周日";
				break;
			case 2:
				day_of_week = "周一";
				break;
			case 3:
				day_of_week = "周二";
				break;
			case 4:
				day_of_week = "周三";
				break;
			case 5:
				day_of_week = "周四";
				break;
			case 6:
				day_of_week = "周五";
				break;
			case 7:
				day_of_week = "周六";
				break;
		}
		return day_of_week;
	}

	/**
	 * 获得以某个时间点所在的某段日期（年、月、日）的开始日期,如某个月的第一天，某一年的第一天等
	 *
	 * @param periodType
	 *            年度：1;月度：2;日：3
	 * @param selectedDate
	 *            所选择的时间点
	 * @return
	 */
	public static String getPeriodStartDate(int periodType, Date selectedDate) {
		if (selectedDate == null)
			return null;

		Calendar ca = new GregorianCalendar();
		ca.setTime(selectedDate);
		if (PERIOD_TYPE_YEAR == periodType) {
			ca.set(Calendar.MONTH, 0);
			ca.set(Calendar.DAY_OF_MONTH, 1);
		} else if (PERIOD_TYPE_MONTH == periodType) {
			ca.set(Calendar.DAY_OF_MONTH, 1);
		}
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		Date date = ca.getTime();
		return sdf.format(date);
	}

	/**
	 * 获得以某个时间点所在的某段日期（年、月、日）的截止日期,如某个月的最后一天，某一年的最后一天等
	 *
	 * @param periodType
	 *            年度：1;月度：2;日：3
	 * @param selectedDate
	 *            所选择的时间点
	 * @return
	 */
	public static String getPeriodEndDate(int periodType, Date selectedDate) {
		if (selectedDate == null)
			return null;

		Calendar ca = new GregorianCalendar();
		ca.setTime(selectedDate);
		if (PERIOD_TYPE_YEAR == periodType) {
			ca.set(Calendar.MONTH, 11);
			ca.set(Calendar.DAY_OF_MONTH,
					ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else if (PERIOD_TYPE_MONTH == periodType) {
			ca.set(Calendar.DAY_OF_MONTH,
					ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		Date date = ca.getTime();
		return sdf.format(date);
	}

	/**
	 * 输入参数： year:年份 month:月份 startDay:开始日期（记帐起始日期，如输出的月范围为2007-1-20到2007-2-20）,
	 * 则这里的startDay为20 输出参数：
	 * String[0]为月范围开始日期(20070120)，String[1]为月范围结束日期(20070220)
	 */
	public static String[] countMonthRange(int year, int month, int startDay)
			throws IllegalArgumentException {
		String[] result = new String[2];
		Calendar temp = Calendar.getInstance();
		try {
			temp.set(year, month - 1, startDay);
			result[1] = DateUtil.getDateString(temp.getTime(), "yyyyMMdd");
			temp.add(Calendar.MONTH, -1);
			temp.add(Calendar.DATE, 1);
			result[0] = DateUtil.getDateString(temp.getTime(), "yyyyMMdd");
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.toString());
		}
		return result;
	}

	/**
	 * 输入参数：
	 *
	 * @param year
	 *            年份
	 * @return 将输入的数字年份转化为中文
	 */
	public static String convertYear(String year) {

		if (year.indexOf("0") != -1) {
			year = year.replaceAll("0", "〇");
		}
		if (year.indexOf("1") != -1) {
			year = year.replaceAll("1", "一");
		}
		if (year.indexOf("2") != -1) {
			year = year.replaceAll("2", "二");
		}
		if (year.indexOf("3") != -1) {
			year = year.replaceAll("3", "三");
		}
		if (year.indexOf("4") != -1) {
			year = year.replaceAll("4", "四");
		}
		if (year.indexOf("5") != -1) {
			year = year.replaceAll("5", "五");
		}
		if (year.indexOf("6") != -1) {
			year = year.replaceAll("6", "六");
		}
		if (year.indexOf("7") != -1) {
			year = year.replaceAll("7", "七");
		}
		if (year.indexOf("8") != -1) {
			year = year.replaceAll("8", "八");
		}
		if (year.indexOf("9") != -1) {
			year = year.replaceAll("9", "九");
		}
		return year;
	}

	/**
	 * 输入参数：
	 *
	 * @param month
	 *            月份
	 * @return 将输入的数字月份转化为中文
	 */
	public static String converMonth(String month) {
		if (month.indexOf("01") != -1) {
			month = month.replaceAll("01", "一");
		}
		if (month.indexOf("02") != -1) {
			month = month.replaceAll("02", "二");
		}
		if (month.indexOf("03") != -1) {
			month = month.replaceAll("03", "三");
		}
		if (month.indexOf("04") != -1) {
			month = month.replaceAll("04", "四");
		}
		if (month.indexOf("05") != -1) {
			month = month.replaceAll("05", "五");
		}
		if (month.indexOf("06") != -1) {
			month = month.replaceAll("06", "六");
		}
		if (month.indexOf("07") != -1) {
			month = month.replaceAll("07", "七");
		}
		if (month.indexOf("08") != -1) {
			month = month.replaceAll("08", "八");
		}
		if (month.indexOf("09") != -1) {
			month = month.replaceAll("09", "九");
		}
		if (month.indexOf("10") != -1) {
			month = month.replaceAll("10", "十");
		}
		if (month.indexOf("11") != -1) {
			month = month.replaceAll("11", "十一");
		}
		if (month.indexOf("12") != -1) {
			month = month.replaceAll("12", "十二");
		}
		return month;
	}

	/**
	 * 输入参数：
	 *
	 * @param year
	 *            天数
	 * @return 将输入的数字天数转化为中文
	 */
	public static String converDay(String day) {

		Integer intDay = Integer.valueOf(day);
		if (intDay.intValue() <= 10) {
			if (day.indexOf("01") != -1) {
				day = day.replaceAll("01", "一");
			}
			if (day.indexOf("02") != -1) {
				day = day.replaceAll("02", "二");
			}
			if (day.indexOf("03") != -1) {
				day = day.replaceAll("03", "三");
			}
			if (day.indexOf("04") != -1) {
				day = day.replaceAll("04", "四");
			}
			if (day.indexOf("05") != -1) {
				day = day.replaceAll("05", "五");
			}
			if (day.indexOf("06") != -1) {
				day = day.replaceAll("06", "六");
			}
			if (day.indexOf("07") != -1) {
				day = day.replaceAll("07", "七");
			}
			if (day.indexOf("08") != -1) {
				day = day.replaceAll("08", "八");
			}
			if (day.indexOf("09") != -1) {
				day = day.replaceAll("09", "九");
			}
			if (day.indexOf("10") != -1) {
				day = day.replaceAll("10", "十");
			}
		} else if (intDay.intValue() > 10 && intDay.intValue() < 20) {
			if (day.indexOf("11") != -1) {
				day = day.replaceAll("11", "十一");
			}
			if (day.indexOf("12") != -1) {
				day = day.replaceAll("12", "十二");
			}
			if (day.indexOf("13") != -1) {
				day = day.replaceAll("13", "十三");
			}
			if (day.indexOf("14") != -1) {
				day = day.replaceAll("14", "十四");
			}
			if (day.indexOf("15") != -1) {
				day = day.replaceAll("15", "十五");
			}
			if (day.indexOf("16") != -1) {
				day = day.replaceAll("16", "十六");
			}
			if (day.indexOf("17") != -1) {
				day = day.replaceAll("17", "十七");
			}
			if (day.indexOf("18") != -1) {
				day = day.replaceAll("18", "十八");
			}
			if (day.indexOf("19") != -1) {
				day = day.replaceAll("19", "十九");
			}
		} else if (intDay.intValue() >= 20 && intDay.intValue() <= 31) {

			if (day.indexOf("20") != -1) {
				day = day.replaceAll("20", "二十");
			}
			if (day.indexOf("21") != -1) {
				day = day.replaceAll("21", "二十一");
			}
			if (day.indexOf("22") != -1) {
				day = day.replaceAll("22", "二十二");
			}
			if (day.indexOf("23") != -1) {
				day = day.replaceAll("23", "二十三");
			}
			if (day.indexOf("24") != -1) {
				day = day.replaceAll("24", "二十四");
			}
			if (day.indexOf("25") != -1) {
				day = day.replaceAll("25", "二十五");
			}
			if (day.indexOf("26") != -1) {
				day = day.replaceAll("26", "二十六");
			}
			if (day.indexOf("27") != -1) {
				day = day.replaceAll("27", "二十七");
			}
			if (day.indexOf("28") != -1) {
				day = day.replaceAll("28", "二十八");
			}
			if (day.indexOf("29") != -1) {
				day = day.replaceAll("29", "二十九");
			}
			if (day.indexOf("30") != -1) {
				day = day.replaceAll("30", "三十");
			}
			if (day.indexOf("31") != -1) {
				day = day.replaceAll("31", "三十一");
			}
		}
		return day;
	}

	/**
	 * 获取 当前年、半年、季度、月、日、小时 开始结束时间
	 */

	private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyyMMddHHmmss");;


	/**
	 * 获得本月的开始时间
	 *
	 * @return
	 */
	public static Date getCurrentMonthStartTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			now = shortSdf.parse(shortSdf.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 本月的结束时间
	 *
	 * @return
	 */
	public static Date getCurrentMonthEndTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 235959");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前季度的开始时间
	 *
	 * @return
	 */
	public static Date getCurrentQuarterStartTime() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 6);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 000000");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前季度的结束时间
	 *
	 * @return
	 */
	public static Date getCurrentQuarterEndTime() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3) {
				c.set(Calendar.MONTH, 2);
				c.set(Calendar.DATE, 31);
			} else if (currentMonth >= 4 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 5);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 7 && currentMonth <= 9) {
				c.set(Calendar.MONTH, 8);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 10 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 11);
				c.set(Calendar.DATE, 31);
			}
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 235959");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

}

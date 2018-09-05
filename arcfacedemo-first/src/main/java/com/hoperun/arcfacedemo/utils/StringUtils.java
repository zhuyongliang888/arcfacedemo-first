package com.hoperun.arcfacedemo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串处理工具类
 */
public final class StringUtils
{
    public final static String OFFSET = "startIndex";
    public final static String LIMIT = "pageSize";
    public final static String ORDERFIELD = "orderField";
    public final static String ORDERFIELDTYPE = "orderFieldType";
    public final static String ROLE = "role";

    public final static String CREATEDATE = "create_date";
    public final static String DESC = "desc";
    public final static String ASC = "asc";

    private static String pattern = "[A-Fa-f0-9]+";

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException
    {
	return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 此方法中前三位格式有： 13+任意数 15+除4的任意数 18+除1和4的任意数
     * 17+除9的任意数 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException
    {
	String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
	Pattern p = Pattern.compile(regExp);
	Matcher m = p.matcher(str);
	return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException
    {
	String regExp = "^(5|6|8|9)\\d{7}$";
	Pattern p = Pattern.compile(regExp);
	Matcher m = p.matcher(str);
	return m.matches();
    }

    /**
     * 判断Object是否为空
     * 
     * @param object
     * @return
     */
    public static boolean isNull(Object object)
    {
	if (object == null)
	{
	    return true;
	} else
	{
	    return false;
	}
    }

    /**
     * 判断Object是否不为空
     * 
     * @param object
     * @return
     */
    public static boolean isNotNull(Object object)
    {
	return !isNull(object);
    }

    /**
     * 判断Object是否全部为空
     * 
     * @param object
     * @return
     */
    public static boolean isNullAll(Object... objects)
    {
	boolean bool = true;
	for (Object object : objects)
	{
	    if (isNotNull(object))
	    {
		bool = false;
		break;
	    }
	}
	return bool;
    }

    /**
     * 判断Object是否全部不为空
     * 
     * @param object
     * @return
     */
    public static boolean isNotNullAll(Object... objects)
    {
	boolean bool = true;
	if (objects.length == 0)
	{
	    return false;
	}
	for (Object object : objects)
	{
	    if (isNull(object))
	    {
		bool = false;
		break;
	    }
	}
	return bool;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str)
    {
	if ((str == null) || (str.trim().length() == 0))
	{
	    return true;
	} else
	{
	    return false;
	}
    }

    /**
     * 判断字符串是否非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str)
    {
	return !isEmpty(str);
    }

    /**
     * 判断字符串是否全部为空 当入参长度为0时，直接返回true
     *
     * @param strs
     * @return
     */
    public static boolean isEmptyAll(String... strs)
    {
	boolean bool = true;
	for (String str : strs)
	{
	    if (isNotEmpty(str))
	    {
		bool = false;
		break;
	    }
	}
	return bool;
    }

    /**
     * 判断字符串是否全部非空 当入参长度为0时，直接返回false
     *
     * @param strs
     * @return
     */
    public static boolean isNotEmptyAll(String... strs)
    {
	boolean bool = true;
	if (strs.length == 0)
	{
	    return false;
	}
	for (String str : strs)
	{
	    if (isEmpty(str))
	    {
		bool = false;
		break;
	    }
	}
	return bool;
    }

    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s
     *            16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s)
    {
	int len = s.length();
	if ((len % 2) != 0)
	{
	    s = "0" + s;
	    len++;
	}
	byte[] b = new byte[len / 2];
	for (int i = 0; i < len; i += 2)
	{
	    // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
	    b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
	}
	return b;
    }

    public static byte[] hexStringToByteArray(String s, int resLen)
    {
	byte[] b = hexStringToByteArray(s);
	int step = resLen - b.length;
	if (step > 0)
	{
	    byte[] temp = new byte[resLen];
	    System.arraycopy(b, 0, temp, step, b.length);
	    return temp;
	}
	return b;
    }

    public static boolean hasNumeric(String str)
    {
	for (int i = str.length(); --i >= 0;)
	{
	    if (Character.isDigit(str.charAt(i)))
	    {
		return true;
	    }
	}
	return false;
    }

    public static boolean hasLowLetter(String str)
    {
	for (int i = str.length(); --i >= 0;)
	{
	    if ((str.charAt(i) >= 97) && (str.charAt(i) <= 122))
	    {
		return true;
	    }
	}
	return false;
    }

    public static boolean hasUpperLetter(String str)
    {
	for (int i = str.length(); --i >= 0;)
	{
	    if ((str.charAt(i) >= 65) && (str.charAt(i) <= 90))
	    {
		return true;
	    }
	}
	return false;
    }

    /**
     * byte数组转hex字符串<br/>
     * 一个byte转为2个hex字符
     *
     * @param src
     * @return
     */
    public static String bytes2Hex(byte[] src)
    {
	char[] res = new char[src.length * 2];
	final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	for (int i = 0, j = 0; i < src.length; i++)
	{
	    res[j++] = hexDigits[(src[i] >>> 4) & 0x0f];
	    res[j++] = hexDigits[src[i] & 0x0f];
	}
	return new String(res);
    }

    public static String bytes2Hex(byte[] src, int len)
    {
	char[] res = new char[src.length * 2];
	final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	for (int i = 0, j = 0; i < src.length; i++)
	{
	    res[j++] = hexDigits[(src[i] >>> 4) & 0x0f];
	    res[j++] = hexDigits[src[i] & 0x0f];
	}
	String content = new String(res);
	StringBuilder sb = new StringBuilder();
	int tempLen = content.length();
	while (len > tempLen)
	{
	    sb.append('0');
	    tempLen++;
	}
	sb.append(content);
	return sb.toString();
    }

    /**
     * int十进制转换为16进制，不足位数高位补零
     *
     * @param number
     * @return
     */
    public static String getHexString(int number)
    {
	String hex = Integer.toHexString(number);
	hex.toUpperCase();
	while (hex.length() < 4)
	{
	    hex = "0" + hex;
	}
	return hex;
    }

    /**
     * 字符串转换为16进制字符串
     *
     * @param s
     * @return
     */
    public static String stringToHexString(String s)
    {
	String str = "";
	for (int i = 0; i < s.length(); i++)
	{
	    int ch = s.charAt(i);
	    String s4 = Integer.toHexString(ch);
	    str = str + s4;
	}
	return str;
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s)
    {
	if ((s == null) || s.equals(""))
	{
	    return null;
	}
	s = s.replace(" ", "");
	byte[] baKeyword = new byte[s.length() / 2];
	for (int i = 0; i < baKeyword.length; i++)
	{
	    try
	    {
		baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16));
	    } catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
	try
	{
	    s = new String(baKeyword, "gbk");
	    new String();
	} catch (Exception e1)
	{
	    e1.printStackTrace();
	}
	return s;
    }

    public static String stringArraytoString(String[] values)
    {
	StringBuffer sb = new StringBuffer();
	if (values == null)
	{
	    return null;
	}
	for (int i = 0; i < values.length; i++)
	{
	    sb.append(values[i] + ",");
	}
	String s = sb.toString();
	return s.length() == 0 ? "" : s.substring(0, s.length() - 1);
    }

    /**
     * 判断字符串是否为Hex字符串
     *
     * @param str
     * @return
     */
    public static boolean isHexString(String str)
    {
	return Pattern.matches(pattern, str);
    }

    /**
     * 手机号验证
     * 
     * @param str
     * 
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str)
    {
	Pattern p = null;
	Matcher m = null;
	boolean b = false;
	p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
	m = p.matcher(str);
	b = m.matches();
	return b;
    }

    public static String dateToDateStr(Date date)
    {
	if (date == null)
	{
	    return null;
	}
	String dateStr = null;
	// format的格式可以任意
	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	try
	{
	    dateStr = sdf.format(date);
	} catch (Exception e)
	{
	    return null;
	}
	return dateStr;
    }

}

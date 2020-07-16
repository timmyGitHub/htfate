package com.htfate.utilcenter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htfate.utilcenter.exception.MyException;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 小小工具箱
 *
 * @author 杨海涛
 * @since 2018-04-12
 */
@SuppressWarnings("ALL")
public class YHTUtils {
    // oauth2 代码
    public static final int CODE_OAUTH_ERROR = 7;
    // 程序执行成功代码
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = 1;
    public static final int CODE_TIMEOUT = 8;
    // 数据库代码详情
    public static final int DB_ADD_ERROR = 100;                    // 数据库执行保存错误
    public static final int DB_DELETED_ERROR = 101;                // 数据库执行删除错误
    public static final int DB_MODIFY_ERROR = 102;                // 数据库执行编辑错误
    public static final int DB_SEARCH_ERROR = 103;                // 数据库执行查找错误

    private static ThreadLocal threadLocal = new ThreadLocal();            // 本地变量线程

    public static void setThreadLocal (Object o) {
        threadLocal.set(o);
    }
    public static Object getThreadLocal () {
        return threadLocal.get();
    }
    public static void removeThreadLocal () {
        threadLocal.remove();
    }


    /**
     * 检查调用结果
     */
    public static void checkRes(Map m) {
        Integer code = (Integer) m.get("code");
        if (0 != code) {
            throw new MyException(m.get("msg").toString());
        }
    }
    //--------------------------------------------json工具类转换----------------------------------
    // json转换
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String obj2String(Object object) {
        String s = null;
        try {
            s = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static <T> T string2Obj(String s, Class<T> clazz) {
        T t = null;
        try {
            t = objectMapper.readValue(s, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
    /**
     * json 数组转 list
     *
     * @param o         json 数组对象
     * @param beanClass java 对象
     * @return list
     */
    /*public static List json2list(Object o, Class beanClass) {
        String text = o.toString();
        return JSON.parseArray(text, beanClass);
    }*/

    /**
     * json 对象转 java 对象
     *
     * @param o         json 对象
     * @param beanClass java 对象
     * @return java 对象
     */
    /*public static Object json2obj(Object o, Class beanClass) {
        String text = o.toString();
        return JSON.parseObject(text,beanClass);
    }*/
    //--------------------------------------------String类操作----------------------------------

    public static String getBase64Encoder(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return new String(Base64.getEncoder().encode(str.getBytes()));
    }
    /**
     * 中文转拼音
     * @param psStr 要转的中文
     * @param type camel-驼峰命名，under-下划线
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static final String getPinyin(String psStr,String type) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < psStr.length(); i++) {
            String[] laPinyin = new String[0];
            try {
                laPinyin = PinyinHelper.toHanyuPinyinStringArray(psStr.charAt(i), format);
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
//                badHanyuPinyinOutputFormatCombination.printStackTrace();
                System.out.println("中文转拼音出错：" + badHanyuPinyinOutputFormatCombination.getMessage());
            }
            if (laPinyin == null) {
                sb.append(psStr.charAt(i));
            } else {
                if ("camel".equalsIgnoreCase(type)) {
                    if (0 == i) {
                        sb.append(laPinyin[0]);
                    } else {
                        sb.append(String.valueOf(laPinyin[0].charAt(0)).toUpperCase());
                        sb.append(laPinyin[0].substring(1,laPinyin[0].length()));
                    }
                }
                else if ("under".equalsIgnoreCase(type)) {
                    if (0 == i) {
                        sb.append(laPinyin[0]);
                    } else {
                        sb.append("_");
                        sb.append(laPinyin[0]);
                    }
                }
                else {
                    sb.append(laPinyin[0]);
                }

            }
        }
        return sb.toString();
    }
    /**
     * 返回list拼接的字符串
     *
     * @param list 数据
     * @return 返回值
     */
    public static String splitList(List<Map<String, Object>> list) {
        if (!list.isEmpty()) {
            StringBuilder s = new StringBuilder();
            for (Map<String, Object> map : list) {
                s.append(map.get("application_id")).append(",");
            }
            s.deleteCharAt(s.length() - 1);
            return s.toString();
        }
        return "";
    }

    /**
     * 判断是否为空
     *
     * @param obj 值
     * @return true 空 false 不为空
     */
    public static boolean isEmpty(Object obj) {
        return null == obj || obj.toString().trim().equals("") || obj.toString().trim().equals("null");
    }

    /**
     * 判断是否不为空
     *
     * @param str 对象
     * @return true 不为空,false 为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !(null == obj || obj.toString().trim().equals("") || obj.toString().trim().equalsIgnoreCase("null"));
    }
    /**
     * 检查参数有效性
     * @param params 要检查的参数
     * @param v 校对的参数
     */
    public static void checkParams(Map<String, Object> params, List<String> v) {
        for (String s : v) {
            if (!params.containsKey(s)) {
                params.put(s,"");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (isEmpty(entry.getValue())) {
                throw new MyException(entry.getKey() + " is not null");
            }
        }
    }

    /**
     * 获取UUID
     *
     * @return 唯一ID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getMD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 初始化失败："+e.getMessage());
            e.printStackTrace();
        }
        // 将String 转换为byte类型
        char[] chars = str.toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        // 获取md5计算后的byte值
        byte[] md5Byte = md5.digest(bytes);
        // 转回string类型
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < md5Byte.length; i++) {
            //使用0xff保持转值不出错
            int val = ((int) md5Byte[i]) & 0xff;
            if (val < 16) {
                stringBuffer.append("0");
            } else {
                stringBuffer.append(Integer.toHexString(val));
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 去掉空格
     */
    public static String getTrim(Object obj) {
        Pattern p = Pattern.compile(TRIM);
        Matcher m = p.matcher(obj.toString());
        return m.replaceAll("");
    }

    /**
     * list 转String字符串
     *
     * @param stringList 数据
     * @param param      用什么分隔
     * @return 返回字符串
     */
    public static String listToString(List<String> stringList, String param) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(param);
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * list 转String字符串
     *
     * @param stringList 数据
     * @return 返回字符串
     */
    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (String string : stringList) {
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 数组转字符串
     *
     * @param a 数组
     * @return 字符串
     */
    public static String arrayToString(Object[] a) {
        if (a == null)
            return null;

        int iMax = a.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString();
            b.append(",");
        }
    }

    public static String arrayToString(Object[] a, String sign) {
        if (a == null)
            return null;

        int iMax = a.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString();
            b.append(sign);
        }
    }

    // upperCase 为 true 则全部首字母大写，否则第二个之后为首字母大写
    public static String arrayToString(String[] strArr, String sign, boolean upperCase) {
        if (strArr == null)
            return null;

        int iMax = strArr.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {

            if (upperCase) {
                b.append(new StringBuilder().append(Character.toUpperCase(strArr[i].charAt(0))).append(strArr[i].substring(1)).toString());
            } else {
                if (0 != i) {
                    b.append(new StringBuilder().append(Character.toUpperCase(strArr[i].charAt(0))).append(strArr[i].substring(1)).toString());
                } else {
                    b.append(strArr[i]);
                }
            }
            if (i == iMax)
                return b.toString();
            b.append(sign);
        }
    }
    /**
     * 地址解析
     *
     * @param address 陕西省西安市莲湖区北关街道宫园壹号二期三号楼
     * @return [{province=陕西省, city=西安市, county=莲湖区, town=北关街道, village=宫园壹号二期三号楼}]
     */
    public static List<Map<String, String>> parseAddress(String address) {
        String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.*?区|.+市|.+旗|.+海域|.+岛)?(?<town>[^街道]+街道|.+镇|.+区)?(?<society>[^社区].*?社区|.+镇)?(?<village>.*)";
        Matcher m = Pattern.compile(regex).matcher(address);
        String province, city, county, town, society, village;
        List<Map<String, String>> table = new ArrayList<>();
        Map<String, String> row;
        while (m.find()) {
            row = new LinkedHashMap<>();
            province = m.group("province");
            row.put("province", province == null ? "" : province.trim());
            city = m.group("city");
            row.put("city", city == null ? "" : city.trim());
            county = m.group("county");
            row.put("county", county == null ? "" : county.trim());
            town = m.group("town");
            row.put("town", town == null ? "" : town.trim());
            society = m.group("society");
            row.put("society",society == null ? "" : society.trim());
            village = m.group("village");
            row.put("village", village == null ? "" : village.trim());
            table.add(row);
        }
        return table;
    }
    /**
     * 根据传入的个数  从A开始返回个数的ASCII码
     *
     * @param num 从A开始后的一共几个
     * @return 对应的ASCII
     */
    public static String numAscii(int num) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < num; i++) {
            char c = (char) (65 + i);
            s.append(String.valueOf(c)).append(",");
        }
        return s.deleteCharAt(s.length() - 1).toString();
    }

    /**
     * 判断字符串在数组中是否存在
     *
     * @param str       字符串
     * @param ignoreArr 数组
     * @return 结果
     */
    public static boolean isExist(String str, String[] ignoreArr) {
        for (String x : ignoreArr) {
            if (str.equals(x))
                return true;
        }
        return false;
    }

    /**
     * 判断是否为字母的
     *
     * @param str 源
     * @return boolean
     */
    public static boolean isEN(String str) {
        byte[] b = str.getBytes();
        if (str.length() == b.length) {
            return true;
        }
        return false;
    }

    /**
     * 一条横线，例如：——————————timmy————————————
     *
     * @param str 内容
     * @return 字符串
     */
    public static String line(String str) {
        byte[] b = str.getBytes();
        int len = str.length();
        int n = 100 - len;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i == n / 2) {
                sb.append(str);
            } else {
                sb.append("—");
            }
        }
        if (len / 2 != 0) {
            sb.append("—");
        }
        return sb.toString();
    }

    public static String line() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("—");
        }
        return sb.toString();
    }
// --------------------------------------------------------日期操作--------------------------------------------

    /**
     * 根据传入的类型返回SimpleDateFormat对象
     *
     * @param dateType
     * @return
     */
    public static SimpleDateFormat getDateFormat(String... dateType) {
        String type = dateType.length == 0 ? "yyyy-MM-dd" : dateType[0];
        return new SimpleDateFormat(type);
    }

    /**
     * 返回当前时间字符串  默认格式yyyy-MM-dd
     *
     * @param dateType 要返回的格式  <span style="color:red">yyyy-MM-dd HH:mm:ss</span>
     * @return 返回当前时间字符串
     */
    public static String getNowDate(String... dateType) {
        return getDateFormat(dateType).format(new Date());
    }

    /**
     * 时间转字符串  默认格式yyyy-MM-dd
     *
     * @param data     时间
     * @param dateType 要转的类型  <span style="color:red">yyyy-MM-dd HH:mm:ss</span>
     * @return 相对应的字符串
     */
    public static String dateToStr(Date data, String... dateType) {
        return getDateFormat(dateType).format(data);
    }
    public static String localDateToStr(LocalDateTime date, String... dateType) {
        DateTimeFormatter df;
        df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String localTime = df.format(date);
        return localTime;
    }

    /**
     * 字符串转日期   默认格式yyyy-MM-dd
     *
     * @param str      时间字符串
     * @param dateType 要转的类型  <span style="color:red">yyyy-MM-dd HH:mm:ss</span>
     * @return 转成的date
     */
    public static Date strToDate(String str, String... dateType) {
        Date date = null;
        try {
            date = getDateFormat(dateType).parse(str);
        } catch (Exception e) {
            System.out.println("时间转字符串出错" + e.toString());
        }
        return date;
    }
    public static LocalDateTime strToLocalDateTime(String str, String... dateType) {
        if (isEmpty(str)) {
            return LocalDateTime.now();
        }
        String type = dateType.length == 0 ? "yyyy-MM-dd" : dateType[0];
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDate.parse(str, DateTimeFormatter.ofPattern(type)).atStartOfDay();
        } catch (Exception e) {
            type = "yyyy/MM/dd";
            try {
                localDateTime = LocalDate.parse(str, DateTimeFormatter.ofPattern(type)).atStartOfDay();
            } catch (Exception ex) {
                type = "yyyy-M-dd";
                try {
                    localDateTime = LocalDate.parse(str, DateTimeFormatter.ofPattern(type)).atStartOfDay();
                } catch (Exception exception) {
                    type = "yyyy/M/dd";
                    try {
                        localDateTime = LocalDate.parse(str, DateTimeFormatter.ofPattern(type)).atStartOfDay();
                    } catch (Exception e1) {
                        type = "yyyyMMdd";
                        localDateTime = LocalDate.parse(str, DateTimeFormatter.ofPattern(type)).atStartOfDay();
                    }
                }
            }
        }
        return localDateTime;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间  格式yyyy-MM-dd
     * @param bdate  较大的时间  格式yyyy-MM-dd
     * @return 相差天数
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = getDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            System.out.println("计算日期天数出错===" + e.toString());
        }
        return 0;
    }

    /**
     * 两个日期相减得到天数小时分钟
     *
     * @param date1
     * @param date2
     * @return
     */
    public static String dateXj(String date1, String date2) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);
            return "" + days + "天" + hours + "小时" + minutes + "分";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "计算出错";
    }

    /***
     * 给日期增加多少天
     *
     * @param calDate
     * @param addDate 类型必须是long
     * @return
     */
    public static String addCalendarDay(Date calDate, long addDate) {
        long time = calDate.getTime();
        addDate = addDate * 24 * 60 * 60 * 1000;
        time += addDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date(time));
    }

    /**
     * 给日期加几个月 然后当前日减去一
     *
     * @param calDate
     * @param month
     * @return
     */
    public static String addCalendarMonth(Date calDate, int month) {
        Format f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, month);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return f.format(c.getTime());
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return 星期
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获得相对当前日期的前几天或者后几天
     *
     * @param day      天数,正数和负数
     * @param dateType 转换格式
     * @return 日期
     */
    public static String otherDay(String type, int num, String dateType) {
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        if ("d".equals(type)) {
            calendar.add(Calendar.DATE, num);    //负数是前几天,正数是后几天
        } else if ("m".equals(type)) {
            calendar.add(Calendar.MONTH, num);
        } else if ("y".equals(type)) {
            calendar.add(Calendar.YEAR, num);
        } else {
            calendar.add(Calendar.DATE, num);
        }
        String otherDay = new SimpleDateFormat(dateType).format(calendar.getTime());
        return otherDay;
    }

    public static String otherDay(String type, int num) {
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        if ("d".equals(type)) {
            calendar.add(Calendar.DATE, num);    //负数是前几天,正数是后几天
        } else if ("m".equals(type)) {
            calendar.add(Calendar.MONTH, num);
        } else if ("y".equals(type)) {
            calendar.add(Calendar.YEAR, num);
        } else {
            calendar.add(Calendar.DATE, num);
        }
        String otherDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
        return otherDay;
    }

    /**
     * 根据输入的时期,获得前几天或者后几天的日期
     *
     * @param type        d-天 m-月 y-年
     * @param currentDate
     * @param num
     * @param dateType
     * @return
     */
    public static String otherDay(long currentDate, String type, int num, String dateType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDate);
        if ("d".equals(type)) {
            calendar.add(Calendar.DATE, num);    //负数是前几天,正数是后几天
        } else if ("m".equals(type)) {
            calendar.add(Calendar.MONTH, num);
        } else if ("y".equals(type)) {
            calendar.add(Calendar.YEAR, num);
        } else {
            calendar.add(Calendar.DATE, num);
        }
        String otherDay = null;
        if (isNotEmpty(dateType)) {
            otherDay = new SimpleDateFormat(dateType).format(calendar.getTime());
        } else {
            otherDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
        }
        return otherDay;
    }

    public static String otherDay(long currentDate, String type, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDate);
        if ("d".equals(type)) {
            calendar.add(Calendar.DATE, num);    //负数是前几天,正数是后几天
        } else if ("m".equals(type)) {
            calendar.add(Calendar.MONTH, num);
        } else if ("y".equals(type)) {
            calendar.add(Calendar.YEAR, num);
        } else {
            calendar.add(Calendar.DATE, num);
        }
        String otherDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
        return otherDay;
    }

    /**
     * 获取 timestamp
     * */
    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
    }

// --------------------------------------------------------session操作-----------------------

    /**
     * 遍历session
     *
     * @param session 结果
     */
    public static String sessionList(HttpSession session) {
        Enumeration<String> enumeration = session.getAttributeNames();
        StringBuilder sb = new StringBuilder();
        String key;
        while (enumeration.hasMoreElements()) {
            key = enumeration.nextElement();
            if (key == null) continue;
            sb.append("[");
            sb.append(key);
            sb.append(" : ");
            sb.append(session.getAttribute(key));
            sb.append("] ");
        }
        return sb.toString();
    }

    public static String cookieList(Cookie[] cookies) {
        StringBuilder sb = new StringBuilder();
        if (cookies != null && cookies.length > 0) {
            for (Cookie x : cookies) {
                sb.append("[");
                sb.append(x.getName());
                sb.append(" : ");
                sb.append(x.getValue());
                sb.append("]");
            }
        } else {
            sb.append("No Cookie");
        }
        return sb.toString();
    }
    //------------------------------------------------正则操作---------------------------
    /**
     * 手机号
     */
    public static final String PHONE = "^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$";

    /**
     * 身份证
     */
    public static final String IDCARE = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";

    /**
     * 邮箱
     */
    public static final String EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 是否是字母
     */
    public static final String ISENGLISH = "[a-zA-Z]+";

    /**
     * 空格
     */
    public static final String TRIM = "\\s*|\t|\r|\n";

    /**
     * 只能是英文字母和数字
     */
    public static final String ENGLISHNUM = "^[0-9a-zA-Z]+$";

    /**
     * 校验手机号
     *
     * @param phone 手机号
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPhone(String phone) {
        return Pattern.matches(PHONE, phone);
    }

    /**
     * 校验身份证
     *
     * @param phone 身份证号码
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIdCard(String phone) {
        return Pattern.matches(IDCARE, phone);
    }

    /**
     * 校验邮箱
     *
     * @param email 邮箱
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(EMAIL, email);
    }

    /**
     * 是否是字母
     *
     * @param str 字符串
     * @return 是true，不是false
     */
    public static boolean isEnglish(String str) {
        return Pattern.matches(ISENGLISH, str);
    }

    /**
     * 只能是英文字母和数字
     *
     * @param str
     * @return
     */
    public static boolean isEnglishNum(String str) {
        return Pattern.matches(ENGLISHNUM, str);
    }

    /**
     *
     * 过滤 url
     * */
    public static boolean ignoreUrl(String url, String[] ignoreArr) {
        String[] urlArr = url.split("/"), s;
        if (urlArr.length == 0) {
            return false;
        }
        String s2;
        boolean b;
        for (String value : ignoreArr) {
            b = true;
            s = value.split("/");
            for (int j = 0; j < urlArr.length; j++) {
                s2 = urlArr[j];
                if (j <= s.length) {
                    if (!"*".equals(s[j])) {
                        if (!s2.equalsIgnoreCase(s[j])) {
                            if (!"**".equals(s[j])) {
                                b = false;
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }
                if (b && (j == s.length - 1 || j == urlArr.length - 1)) {
                    break;
                }
            }
            if (b) {
                return true;
            }
        }
        return false;
    }
    // --------------------------------------------------文件操作---------------------------

    /**
     * 判断文件是否存在，不存在就创建
     *
     * @param str 路径
     * @return true or false
     */
    public static boolean bFolder(String str) {
        File dest = new File(str);
        if (!dest.getParentFile().exists()) {
            return dest.getParentFile().mkdirs();
        }
        return true;
    }

    /**
     * 读取文件
     *
     * @param filename
     * @return
     */
    public static String readFile(String filename) {
        if (!isExist(filename)) {
            return "文件不存在";
        }
        StringBuffer buffer = new StringBuffer();
        FileInputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            inputStream = new FileInputStream(new File(filename));
            isr = new InputStreamReader(inputStream, "utf-8");
            reader = new BufferedReader(isr);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                buffer.append(tempString + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 写入文件
     *
     * @param content
     * @param filename
     */
    public static void writerFileByLine(String content, String filename) {
        File file = new File(filename);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 下载txt文件内容是传过来的string
     *
     * @param response response
     * @param txt      要下载到txt的内容
     */
    public static void uploadText(HttpServletResponse response, String txt) {
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + new Date().getTime() + ".txt");
            response.setContentType("text/plain;charset=utf-8");
            os.write(txt.getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param path      路径不带名字
     * @param fileNames 所有的文件名字
     */
    public static void deleteFile(String path, String... fileNames) {
        if (fileNames.length > 0) {
            File file = null;
            for (String fileName : fileNames) {
                file = new File(path + File.separator + fileName);
                file.delete();
            }
        }
    }
    // --------------------------------------------数据库类操作-----------------------------------

    /**
     * 设置分页
     * MySql limit pageIndex,pageSIze
     * pageIndex-第几条记录开始 pageSize 查几个
     * MySql 是从 0 开始起
     *
     * @param map 参数
     */
    public static void setPage(Map<String, Object> map) {

        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        if (null != pageIndex && null != pageSize) {
            if (pageIndex > 0 && pageIndex != 1) {
                pageIndex -= 1;
                if (pageSize > 0 && pageSize <= 50) {
                    pageIndex *= pageSize;
                } else {
                    pageSize = 10;
                }
                map.put("pageIndex", pageIndex);
                map.put("pageSize", pageSize);
            } else {
                pageIndex = 0;
                map.put("pageIndex", pageIndex);
                map.put("pageSize", pageSize);
            }
        } else {
            map.put("pageIndex", 0);
            map.put("pageSize", 10);
        }
    }

    /**
     * 连接数据库
     *
     * @param dbName   数据库名
     * @param name     用户名
     * @param password 密码
     * @return 返回connection
     */
    public static Connection openDB(String url,String dbName, String name, String password) {
//        String url = "jdbc:mysql://localhost:3306/" + dbName + "?characterEncoding=UTF-8&serverTimezone=GMT%2B8";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log("加载驱动失败");
            e.printStackTrace();
        }
        try {
            return DriverManager.getConnection(url, name, password);
        } catch (SQLException e) {
            log("连接数据库失败");
            e.printStackTrace();
        }
        return null;
    }

    public static void closeDB(Connection con, PreparedStatement ps) {
        if (null != con) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (null != ps) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // -------------------------------------------输出类操作------------------------------------

    /**
     * 数据输出
     */
    public static void log(Object... o) {
        int num = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String clazzName = Thread.currentThread().getStackTrace()[2].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String line = "\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014" +
                "\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014" +
                "\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014" +
                "\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014" +
                "\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014" +
                "\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014" +
                "\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014" +
                "\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014YHT\u2014\u2014\u2014\u2014";
        StringBuilder url = new StringBuilder();
        url.append("(");
        url.append(clazzName);
        url.append(".");
        url.append(methodName);
        url.append(" : ");
        url.append(num);
        url.append(")");
        StringBuilder out = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        out.append(logLine(url.toString()));
        out.append(recursionAddSpace("", ""));
        int len = o.length;
        int index = 1;
        for (Object x : o) {
            if (len == 1) {
                if (x == null) {
                    out.append(recursionAddSpace("", "NullPointerException"));
                } else {
                    out.append(recursionAddSpace("", x.toString()));
                }
            } else {
                sb.append("<");
                sb.append(index);
                sb.append(">");
                out.append(logSpace(sb.toString()));
                if (x == null) {
                    out.append(recursionAddSpace("", "NullPointerException"));
                } else {
                    out.append(recursionAddSpace("", x.toString()));
                }
                out.append(recursionAddSpace("", ""));
                sb.delete(0, sb.length());
            }
            index++;
        }
        out.append(recursionAddSpace("", ""));
        out.append(line);
        System.out.println(out);
    }

    public static void log() {
        log("啊哈，没有可输出的！");
    }

    /**
     * 输出的结果像这样  ——————className——————
     *
     * @param str className
     * @return ——————className——————
     */
    public static String logLine(String str) {
        StringBuilder preS = new StringBuilder();
        double b = ((double) (100 - str.length()) / 2);
        int l = (int) b;
        boolean bRight = b > (double) l ? true : false;
        for (int i = 0; i < l; i++) {
            preS.append("—");
        }
        preS.append(str);
        for (int i = 0; i < l; i++) {
            preS.append("—");
        }
        if (bRight) preS.append("—");
        preS.append("\r\n");
        return preS.toString();
    }

    /**
     * 使用递归判断是否回车换行
     *
     * @param str 内容
     * @return 字符串
     */
    private static String recursionAddSpace(String cur, String next) {
        StringBuilder current = new StringBuilder("|  ");
        int len = next.length();
        if (len <= 98) {
            int forI = 100 - 6 - len;
            current.append(next);
            for (int i = 0; i < forI; i++) {
                current.append(" ");
            }
            current.append("  |\r\n");
            current.insert(0, cur);
            return current.toString();
        } else {
            current.append(next.substring(0, 94));
            current.append("  |\r\n");
            current.insert(0, cur);
            String nextContext = next.substring(94, next.length());

            return recursionAddSpace(current.toString(), nextContext);
        }
    }

    /**
     * 添加空格
     */
    private static String logSpace(String str) {
        StringBuilder sb = new StringBuilder("|");
        sb.append(str);
        int len = str.length();
        int forI = 100 - 2 - len;
        for (int i = 0; i < forI; i++) {
            sb.append(" ");
        }
        sb.append("|\r\n");
        return sb.toString();
    }
    /* ----------------------------------------- 数值操作 ------------------------------- */

    /**
     * 有效位取值
     */
    public static String numFormat(String format, Object o) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(o);
    }

}

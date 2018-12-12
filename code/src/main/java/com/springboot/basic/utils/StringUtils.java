package com.springboot.basic.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	public static final String patternUpper = "[A-Z]+";
	public static final String patternLower = "[a-z]+";
	public static final String patternDigit = "[0-9]+";
	public static final String patternPunct = "\\p{Punct}+";
	public static final String BLANK_SPACE = " ";

	@SuppressWarnings("rawtypes")
	public static boolean inCollection(String key, Collection<String> entitys) {
		boolean retval = Boolean.FALSE.booleanValue();
		if (!isEmpty(trim(key)) && entitys != null && !entitys.isEmpty()) {
			Iterator arg2 = entitys.iterator();

			while (arg2.hasNext()) {
				String object = (String) arg2.next();
				if (key.equals(object)) {
					retval = Boolean.TRUE.booleanValue();
				}
			}
		}

		return retval;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean inCollection(String key, String... entitys) {
		return inCollection(key, (Collection) Arrays.asList(entitys));
	}

	public static boolean isEmpty(CharSequence str) {
		return org.apache.commons.lang3.StringUtils.isEmpty(str) || "null".equals(str);
	}

	public static Boolean isEmptyofTrim(String str) {
		return Boolean.valueOf(isBlank(trim(str)));
	}

	public static Boolean checkPassword(String password) {
		Boolean retval = Boolean.FALSE;
		if (null != password && password.length() >= 8) {
			if (matcherUpper(password).booleanValue() && matcherLower(password).booleanValue()
					&& (matcherDigit(password).booleanValue() || matcherPunct(password).booleanValue())) {
				retval = Boolean.TRUE;
			}

			return retval;
		} else {
			return retval;
		}
	}

	public static String random(Integer number) {
		Double random = Double.valueOf(Math.random() * (double) System.currentTimeMillis());
		return number != null && number.intValue() > 0
				? String.valueOf(random).substring(2, 2 + number.intValue())
				: String.valueOf(random).substring(2, 12);
	}

	public static String random() {
		Double random = Double.valueOf(Math.random() * (double) System.currentTimeMillis());
		return String.valueOf(random);
	}

	public static Boolean matcher(String pattern, String str) {
		Boolean retval = Boolean.FALSE;
		if (!isEmptyofTrim(pattern).booleanValue() && !isEmptyofTrim(str).booleanValue()) {
			Pattern temp = Pattern.compile(pattern);
			Matcher matcher = null;
			if (temp != null) {
				matcher = temp.matcher(str);
			}

			if (matcher != null) {
				retval = Boolean.valueOf(matcher.find(0));
			}

			return retval;
		} else {
			return retval;
		}
	}

	public static Boolean matcherUpper(String str) {
		return matcher("[A-Z]+", str);
	}

	public static Boolean matcherLower(String str) {
		return matcher("[a-z]+", str);
	}

	public static Boolean matcherDigit(String str) {
		return matcher("[0-9]+", str);
	}

	public static Boolean matcherPunct(String str) {
		return matcher("\\p{Punct}+", str);
	}

	public static String format(int num) {
		return format(8, num);
	}

	public static String format(int length, int num) {
		return String.format("%0" + length + "d", new Object[]{Integer.valueOf(num)});
	}

	public static String filter(String str) throws PatternSyntaxException {
      String regEx = "^[A-Za-z\\\\d\\一-\\龥\\\\p{P}‘’“”]+";
      StringBuilder builder = new StringBuilder(str);
      int len = str.length();

      for(int i = len - 1; i >= 0; --i) {
         if(!matcher(regEx, String.valueOf(str.charAt(i))).booleanValue()) {
            builder.deleteCharAt(i);
         }
      }

      return builder.toString();
   }

	public static String lastChat(String str, String chat) {
		int last = lastIndexOf(str, chat);
		return last != -1 && last != str.length() ? str + chat : str;
	}
}
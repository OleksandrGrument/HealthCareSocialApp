package com.comeonbaby.android.app.utils;

import java.util.Locale;


public class MathUtils {
	
	/**
	 * @author PvTai
	 * @Description Compare two double number
	 * 
	 * @param v1
	 *            first number
	 * @param v2
	 *            second number
	 * @return the positive number if v1 is greater than v2, negative value is
	 *         v1 is less than v2, and 0 if equal
	 */
	public static int compareDouble(double v1, double v2) {
		int real1 = (int) v1;
		int real2 = (int) v2;
		double remain1 = v1 - real1;
		double remain2 = v2 - real2;

		if (real1 != real2)
			return (real1 - real2);
		else {
			int temp1 = (int) (remain1 * ConstsCore.EPSILON);
			int temp2 = (int) (remain2 * ConstsCore.EPSILON);
			return (temp1 - temp2);
		}
	}
	
	/**
	 * @author PvTai
	 * @param num
	 * @param delta
	 * @return string number after round
	 */
	public static String roundNumber(double num, int delta) {
		String result = "0.00";
		long a = (long) (num * Math.pow(10, delta + 1));
		int du = (int) (a % 10);
		a = a / 10;

		double rs;
		if (du < 5)
			rs = a / Math.pow(10, delta);
		else
			rs = (a + 1) / Math.pow(10, delta);
		try {
			result = String.format(Locale.ENGLISH, "%." + delta + "f", rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}

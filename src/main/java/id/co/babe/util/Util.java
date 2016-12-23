package id.co.babe.util;

import java.util.List;

public class Util {

	public static void printList(List<?> data) {
		for(int i = 0 ; i < data.size() ; i ++) {
			System.out.println(data.get(i));
		}
	}
	
	public static String toString(double[] x) {
		String result = "[ ";
		for(int i = 0 ; i < x.length ; i ++) {
			result += " " + x[i] + " ";
		}
		result += " ] ";
		return result;
	}
} 

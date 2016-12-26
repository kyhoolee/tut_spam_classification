package id.co.babe.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static boolean checkNan(double[] x) {
		for(int i = 0 ; i < x.length ; i ++) {
			if (Double.isNaN(x[i])) {
			    return true;
			}
		}
		
		return false;
	}
	
	public static int countSub(String input, String sub) {
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){

		    lastIndex = input.indexOf(sub,lastIndex);

		    if(lastIndex != -1){
		        count ++;
		        lastIndex += sub.length();
		    }
		}
		return count;
	}
	
	public static String filter(String input) {
		input = input.replace("\\n", " ");
		input = input.replace(",", " ");
		input = input.replace("\",\"", " ");
		return input;
	}
	
	public static String phoneNumber(String input) {
		String result = "";
		input = "serius hubungi hp/sms/wa: 0821 8788 1779 pin bb: 5f4665db";
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(input);
		while (m.find()) {
		  System.out.println(m.group());
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		phoneNumber("");
	}
} 

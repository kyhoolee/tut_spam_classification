package id.co.babe.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.co.babe.filter.model.Komen;

public class RuleFilter {
	
	public static int rule(String input) {
		if( (uppercaseRule(input) > 0.1 && lengthRule(input) > 5 && specialWordRule(input) > 1)  
				|| (singlecharacterRule(input) > 0.3 && lengthRule(input) > 10)
				) {
			return Komen.SPAM;
		}
		
		return Komen.NORMAL;
	}
	
	public static void printRule(String input) {
		String sep = "    --    ";
		System.out.println(
				uppercaseRule(input) + sep
				+ lengthRule(input) + sep
				+ specialWordRule(input) + sep 
				+ singlecharacterRule(input) + sep 
				+ input
				);
	}
	
	public static double uppercaseRule(String input) {
		
		if(input  == null || input.length() == 0)
			return 0;
		
		int upperCase = 0;
		for (int k = 0; k < input.length(); k++) {
		    if (Character.isUpperCase(input.charAt(k))) upperCase++;
		}
		
		double check = (double) upperCase * 1.0 / input.length();
		return check;
	}
	
	public static double singlecharacterRule(String input) {
		if(input  == null || input.length() == 0)
			return 0;
		
		String[] words = input.toLowerCase().split(" ");
		int count = 0;
				
		for(int i = 0 ; i < words.length ; i ++) {
			if(words[i].length() == 1) 
				count ++;
		}
		
		double check = (double) count * 1.0 / words.length;
		
		return check;
	}
	
	
	public static double lengthRule(String input) {
		String[] split = input.split(" ");
		
		return split.length;
	}
	
	public static int specialWordRule(String input) {
		if(input  == null || input.length() == 0)
			return 0;
		
		String[] words = input.toLowerCase().split(" ");
		int count = 0;
				
		boolean nc = false;
		boolean ns = false;
		boolean cs = false;
		for(int i = 0 ; i < words.length ; i ++) {
			boolean n = words[i].matches(".*\\d+.*");
			boolean c = words[i].matches(".*[a-zA-Z]+.*");
			
//			Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
//			Matcher m = p.matcher("I am a string");
//			boolean s = m.find();
			boolean s = words[i].matches(".*[!@#$%^&*]+.*");
			
			if((n&&c) || (n&&s) || (c&&s)) // 
				count ++;
			if(n&&c&&s)
				count += 10;
			
			if(n&&c) nc = true;
			if(n&&s) ns = true;
			if(c&&s) cs = true;
		}
		
		if((nc&&cs) || (ns&&cs) || (nc&&ns)) // 
			count += 10;
		
		
		return count;
	}
	
	public static void regexMatch(String input) {
		if(input.matches("/\\s+[Oo]\\s*[Bb]\\s*[Aa]\\*[Tt]\\s+/"))
			System.out.println(input);
		
		if(input.matches("/\\s+0[0-9]{2,}\\s*[0-9]{3,}/"))
			System.out.println(input);
		
		
		if(input.matches("/\\s+([0-9A-F]{2}\\s*){3,}/"))
			System.out.println(input);
		
		if(input.matches("/[0-9][ABCDEFabcdef]\\s*[ABCDEFabcdef]{2,}/"))
			System.out.println(input);
	}

}

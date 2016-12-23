package id.co.babe.filter.model;

public class Komen {
	public static final int SPAM = 0;
	public static final int NORMAL = 1;
	
	
	public String content;
	public int label;
	
	public Komen(String content, int label) {
		this.content = content;
		this.label = label;
	}
	
	

}

package id.co.babe.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.co.babe.classifier.Classifier;
import id.co.babe.classifier.bayes.BayesClassifier;
import id.co.babe.classifier.bayes.LogisticRegression;
import id.co.babe.classifier.bayes.LogisticRegression.Instance;
import id.co.babe.filter.model.Komen;
import id.co.babe.filter.model.KomenDataset;
import id.co.babe.util.Util;

public class SpamFilter {
	
	
	public static void main(String[] args) {
		ruleFilter();
		//logisticFilter();
	}
	
	public static final String ROOT = "/home/kyhoolee/tutorial/learn/projects/spam_filter/Spam-Classification/";
	private static KomenDataset buildData() {
		KomenDataset data = new KomenDataset(); 
		
		data.updateData(DataReader.readSpamKomens(ROOT + "neg_words.txt"), 1);
		data.updateData(DataReader.readSpamKomens(ROOT + "spam_output.txt.1"), 0.6);
		data.updateData(DataReader.readNormalKomens(ROOT + "pure_comments.txt.1"), 0.6);
		data.updateData(DataReader.readSpamKomens(ROOT + "pure_spam.txt.1"), 0.6);
		data.updateData(DataReader.readSpamKomens(ROOT + "pure_spam_1.txt.1"), 0.6);
		
		
		
		
		System.out.println("Train data: ");
		System.out.println(data.train_pos + " -- " + data.train_neg);
        
		System.out.println("\n\nTest data: ");
		System.out.println(data.test_pos + " -- " + data.test_neg);
		System.out.println("\n\n-------------------");
		
		return data;
	}
	
	private static double[] getFeatures(Komen k, BayesClassifier<String, Integer> bayes) {
		double[] input = new double[5];
		input[0] = bayes.classify(Arrays.asList(k.content.toLowerCase().split("\\s"))).getCategory();
		input[1] = 0.01 * RuleFilter.lengthRule(k.content);
		input[2] = RuleFilter.singlecharacterRule(k.content);
		input[3] = 0.01 * RuleFilter.specialWordRule(k.content);
		input[4] = RuleFilter.uppercaseRule(k.content);
		
//		if (Util.checkNan(input)) {
//		    System.out.println(Util.toString(input));
//		    System.out.println(k.label + " -- " + k.content);
//		    System.out.println("\n");
//		}
		
		return input;
	}
	
	public static void logisticFilter() {
		KomenDataset data = buildData();
		
		// Train the probability with naive-bayes
		BayesClassifier<String, Integer> bayes = new BayesClassifier<String, Integer>();
		for(int i = 0; i < data.train.size() ; i ++) {
			Komen k = data.train.get(i);
			bayes.learn(k.label, Arrays.asList(k.content.split("\\s")));
		}
		
		List<Instance> instances = new ArrayList<>();
		for(int i = 0 ; i < data.train.size() ; i ++) {
			Komen k = data.train.get(i);
			double[] input = getFeatures(k, bayes);
			Instance inst = new Instance(k.label, input);
			instances.add(inst);
			System.out.println(inst);
		}
		
		LogisticRegression logistic = new LogisticRegression(5);
		logistic.train(instances);
		
		
		
		// Test the model
		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;
		
		List<String> falseNegList = new ArrayList<>();
		
		for(int i = 0 ; i < data.test.size() ; i ++) {
			Komen k = data.test.get(i);
			double[] input = getFeatures(k, bayes);
			double prob_normal = logistic.classify(input);
			int res = Komen.NORMAL;
			if(prob_normal < 0.5)
				res = Komen.SPAM;
					
			if(k.label == Komen.SPAM && res == Komen.NORMAL) {
				//System.out.println(res + " -- " + k.label + "   --  " + k.content);
				//System.out.println(k.content);
				//RuleFilter.printRule(k.content);
			}
			
			if(k.label == Komen.NORMAL && res == Komen.SPAM) {
				//falseNegList.add(k.content);
			}
			
			if(k.label == Komen.NORMAL) {
				if(res == Komen.NORMAL) {
					true_neg ++;
				} else {
					false_neg ++;
				}
			} else {
				if(res == Komen.SPAM) {
					true_pos ++;
				} else {
					false_pos ++;
				}
			}
			
		}
		
		System.out.println("\n\n");
		System.out.println("False_pos: " + false_pos + " -- Total_pos: " + (false_pos + true_pos));
		System.out.println("False_neg: " + false_neg + " -- Total_neg: " + (false_neg + true_neg));
		
		TextfileIO.writeFile(ROOT + "false_negative.txt", falseNegList);
		
		double precision = true_pos * 1.0 / (true_pos + false_pos);
		double recall = true_pos * 1.0 / (false_neg + true_pos);
		System.out.println("Precision: " + precision + " -- Recall: " + recall);
		
		
	}
	
	public static void ruleFilter() {
		KomenDataset data = buildData();
		
		// Train the probability with naive-bayes
		BayesClassifier<String, Integer> bayes = new BayesClassifier<String, Integer>();
		for(int i = 0; i < data.train.size() ; i ++) {
			Komen k = data.train.get(i);
			bayes.learn(k.label, Arrays.asList(k.content.toLowerCase().split("\\s")));
		}
		
		// Test the model
		
		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;
		
		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();
		
		for(int i = 0 ; i < data.test.size() ; i ++) {
			Komen k = data.test.get(i);
			int res = bayes.classify(Arrays.asList(k.content.toLowerCase().split("\\s"))).getCategory();
			k.content = Util.filter(k.content);
			if(res == Komen.NORMAL) {
				res = RuleFilter.ruleSpam(k.content);
			} else { //if(res == Komen.SPAM) 
				res = RuleFilter.ruleNormal(k.content);
			}
			if(k.label == Komen.SPAM && res == Komen.NORMAL) {
				falsePosList.add(k.content);//RuleFilter.printRule(k.content).toLowerCase());//
			}
			
			if(k.label == Komen.NORMAL && res == Komen.SPAM) {
				falseNegList.add(RuleFilter.printRule(k.content)); //k.content);//
			}
			
			if(k.label == Komen.NORMAL) {
				if(res == Komen.NORMAL) {
					true_neg ++;
				} else {
					false_neg ++;
				}
			} else {
				if(res == Komen.SPAM) {
					true_pos ++;
				} else {
					false_pos ++;
				}
			}
			
		}
		
		System.out.println("\n\n");
		System.out.println("False_pos: " + false_pos + " -- Total_pos: " + (false_pos + true_pos));
		System.out.println("False_neg: " + false_neg + " -- Total_neg: " + (false_neg + true_neg));
		
		TextfileIO.writeFile(ROOT + "false_negative.txt", falseNegList);
		TextfileIO.writeFile(ROOT + "false_positive.txt", falsePosList);
		
		double precision = true_pos * 1.0 / (true_pos + false_pos);
		double recall = true_pos * 1.0 / (false_neg + true_pos);
		System.out.println("Precision: " + precision + " -- Recall: " + recall);
		
		double f_score = 2 * precision * recall / (precision + recall);
		System.out.println("F-Score: " + f_score);


	}

}

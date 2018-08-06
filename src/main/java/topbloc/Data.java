package topbloc;

import java.util.ArrayList;

public class Data {
	private ArrayList<Double> numSet1;
	private ArrayList <Double> numSet2;
	private ArrayList<String> wordSet;
	
	public Data(ArrayList<Double> numSet1, ArrayList<Double> numSet2, ArrayList<String> wordSet) {
		this.numSet1 = numSet1;
		this.numSet2 = numSet2;
		this.wordSet = wordSet;
	}

	public ArrayList<Double> getNumSet1() {
		return numSet1;
	}

	

	public ArrayList <Double> getNumSet2() {
		return numSet2;
	}

	

	public ArrayList<String> getWordSet() {
		System.out.println(wordSet);
		return wordSet;
	}

	
	
	
}

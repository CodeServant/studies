package utp.cls3.zad1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

// zobaczyć jeszcze czy kolejności są odpowiednie
public class Anagrams {
	public List<String> words;
	
	protected RuleBasedCollator collator;
	
	public Anagrams(String wordsFile) {
		
		words = new ArrayList<String>();
		try {
			BufferedReader rdr = new BufferedReader(new FileReader(wordsFile));
			String toSplit;
			while((toSplit = rdr.readLine()) != null) {
				addLine(toSplit);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			collator = new RuleBasedCollator("< a, A < ą, Ą < b, B < c, C < ć, Ć < d, D < e, E < ę, Ę < f, F < g, G < h, H < i, I < j, J < k, K < l, L < ł, Ł < m, M < n, N < ń, Ń < o, O < ó, Ó < p, P < q, Q < r, R < s, S < ś, Ś < t, T < u, U < v, V < w, W < x, X < y, Y < z, Z < ź, Ź < ż, Ż");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public List<List<String>> getSortedByAnQty(){
		SortedSet<List<String>> set = new TreeSet<>((o1, o2) -> {
			if(o1.size() ==  o2.size()) return collator.compare(o1.get(0), o2.get(0));
			else
				return o2.size()-o1.size();
		});
		for(int i = 0; i<words.size(); i++)
			set.add(anagramsOf(this.words.get(i)));
		ArrayList<List<String>> list = new ArrayList<>(set);
		return list;
	}
	
	private List<String> anagramsOf(String word){
		return words.stream().filter((s) -> testAnagram(word, s)).sorted(collator).collect(Collectors.toList());
	}
	
	public String getAnagramsFor(String word) {
		return word + ": " + anagramsOf(word).stream().filter((s) -> !s.equals(word)).collect(Collectors.toList()).toString();
		
	}
	
	private void addLine(String line) {
		for(String word : line.split("\s+"))
			words.add(word);
	}
	private boolean testAnagram(String t1, String t2) {
		String t1Sorted = this.sortLetters(t1),
				t2Sorted = this.sortLetters(t2);
		return t1Sorted.compareTo(t2Sorted) == 0;
	}
	private String sortLetters(String word) {
		String[] znaki = new String[word.length()]; 
		for(int i=0; i<znaki.length; i++) {
			znaki[i]=String.valueOf(word.charAt(i));
		}
		Arrays.sort(znaki, this.collator);
		StringBuilder bd = new StringBuilder();
		for(int i=0; i<znaki.length; i++) {
			bd.append(znaki[i]);
		}
		return bd.toString();
	}
}  

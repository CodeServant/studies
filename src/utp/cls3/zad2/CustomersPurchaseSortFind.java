package utp.cls3.zad2;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

public class CustomersPurchaseSortFind {
	protected List<Purchase> purchases = new ArrayList<>();
	
	public void readFile(String fileName){
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = bfr.readLine()) != null)
				purchases.add(parsePurchase(line));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void showSortedBy(String item) {
		SortedMap<String, Comparator<Purchase>> comparators = new TreeMap<>();
		comparators.put("Nazwiska", (p1, p2) -> {
			int ret = p1.getNazwisko().compareTo(p2.getNazwisko());
			if(ret == 0) ret = p1.getIdKlienta().compareTo(p2.getIdKlienta());
			return ret;
		});
		comparators.put("Koszty", (p1, p2) -> {
			int ret = -p1.getKoszt().compareTo(p2.getKoszt());
			if(ret == 0) ret = p1.getIdKlienta().compareTo(p2.getIdKlienta());
			return ret;
		});
		System.out.println(item);
		Comparator<Purchase> comp = comparators.get(item);
		purchases.stream().sorted(comp).forEach((p) -> {
			System.out.println(p + " (koszt: " + p.getKoszt() + ")");
		});
		
	}
	public void showPurchaseFor(String id) {
		SortedMap<String, Predicate<Purchase>> predicates = new TreeMap<>();
		System.out.println("Klient "+id);
		Predicate<Purchase> pred = (p) -> p.getIdKlienta().compareTo(id)==0;
		purchases.stream().filter(pred).forEach(System.out::println);
	}
	private Purchase parsePurchase(String r) {
		String[] record = r.split(";");
		double cena = Double.parseDouble(record[3]);
		double ilosc = Double.parseDouble(record[4]);
		int idC = Integer.valueOf(record[0].replaceAll("c", ""));
		return new Purchase(idC, record[1].split(" ")[0], record[1].split(" ")[1], record[2], cena, ilosc);
	}
}

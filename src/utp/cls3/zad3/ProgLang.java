package utp.cls3.zad3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProgLang {
	protected List<List<String>> programisci;
	protected List<String> jezyki;

	public ProgLang(String path) {
		this.programisci = new ArrayList<>();
		this.jezyki = new ArrayList<>();
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(path));
			String line;
			while ((line = bfr.readLine()) != null) {
				this.parseNewLanguage(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public Map<Integer, Integer> testMap(Map<Integer, Integer> mapa, Comparator<Map.Entry<Integer,Integer>> comp){
		return sorted(mapa, comp);
	}
	public Map<String, Set<String>> getLangsMap() {
		Map<String, Set<String>> mapa = new LinkedHashMap<>();
		for (int i = 0; i < jezyki.size(); i++) {
			Set<String> set = new LinkedHashSet<>();
			set.addAll(programisci.get(i));
			mapa.put(jezyki.get(i), set);
		}
		return mapa;
	}

	public Map<String, Set<String>> getProgsMap() {
		Map<String, Set<String>> mapa = new LinkedHashMap<>();

		for (int i = 0; i < jezyki.size(); i++) {

			String jezyk = jezyki.get(i);
			for (int j = 0; j < this.programisci.get(i).size(); j++) {
				String programista = this.programisci.get(i).get(j);

				Set<String> set = mapa.get(programista);
				if (set == null)
					set = new LinkedHashSet<>();
				set.add(jezyk);
				mapa.put(programista, set);
			}
		}
		return mapa;
	}

	public Map<String, Set<String>> getLangsMapSortedByNumOfProgs() {
		Comparator<Map.Entry<String, Set<String>>> comparator = (e1, e2) -> {
			int compared = e2.getValue().size() - e1.getValue().size();
			if (compared == 0)
				compared = e1.getKey().compareTo(e2.getKey());
			return compared;
		};
		return this.sorted(this.getLangsMap(), comparator);
	}

	public Map<String, Set<String>> getProgsMapSortedByNumOfLangs() {
		Comparator<Map.Entry<String, Set<String>>> comparator = (e1, e2) -> {
			int compared = e2.getValue().size() - e1.getValue().size();
			if (compared == 0)
				compared = e1.getKey().compareTo(e2.getKey());
			return compared;
		};
		return this.sorted(this.getProgsMap(), comparator);
	}

	public Map<String, Set<String>> getProgsMapForNumOfLangsGreaterThan(int n) {
		Predicate<Map.Entry<String, Set<String>>> predicate = (e) -> {
			return e.getValue().size() > n;
		};
		return this.filtered(this.getProgsMap(), predicate);
	}

	private static <K, V> Map<K, V> sorted(Map<K, V> mapa,
			Comparator<Map.Entry<K, V>> comparator) {

		LinkedHashSet<Map.Entry<K, V>> entrySet = new LinkedHashSet<>(
				mapa.entrySet().stream().sorted(comparator).collect(Collectors.toList()));
		LinkedHashMap<K, V> newMap = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : entrySet)
			newMap.put(entry.getKey(), entry.getValue());
		return newMap;
	}

	private static <K, V> Map<K, V> filtered(Map<K, V> mapa,
			Predicate<Map.Entry<K, V>> predicate) {
		LinkedHashSet<Map.Entry<K, V>> entrySet = new LinkedHashSet<>(
				mapa.entrySet().stream().filter(predicate).collect(Collectors.toList()));
		LinkedHashMap<K, V> newMap = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : entrySet)
			newMap.put(entry.getKey(), entry.getValue());
		return newMap;
	}

	private void parseNewLanguage(String lang) {
		String[] segments = lang.split("\t");
		this.jezyki.add(segments[0]);
		ArrayList<String> progList = new ArrayList<>();
		for (int i = 1; i < segments.length; i++) {
			progList.add(segments[i]);
		}
		this.programisci.add(progList);

	}
}

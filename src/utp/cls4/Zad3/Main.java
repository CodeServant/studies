package utp.cls4.Zad3;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) {
		try {
			Map<String, List<String>> maps = new BufferedReader(
					new InputStreamReader(new URL("http://wiki.puzzlers.org/pub/wordlists/unixdict.txt").openStream())).lines().collect(Collectors.groupingBy(w -> {char[] crs = w.toCharArray(); Arrays.sort(crs); return String.valueOf(crs);}));
			maps.values().stream().filter(l -> l.size() == maps.values().stream().max((a, b) -> a.size() - b.size()).get().size()).sorted((a, b) -> a.get(0).compareTo(b.get(0)))
			.forEachOrdered(
					l -> {String last = l.get(l.size()-1);
						l.forEach(ll -> System.out.print(ll+(ll==last?"":" ")));
					System.out.println();
					});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

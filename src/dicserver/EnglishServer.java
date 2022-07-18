package dicserver;

import java.util.TreeMap;
import java.util.TreeSet;

import dicserver.langServer.*;

public class EnglishServer {

	public static void main(String[] args) {
		System.out.println("english server");
		try {
			LangServer langServ = new LangServer("en", 51152);
			TreeMap<String, String> dict = new TreeMap<>();
			dict.put("samochód", "car");
			dict.put("sufit", "roof");
			langServ.getTranslator().setDictionary(dict);
			langServ.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

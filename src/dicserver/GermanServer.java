package dicserver;

import java.util.TreeMap;

import dicserver.langServer.LangServer;

public class GermanServer {
	public static void main(String[] args) {
		System.out.println("german server");
		try {
			LangServer langServ = new LangServer("de", 51155);
			TreeMap<String, String> dict = new TreeMap<>();
			dict.put("samochód", "wagen");
			dict.put("sufit", "decke");
			dict.put("glupek", "dumm");
			dict.put("sklep", "laden");
			dict.put("żona", "ehefrau");
			langServ.getTranslator().setDictionary(dict);
			langServ.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

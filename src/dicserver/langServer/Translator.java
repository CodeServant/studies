package dicserver.langServer;

import java.util.TreeMap;

public class Translator {
	String langCode;

	/**
	 * <code><PolishWord, ForeignWord></code>
	 */
	TreeMap<String, String> dictionary;

	public Translator(String langCode) throws Exception {
		if (!langCode.matches("\\w{2}"))
			throw new Exception("langCode have to be 2 letters");
		this.langCode = langCode;
		dictionary = new TreeMap<>();
	}

	/**
	 * Returns the translation of a phrase. Null if not found.
	 * 
	 * @param phrase
	 * @return
	 */
	public String translate(String phrase) {
		return dictionary.get(phrase);
	}

	public void setDictionary(TreeMap<String, String> disct) {
		this.dictionary = disct;
	}
}

package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

/**
 * Moze byc przedmiotem sprzedazy badz przechowywac na sobie nieruchomosc.
 * 
 *
 */
public class Dzialka extends Nieruchomosc {
	private String rodzaj = "mieszkalna";
	private int powierzchnia;
	private static Set<Dzialka> dzialki = new HashSet<>();

	/**
	 * Domy jakie stoja na dzialce.
	 */
	private Set<Dom> domy = new HashSet<>();

	public Dzialka(BigDecimal cenaOfetowa, Lokalizacja lokalizacja, BigDecimal podatek, String rodzaj,
			int powierzchnia) {
		super(cenaOfetowa, lokalizacja, podatek);
		if (rodzaj != null)
			this.rodzaj = rodzaj;
		this.powierzchnia = powierzchnia;
		dzialki.add(this);
	}

	public Dzialka(Nieruchomosc nieruchomosc, String rodzaj, int powierzchnia) {
		this(nieruchomosc.getCenaOfetowa(), nieruchomosc.getLokalizacja(), nieruchomosc.getPodatek(), rodzaj,
				powierzchnia);
		nieruchomosc.aktualizujAsocjacje(this);
	}

	public static void usunDzialke(Dzialka dzialka) {
		dzialka.changeBehaviour();
		dzialki.remove(dzialka);
		dzialka.usunDomy();
	}

	public void remove() {
		usunDzialke(this);
	}

	private void usunDomy() {
		//domy.forEach(d -> d.remove());
		while(domy.iterator().hasNext()) {
			domy.iterator().next().remove();
		}
	}

	protected boolean addDom(Dom dom) throws Exception {
		if (dom == null)
			throw new NullPointerException();
		if (Dom.extenseContaint(dom))
			throw new Exception("obiekt nie może być współdzielony przez kilka działek");
		if (domy.contains(dom))
			return false;
		else {
			domy.add(dom);
			return true;
		}
	}

	public boolean delDom(Dom dom) {
		if (this.domy.contains(dom)) {
			this.domy.remove(dom);
			dom.remove();
			return true;
		}
		return false;
	}

	public List<Dom> getDomy() {
		return new ArrayList<Dom>(domy);
	}

	public static int liczbaDzialek() {
		return dzialki.size();
	}
	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(dzialki);
	}
	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		dzialki = (Set<Dzialka>) oStream.readObject();
	}

	public static List<Dzialka> filtrujDzialki(Predicate<Dzialka> pred) {
		return dzialki.stream().filter(pred).collect(Collectors.toList());
	}
}

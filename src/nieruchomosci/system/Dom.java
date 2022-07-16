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

import javax.print.attribute.HashAttributeSet;

/**
 * Przechowuje informacje o domach, ktore snajduja sie na {@link Dzialka}
 * 
 *
 */
public class Dom extends Zabudowa {
	private Dzialka dzialka;
	private int powierzchniaZabudowy;
	private List<String> media = new ArrayList<>();
	private static Set<Dom> domy = new HashSet<>();

	/**
	 * Zamienia obecna nieruchomosc na ten dom.
	 * 
	 * @param dzialka
	 * @param nieruchomosc
	 * @param metraz
	 * @param rokBudowy
	 * @param liczbaPieter
	 * @param pomieszczenia
	 * @param powierzchniaZabudowy
	 * @throws Exception
	 */
	private Dom(Dzialka dzialka, Nieruchomosc nieruchomosc, int metraz, int rokBudowy, int liczbaPieter,
			String[] pomieszczenia, int powierzchniaZabudowy) throws Exception {
		super(nieruchomosc, metraz, rokBudowy, liczbaPieter, pomieszczenia);
		this.dzialka = dzialka;
		this.powierzchniaZabudowy = powierzchniaZabudowy;

	}

	/**
	 * Tworzy nowy dom jako nowa nieruchomosc.
	 * 
	 * @param dzialka
	 * @param cenaOfetowa
	 * @param lokalizacja
	 * @param podatek
	 * @param metraz
	 * @param rokBudowy
	 * @param liczbaPieter
	 * @param pomieszczenia
	 * @param powierzchniaZabudowy
	 * @throws Exception
	 */
	private Dom(Dzialka dzialka, BigDecimal cenaOfetowa, Lokalizacja lokalizacja, BigDecimal podatek, int metraz,
			int rokBudowy, int liczbaPieter, String[] pomieszczenia, int powierzchniaZabudowy) throws Exception {
		super(cenaOfetowa, lokalizacja, podatek, metraz, rokBudowy, liczbaPieter, pomieszczenia);
		this.dzialka = dzialka;
		this.powierzchniaZabudowy = powierzchniaZabudowy;
	}

	public static int liczbaDomow() {
		return domy.size();
	}

	/**
	 * Tworzy dom z obecnej juz nieruchomosci.
	 * 
	 * @param dzialka
	 * @param nieruchomosc         nieruchomosc ktora ma zostac zmieniona na dom
	 * @param metraz
	 * @param rokBudowy
	 * @param liczbaPieter
	 * @param pomieszczenia
	 * @param powierzchniaZabudowy
	 * @return
	 * @throws Exception
	 */
	public static Dom constructDom(Dzialka dzialka, Nieruchomosc nieruchomosc, int metraz, int rokBudowy,
			int liczbaPieter, String[] pomieszczenia, int powierzchniaZabudowy) throws Exception {
		if (dzialka == null)
			throw new NullPointerException("dzialka is null");

		Dom nowy = new Dom(dzialka, nieruchomosc, metraz, rokBudowy, liczbaPieter, pomieszczenia, powierzchniaZabudowy);
		dzialka.addDom(nowy); // sprawdza też czy część nie istnieje już na innej działce
		domy.add(nowy); // dodanie do ekstencji powinno być po dodaniu do dzialki bo dzialka spprawdza
						// czynie ma w ekstensji
		return nowy;
	}

	/**
	 * Tworzy nowy dom.
	 * 
	 * @param dzialka
	 * @param cenaOfetowa
	 * @param podatek
	 * @param metraz
	 * @param rokBudowy
	 * @param liczbaPieter
	 * @param pomieszczenia
	 * @param powierzchniaZabudowy
	 * @return
	 * @throws Exception
	 */
	public static Dom constructDom(Dzialka dzialka, BigDecimal cenaOfetowa, BigDecimal podatek, int metraz,
			int rokBudowy, int liczbaPieter, String[] pomieszczenia, int powierzchniaZabudowy) throws Exception {
		if (dzialka == null)
			throw new NullPointerException("dzialka is null");

		Dom nowy = new Dom(dzialka, cenaOfetowa, dzialka.getLokalizacja(), podatek, metraz, rokBudowy, liczbaPieter,
				pomieszczenia, powierzchniaZabudowy);
		dzialka.addDom(nowy); // sprawdza też czy część nie istnieje już na innej działce
		domy.add(nowy); // dodanie do ekstencji powinno być po dodaniu do dzialki bo dzialka spprawdza
						// czynie ma w ekstensji
		return nowy;
	}

	/**
	 * Dodaje medium.
	 * 
	 * @param medium
	 * @return true jezeli dodalo, false jezeli nie dodalo
	 */
	public boolean addMedia(String medium) {
		if (!media.contains(medium)) {
			media.add(medium);
			return true;
		} else
			return false;
	}

	/**
	 * Sprawdza czy taki dom zostal juz stworzony.
	 * 
	 * @param dom
	 * @return
	 */
	public static boolean extenseContaint(Dom dom) {
		return domy.contains(dom);
	}

	public static List<Dom> filtrujDomy(Predicate<Dom> pred) {
		return domy.stream().filter(pred).collect(Collectors.toList());
	}

	public Dzialka getDzialka() {
		return dzialka;
	}

	public void remove() {
		super.remove();
		if (dzialka != null) {
			Dzialka d = this.getDzialka();
			dzialka = null;
			super.remove();
			domy.remove(this);
			d.delDom(this);
		}
	}

	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(domy);
	}

	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		domy = (Set<Dom>) oStream.readObject();
	}

	
	@Override
	protected void changeBehaviour() {
		super.changeBehaviour();
		this.remove();
	}
}

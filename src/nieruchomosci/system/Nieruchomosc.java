package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Nieruchomosc implements Serializable {
	private static Set<Nieruchomosc> nieruchomosci = new HashSet<>();
	private BigDecimal cenaOfetowa;
	private Lokalizacja lokalizacja;
	private List<String> atrakcje = new ArrayList<>();
	private Set<Wycena> wyceny = new HashSet<>();
	private Set<Umowa> umowy = new HashSet<>();
	private Set<Prezentacja> prezentacje = new HashSet<>();
	/**
	 * Podatek liczony w PLN na rok.
	 */
	private BigDecimal podatek;

	/**
	 * Definiowanie zachowania obiektu przy zmianie na inny typ. Zwykle
	 * aktualizowanie ekstencji, usuanie obiektu.
	 */
	protected void changeBehaviour() {
		nieruchomosci.remove(this);
	}

	public static List<Nieruchomosc> getAll() {
		return new ArrayList<>(nieruchomosci);
	}

	public Nieruchomosc(BigDecimal cenaOfetowa, Lokalizacja lokalizacja, BigDecimal podatek) {
		setCenaOfetowa(cenaOfetowa);
		setLokalizacja(lokalizacja);
		setPodatek(podatek);
		nieruchomosci.add(this);
	}

	public boolean maWycene() {
		return this.wyceny.size() > 0;
	}

	public List<Wycena> getWyceny() {
		return new ArrayList<>(this.wyceny);
	}

	/**
	 * Aktualizuje asocjacje w obiektach z ktorymi jest poloczony.
	 * 
	 * @param nieruchomosc obiekt ktory wskakuje na miejsce obecnego
	 */
	protected void aktualizujAsocjacje(Nieruchomosc nieruchomosc) {
		wyceny.forEach(w -> w.zmienAsocjacjeNieruchomosci(nieruchomosc));
		this.prezentacje.forEach(p -> p.zmienAsocjacjeNieruchomosci(nieruchomosc));
		for (Umowa u : this.umowy)
			u.zmienAsocjacjeNieruchomosci(this, nieruchomosc);
		this.changeBehaviour();
	}

	public BigDecimal getCenaOfetowa() {
		return cenaOfetowa;
	}

	public void setCenaOfetowa(BigDecimal cenaOfetowa) {
		if (cenaOfetowa == null)
			throw new NullPointerException();
		this.cenaOfetowa = cenaOfetowa;
	}

	public Lokalizacja getLokalizacja() {
		return lokalizacja;
	}

	public List<String> getAtrakcje() {
		return new ArrayList<String>(this.atrakcje);
	}

	private void setLokalizacja(Lokalizacja lokalizacja) {
		if (lokalizacja == null)
			throw new NullPointerException();
		this.lokalizacja = lokalizacja;
	}

	public BigDecimal getPodatek() {
		return podatek;
	}

	public static int liczbaNieruchomosci() {
		return nieruchomosci.size();
	}

	public void setPodatek(BigDecimal podatek) {
		if (podatek == null)
			throw new NullPointerException();
		this.podatek = podatek;
	}

	/**
	 * Dodaje atrakcje tylko jezeli nie jest ona juz zapisana.
	 * 
	 * @param atrakcja
	 * @return true jezeli dodalo pomyslnie i false jezeli nie udalo sie dodac
	 */
	public boolean addAtrakcje(String atrakcja) {
		if (atrakcja != null && !atrakcje.contains(atrakcja)) {
			return atrakcje.add(atrakcja);
		}
		return false;
	}

	protected void addWycena(Wycena wycena) throws Exception {
		final Nieruchomosc nieruch = wycena.getNieruchomosc();
		if (!this.wyceny.contains(wycena) && nieruch == this) {
			this.wyceny.add(wycena);
		} else if (nieruch != null)
			throw new Exception("wycena ma juz nieruchomosc");
	}

	protected void delWycena(Wycena wycena) throws Exception {
		final Nieruchomosc nieruch = wycena.getNieruchomosc();
		if (wyceny.contains(wycena) && nieruch == null) {
			wyceny.remove(wycena);
		} else if (nieruch != null)
			throw new Exception("można usunąć jedynie wycene, która nie jest już połączona z nieruchomoscia");
	}

	public Set<Prezentacja> getPrezentacje() {
		return new HashSet<>(this.prezentacje);
	}

	public String toString() {
		return getLokalizacja().toString() + " wyceny: " + wyceny.size() + ", umoway: " + this.umowy.size()
				+ ", prezentacje: " + this.prezentacje.size() + ", cena: " + this.cenaOfetowa;
	}

	/**
	 * Usuwa atrakcje.
	 * 
	 * @param atrakcja
	 * @return zwraca true jezeli usunieto atrakcje i false jezeli nie bylo takiej w
	 *         systemie
	 */
	public boolean delAtrakcje(String atrakcja) {
		return atrakcje.remove(atrakcja);
	}

	public static Collection<Nieruchomosc> filtrujNieruchomosci(Predicate<Nieruchomosc> pred) {
		return nieruchomosci.stream().filter(pred).collect(Collectors.toList());
	}

	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(nieruchomosci);
	}

	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		nieruchomosci = (Set<Nieruchomosc>) oStream.readObject();
	}

	protected boolean addUmowa(Umowa umowa) {
		final Set<Nieruchomosc> nier = umowa.getNieruchomosci();
		if (!umowy.contains(umowa) && nier.contains(this)) {
			umowy.add(umowa);
			return true;
		} else
			return false;
	}

	public boolean delUmowa(Umowa umowa) {
		if (this.umowy.contains(umowa)) {
			this.umowy.remove(umowa);
			umowa.delNieruchomosc(this);
			return true;
		} else
			return false;
	}

	protected boolean addPrezentacja(Prezentacja prezentacja) throws Exception {
		final Nieruchomosc nier = prezentacja.getNieruchomosc();
		if (!this.prezentacje.contains(prezentacja) && nier == this) {
			this.prezentacje.add(prezentacja);
			return true;
		} else if (nier != null)
			throw new Exception("prezentacja ma juz nieruchomosc");
		return false;
	}

	public boolean delPrezentacja(Prezentacja prezentacja) {
		if (this.prezentacje.contains(prezentacja)) {
			this.prezentacje.remove(prezentacja);
			prezentacja.delNieruchomosc();
			return true;
		} else
			return false;
	}

	public void remove() {
		Nieruchomosc obecna = this;
		// usuwanie wycen
		this.wyceny.forEach(w -> w.remove());
		// usuwanie nieruchomosci z umowy
		this.umowy.forEach(u -> u.delNieruchomosc(obecna));
		// usuwanie prezentacji
		this.prezentacje.forEach(p -> p.delNieruchomosc());
		// usuwanie ekstensji
		nieruchomosci.remove(obecna);
	}

	public static boolean contains(Nieruchomosc nieruchomosc) {
		return nieruchomosci.contains(nieruchomosc);
	}
}

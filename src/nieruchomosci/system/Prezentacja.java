package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

public class Prezentacja implements Serializable {

	private LocalDate dataPrezentacji;
	private Status status = Status.W_TOKU;

	private Set<Kupujacy> nabywcy = new HashSet<>();
	private Set<Handlowiec> handlowcy = new HashSet<>();
	private Nieruchomosc nieruchomosc;
	private static Set<Prezentacja> prezentacje = new HashSet<>();
	/**
	 * Maksymalna liczba handlowców na prezentacji.
	 */
	private static int maxHandlowcy = 2;
	/**
	 * Maksymalna liczba mozliwych odwiedzajacych.
	 */
	private static int maxNabywcy = 15;

	public Prezentacja(Nieruchomosc nieruchomosc, List<Handlowiec> handlowcy, List<Kupujacy> kupujacy,
			LocalDate dataPrezentacji) throws Exception {
		this.setNieruchomosc(nieruchomosc);
		this.addHandlowcy(handlowcy);
		this.addNabywcy(kupujacy);
		this.setDataPrezentacji(dataPrezentacji);
		prezentacje.add(this);
	}

	private void setNieruchomosc(Nieruchomosc nieruchomosc) throws Exception {
		if (nieruchomosc == null)
			throw new NullPointerException();
		this.nieruchomosc = nieruchomosc;
		nieruchomosc.addPrezentacja(this);
	}

	private interface ThrowConsumer<T> {
		public void accept(T typ) throws Exception;
	}

	/**
	 * Dodaje kolekcje dodawana do postawowej, przy sprawdzeniu czy nie zostanie
	 * przekroczona wartosc maxymalna. Ilosc po zsumowaniu z dodawanej i podstawowej
	 * musi byc wieksza od 0.
	 * 
	 * @param <T>
	 * @param dodawana   kolekcja
	 * @param podstawowa kolekcja do ktorej beda dodawane wartosci
	 * @param max
	 * @throws Exception jezeli zostanie przekroczona maxymalna wartosc lub nie
	 *                   zostanie podana zadna wartosc
	 */
	protected <T> void addObligatoryColelction(Collection<T> dodawana, Collection<T> podstawowa, int max,
			ThrowConsumer<T> polaczZwrotne) throws Exception {
		int suma = podstawowa.size() + dodawana.size();
		if (suma == 0)
			throw new Exception("Jezeli nie ma obiektu musisz podac przynajmniej jeden.");
		if (suma <= max) {
			for (T typ : dodawana) {
				if (!podstawowa.contains(typ) && typ != null) {
					podstawowa.add(typ);
					polaczZwrotne.accept(typ);
				}

			}
		} else {
			T first = dodawana.iterator().next();
			throw new Exception("max ilosc to " + max + " dla obiektow " + first.getClass());
		}
	}

	private void setDataPrezentacji(LocalDate dataPrezentacji) {
		if (dataPrezentacji == null)
			throw new NullPointerException();
		this.dataPrezentacji = dataPrezentacji;
	}

	public void addHandlowcy(Collection<Handlowiec> handlowcy) throws Exception {
		final Prezentacja prez = this;
		addObligatoryColelction(handlowcy, this.handlowcy, this.maxHandlowcy, t -> t.addPrezentacja(prez));
	}

	public boolean delHandlowiec(Handlowiec handlowiec) {
		if (this.handlowcy.contains(handlowiec)) {
			this.handlowcy.remove(handlowiec);
			handlowiec.delPrezentacja(this);
			if (getHandlowcy().size() == 0)
				this.remove();
			return true;
		} else
			return false;

	}

	/**
	 * Usuniecie nieruchomosci jest rownoznaczne z usunieciem prezentacji.
	 * 
	 * @return
	 */
	public boolean delNieruchomosc() {
		if (this.nieruchomosc != null) {
			Nieruchomosc tmp = this.nieruchomosc;
			this.nieruchomosc = null;
			tmp.delPrezentacja(this);
			this.remove();
			return true;
		}
		return false; // tutaj jeszcze this nieruchomosc trzeba zamienic

	}

	public void remove() {

		Prezentacja prez = this;
		// usuwa asocjacje z nieruchomoscia
		this.delNieruchomosc();

		// usuwa asocjacje z klientem
		//this.nabywcy.forEach((k) -> k.delPrezentacja(prez));
		while(this.nabywcy.iterator().hasNext()) {
			this.nabywcy.iterator().next().delPrezentacja(prez);
		}
		// usuwa asocjacje z handlowcami
		//this.handlowcy.forEach((h) -> h.delPrezentacja(prez));
		while(this.handlowcy.iterator().hasNext()) {
			this.handlowcy.iterator().next().delPrezentacja(prez);
		}
		// usuwa ekstensje
		prezentacje.remove(this);
	}

	public void addNabywcy(Collection<Kupujacy> nabywcy) throws Exception {
		final Prezentacja prez = this;
		addObligatoryColelction(nabywcy, this.nabywcy, this.maxNabywcy, t -> t.addPrezentacja(prez));
	}

	public boolean delNabywcy(Kupujacy kupujacy) {
		if (this.nabywcy.contains(kupujacy)) {
			this.nabywcy.remove(kupujacy);
			kupujacy.delPrezentacja(this);
			if (getNabywcy().size() == 0)
				this.remove();
			return true;
		} else
			return false;
	}
	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(prezentacje);
	}
	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		prezentacje = (Set<Prezentacja>) oStream.readObject();
	}

	public Nieruchomosc getNieruchomosc() {
		return this.nieruchomosc;
	}

	public Set<Handlowiec> getHandlowcy() {
		return new HashSet<Handlowiec>(this.handlowcy);
	}

	public Set<Kupujacy> getNabywcy() {
		return new HashSet<Kupujacy>(this.nabywcy);
	}

	protected void zmienAsocjacjeNieruchomosci(Nieruchomosc nieruchomosc2) {
		this.nieruchomosc = nieruchomosc2;
	}

	public static Integer liczbaPrezentacji() {

		return prezentacje.size();
	}
}

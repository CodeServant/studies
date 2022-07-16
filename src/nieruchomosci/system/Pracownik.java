package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class Pracownik implements iOsoba, Serializable {
	private LocalDate dataRozpoczecia;
	private String rodzajUmowy;
	private static BigDecimal pensjaMinimalna = new BigDecimal(2000);
	private static Set<Pracownik> pracownicy = new HashSet<>();
	private static Set<String> wszystkieLoginy = new HashSet<>();
	private Osoba osoba;
	private String login;
	private String haslo;

	protected Pracownik(Osoba osoba, LocalDate dataRozpoczecia, String rodzajUmowy, String login, String haslo)
			throws Exception {
		if (osoba.isPracownik())
			throw new Exception("osoba jest już pracownikiem");
		else
			this.osoba = osoba;
		this.setDataRozpoczecia(dataRozpoczecia);
		this.setRodzajUmowy(rodzajUmowy);
		this.setLogin(login);
		this.setHaslo(haslo);
	}

	/**
	 * Interface stworzony na potrzeby tworzenia generycznej metody
	 * createPracownikObj(Osoba ExSupplier<T>)
	 * 
	 * @return
	 * @throws Exception
	 */
	private interface ExSupplier<T> {
		T getOrThrow() throws Exception;
	}
	
	private static <T extends Pracownik> T createPracownikObj(Osoba osoba, ExSupplier<T> sup) throws Exception {
		if (osoba == null)
			throw new NullPointerException();

		T nowy = sup.getOrThrow();
		osoba.addPracownik(nowy);
		pracownicy.add(nowy); // dodanie do ekstencji powinno być po dodaniu do osoby bo osoba sprawdza
								// czynie ma w ekstensji

		return nowy;
	}

	public static Rzeczoznawca createRzeczoznawca(Osoba osoba, LocalDate dataRozpoczecia, String rodzajUmowy,
			String login, String haslo, int praktyka, BigDecimal pensja) throws Exception {
		return createPracownikObj(osoba,
				() -> new Rzeczoznawca(osoba, dataRozpoczecia, rodzajUmowy, login, haslo, praktyka, pensja));

	}

	public static Handlowiec createHandlowiec(Osoba osoba, LocalDate dataRozpoczecia, String rodzajUmowy, String login,
			String haslo, BigDecimal podstawaPensji, BigDecimal prowizja) throws Exception {

		return createPracownikObj(osoba,
				() -> new Handlowiec(osoba, dataRozpoczecia, rodzajUmowy, login, haslo, podstawaPensji, prowizja));
	}
	
	/**
	 * Straz pracy to jest liczba przeprawcowanych lat z dokładnościa do miesiecy.
	 * 
	 * @return
	 */
	public double stazPracy() {
		final Period prd = dataRozpoczecia.until(LocalDate.now());
		final double years = ((double) prd.getYears());
		final double months = ((double) prd.getMonths());
		return years + (months / 12);
	}

	public static boolean extentsContains(Pracownik p) {
		return pracownicy.contains(p);
	}

	public static void setPensjaMinimalna(BigDecimal nowa) {
		pensjaMinimalna = nowa;
	}

	public static BigDecimal getPensjaMinimalna() {
		return pensjaMinimalna;
	}

	public LocalDate getDataRozpoczecia() {
		return dataRozpoczecia;
	}

	public void setDataRozpoczecia(LocalDate dataRozpoczecia) {
		if (dataRozpoczecia == null)
			throw new NullPointerException();
		this.dataRozpoczecia = dataRozpoczecia;
	}

	public String getRodzajUmowy() {
		return rodzajUmowy;
	}

	public void setRodzajUmowy(String rodzajUmowy) {
		if (rodzajUmowy == null)
			throw new NullPointerException();
		this.rodzajUmowy = rodzajUmowy;
	}

	public String getLogin() {
		return login;
	}

	public boolean verifyPassword(String h) {
		return h.equals(this.haslo);
	}

	public static boolean verification(String login, String passwd) {
		if (loginValid(login)) {
			return getPracownik(login).verifyPassword(passwd);
		} else
			return false;

	}

	public static Pracownik getPracownik(String login) {
		for (Pracownik p : pracownicy)
			if (p.getLogin().equals(login))
				return p;
		return null;

	}

	public static List<Pracownik> allPracownicy() {
		return new ArrayList<>(pracownicy);
	}

	/**
	 * Sprawdza czy login jest poprwnie zbudowany i czy jest w systemie,.
	 * 
	 * @param login
	 * @return
	 */
	private static boolean loginValid(String login) {
		if (login == null)
			return false;
		if (!wszystkieLoginy.contains(login))
			return false;
		return true;
	}

	public String toString() {
		return this.login + " " + this.getImie() + " " + haslo;
	}

	public void setLogin(String login) throws Exception {
		if (login == null)
			throw new NullPointerException();
		if (!wszystkieLoginy.contains(login)) {
			this.login = login;
			wszystkieLoginy.add(login);
		} else {
			throw new Exception("login nie jest unikalny");
		}

	}

	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(pracownicy);
		oStream.writeObject(wszystkieLoginy);
	}

	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		pracownicy = (HashSet<Pracownik>) oStream.readObject();
		wszystkieLoginy = (HashSet<String>) oStream.readObject();
	}

	public void setHaslo(String haslo) {
		if (haslo == null)
			throw new NullPointerException();
		this.haslo = haslo;
	}

	public static int liczbaPracownikow() {
		return pracownicy.size();
	}

	@Override
	public Osoba getOsoba() {
		return this.osoba;
	}

	public void remove() {
		if (this.osoba != null) {
			Osoba tmp = this.getOsoba();
			this.osoba = null;
			tmp.remove();
		}
		pracownicy.remove(this);
		wszystkieLoginy.remove(this.login);
	}

	
}

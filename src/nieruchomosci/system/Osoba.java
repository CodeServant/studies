package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class Osoba implements iOsoba, Serializable {
	private String imie;
	private String nazwisko;
	private String pesel;
	private static Map<String, Osoba> osoby = new HashMap<>();
	private Klient klient;
	private Pracownik pracownik;

	public Osoba(String imie, String nazwisko, String pesel) throws Exception {
		if (imie == null || nazwisko == null || pesel == null)
			throw new NullPointerException();
		if (osoby.containsKey(pesel))
			throw new Exception("osoba o takim peselu już istnieje");
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.pesel = pesel;
		osoby.put(pesel, this);
	}

	protected void addKlient(Klient klient) throws Exception {
		if (klient == null)
			throw new NullPointerException();
		if (Klient.extentContains(klient)) {
			throw new Exception("obiekt jest obiektem innej klasy, nie można współdzielić");
		}
		if (this.klient == null) {
			this.klient = klient;
		} else
			throw new Exception("osoba już posiada klienta");
	}

	protected void addPracownik(Pracownik pracownik) throws Exception {
		if (pracownik == null)
			throw new NullPointerException();
		if (Pracownik.extentsContains(pracownik)) {
			throw new Exception("obiekt jest obiektem innej klasy, nie można współdzielić");
		}
		if (this.pracownik == null) {
			this.pracownik = pracownik;
		} else
			throw new Exception("osoba już posiada pracownika");
	}

	public boolean isPracownik() {
		return pracownik != null;
	}

	public boolean isKlient() {
		return klient != null;
	}

	public Pracownik getPracownik() throws Exception {
		if (!isPracownik())
			throw new Exception(this + " nie jest pracownikiem");
		return pracownik;
	}

	public Klient getKlient() throws Exception {
		if (!isKlient())
			throw new Exception(this + " nie jest klientem");
		return this.klient;
	}

	public void remove() {
		if (this.isPracownik()) {
			Pracownik tmp = this.pracownik;
			this.pracownik=null;
			tmp.remove();
		}
		if (this.isKlient()) {
			Klient tmp = this.klient;
			this.klient=null;
			tmp.remove();
		}
		if(osoby.containsKey(this.pesel) && osoby.get(this.pesel).equals(this)) {
			osoby.remove(this.pesel);
		}
	}
	public static Collection<Osoba> getAllExtents() {
		return new ArrayList<>(osoby.values());
	}
	@Override
	public String getImie() {
		return this.imie;
	}

	@Override
	public String getNazwisko() {
		return this.nazwisko;
	}

	@Override
	public String getPesel() {
		return this.pesel;
	}

	@Override
	public Osoba getOsoba() {
		return this;
	}

	public static int liczbaOsob() {
		return osoby.size();
	}
	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(osoby);
	}
	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		osoby = (Map<String, Osoba>) oStream.readObject();
	}

	public static Osoba wyszukaj(String pesel) {
		return osoby.get(pesel);
	}
}

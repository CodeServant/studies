package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;

public class Klient implements iKlient, Serializable {
	private LocalDate dataRejestracji;
	private String osobowosc;
	private List<String> mail = new ArrayList<>();
	private List<String> telefon = new ArrayList<>();
	private Osoba osoba;
	private static Set<Klient> klienci = new HashSet<>();

	private Sprzedajacy sprzedawca;
	private Kupujacy kupujacy;

	private Klient(Osoba osoba, LocalDate dataRejestracji, String osobowosc) {
		this.osoba = osoba;
		this.dataRejestracji = dataRejestracji;
		this.osobowosc = osobowosc;
	}

	public static Klient createKlient(Osoba osoba, LocalDate dataRejestracji, String osobowosc) {
		if (osoba == null || dataRejestracji == null || osobowosc == null)
			throw new NullPointerException();

		Klient nowy = null;
		try {
			nowy = new Klient(osoba, dataRejestracji, osobowosc);
			osoba.addKlient(nowy);
			klienci.add(nowy); // dodanie do ekstencji powinno być po dodaniu do osoby bo osoba spprawdza
								// czynie ma w ekstensji
		} catch (Exception e) {
			e.printStackTrace();
		} // doda klienta jezeli nie ma jeszcze zapisanego

		return nowy;
	}

	protected void addSprzedajacy(Sprzedajacy sprzedajacy) throws Exception {
		if (sprzedajacy == null)
			throw new NullPointerException();
		if (Sprzedajacy.extentsContains(sprzedajacy)) {
			throw new Exception("obiekt jest obiektem innej klasy, nie można współdzielić");
		}
		if (this.sprzedawca == null) {
			this.sprzedawca = sprzedajacy;
		} else
			throw new Exception("Klient już posiada sprzedajacego");
	}

	protected void addKupujacy(Kupujacy kupujacy) throws Exception {
		if (kupujacy == null)
			throw new NullPointerException();
		if (Kupujacy.extentsContains(kupujacy)) {
			throw new Exception("obiekt jest obiektem innej klasy, nie można współdzielić");
		}
		if (this.kupujacy == null) {
			this.kupujacy = kupujacy;
		} else
			throw new Exception("Klient już posiada kupujacego");
	}

	public static boolean extentContains(Klient k) {
		return klienci.contains(k);
	}

	public static int liczbaKlientow() {
		return klienci.size();
	}

	public boolean isSprzedajacy() {
		return this.sprzedawca != null;
	}

	public boolean isKupujacy() {
		return this.kupujacy != null;
	}

	public Sprzedajacy getSprzedajacy() throws Exception {
		if (!isSprzedajacy())
			throw new Exception(this + " nie jest sprzedajacym");
		return this.sprzedawca;
	}

	public Kupujacy getKupujacy() throws Exception {
		if (!isKupujacy())
			throw new Exception(this + " nie jest kupującym");
		return this.kupujacy;
	}

	@Override
	public void zmienOsobowosc(String nowa) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void addMail(String nowy) {
		if(nowy==null) throw new NullPointerException();
		mail.add(nowy);
	}

	@Override
	public void addTelefon(String nowy) {
		if(nowy==null) throw new NullPointerException();
		telefon.add(nowy);
	}

	@Override
	public Klient getKlient() {
		return this;
	}

	@Override
	public Osoba getOsoba() {
		return this.osoba;
	}

	@Override
	/**
	 * Czy klient ma wpisany do systemu jakis kontakt do siebie.
	 * 
	 * @return
	 */
	public boolean maKontakt() {
		return mail.size() + telefon.size() > 0;
	}

	public void remove() {
		
		if (this.isKupujacy()) {
			Kupujacy tmp = this.kupujacy;
			this.kupujacy=null;
			tmp.remove();
		}
		if (this.isSprzedajacy()) {
			Sprzedajacy tmp = this.sprzedawca;
			this.sprzedawca=null;
			tmp.remove();
		}
		
		if(this.osoba!=null) {
			Osoba tmp = this.osoba;
			this.osoba=null;
			tmp.remove();
		}
		this.klienci.remove(this);
	}
	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(klienci);
	}
	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		klienci = (Set<Klient>) oStream.readObject();
	}

}

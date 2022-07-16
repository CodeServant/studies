package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Kupujacy implements iKupujacy, Serializable {
	private String preferowanaFormaFinansowania;
	private static Set<Kupujacy> nabywcy = new HashSet<>();
	private Klient klient;
	private Set<Prezentacja> prezentacje = new HashSet<>();

	private Kupujacy(Klient klient, String preferForma) {
		this.klient = klient;
		this.preferowanaFormaFinansowania = preferForma;
	}

	public static Kupujacy createKupujacy(Klient klient, String preferForma) throws Exception {
		if (klient == null || preferForma == null)
			throw new NullPointerException();
		Kupujacy nowy = null;
		nowy = new Kupujacy(klient, preferForma);
		klient.addKupujacy(nowy);
		nabywcy.add(nowy);

		return nowy;
	}

	public static int liczbaKupujacych() {
		return nabywcy.size();
	}

	@Override
	public void setPreferowanaFormaFinansowania(String nowa) {
		if (nowa == null)
			throw new NullPointerException();
		this.preferowanaFormaFinansowania = nowa;
	}

	@Override
	public Kupujacy getKupujacy() {
		return this;
	}

	public static boolean extentsContains(Kupujacy k) {
		return nabywcy.contains(k);
	}

	protected boolean addPrezentacja(Prezentacja prezentacja) {
		final Set<Kupujacy> kupuj = prezentacja.getNabywcy();
		if (!this.prezentacje.contains(prezentacja) && kupuj.contains(this)) {
			this.prezentacje.add(prezentacja);
			return true;
		} else
			return false;
	}

	public boolean delPrezentacja(Prezentacja prezentacja) {
		if (this.prezentacje.contains(prezentacja)) {
			this.prezentacje.remove(prezentacja);
			prezentacja.delNabywcy(this);
			return true;
		} else
			return false;
	}

	public Set<Prezentacja> getPrezentacje() {
		return new HashSet<>(this.prezentacje);
	}

	@Override
	public Osoba getOsoba() {
		return this.klient.getOsoba();
	}

	@Override
	public Klient getKlient() {
		return this.klient;
	}

	public void remove() {
		Kupujacy k = this;
		if (this.klient != null) {
			Klient kl = this.klient;
			this.klient = null;
			kl.remove();

		}
		prezentacje.forEach(p -> p.delNabywcy(k));
		nabywcy.remove(this);

	}
	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(nabywcy);
	}
	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		nabywcy = (Set<Kupujacy>) oStream.readObject();
	}

}

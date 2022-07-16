package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Sprzedajacy implements iSprzedajacy, Serializable {

	private static Set<Sprzedajacy> sprzedawcy = new HashSet<>();
	private Klient klient;
	private Set<Umowa> umowy = new HashSet<>();
	// sprzedający powinien mieć albo tel albo maila
	private Sprzedajacy(Klient klient) {
		this.klient = klient;
	}

	private static Sprzedajacy createSprzedajacy(Klient klient) throws Exception {
		if (klient == null)
			throw new NullPointerException();
		Sprzedajacy nowy;
		nowy = new Sprzedajacy(klient);
		klient.addSprzedajacy(nowy);
		sprzedawcy.add(nowy);
		return nowy;
	}

	private static Sprzedajacy sprzedajacyZKontaktem(Klient klient, String kontakt,
			BiConsumer<Klient, String> dodajNowy) throws Exception {
		Sprzedajacy nowy = createSprzedajacy(klient);

		if (klient.maKontakt() && kontakt == null) {
			return nowy;
		} else
			dodajNowy.accept(klient, kontakt);
		return nowy;
	}

	public static Sprzedajacy sprzedajacyZNumerem(Klient klient, String numerTel) throws Exception {
		return Sprzedajacy.sprzedajacyZKontaktem(klient, numerTel, (k, tel) -> k.addTelefon(tel));

	}

	public static Sprzedajacy sprzedajacyZMailem(Klient klient, String mail) throws Exception {
		return Sprzedajacy.sprzedajacyZKontaktem(klient, mail, (k, m) -> k.addMail(m));
	}

	public static int liczbaSprzedajacych() {
		return sprzedawcy.size();
	}

	@Override
	public Sprzedajacy getSprzedajacy() {
		return this;
	}

	public static boolean extentsContains(Sprzedajacy s) {
		return sprzedawcy.contains(s);
	}
	
	protected void addUmowa(Umowa umowa) throws Exception {
		final Sprzedajacy sp = umowa.getSprzedajacy();
		if (!this.umowy.contains(umowa) && sp == this) {
			this.umowy.add(umowa);
		} else if (sp != this)
			throw new Exception("nie można dodać umowy, umowa ma już połączenie z sprzedajacym");
	}
	protected void delUmowa(Umowa umowa) throws Exception {
		final Sprzedajacy sp = umowa.getSprzedajacy();
		if (this.umowy.contains(umowa) && sp == null) {
			this.umowy.remove(umowa);
		} else if (sp != null)
			throw new Exception("można usunąć jedynie umowę, która nie jest już połączona ze sprzedającym");
	}

	@Override
	public Osoba getOsoba() {
		return this.klient.getOsoba();
	}
	@Override
	public Klient getKlient() {
		return this.klient;
	}
	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(sprzedawcy);
	}
	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		sprzedawcy = (Set<Sprzedajacy>) oStream.readObject();
	}

	public void remove() {
		Sprzedajacy s = this;
		if(this.klient!=null) {
			Klient kl = this.klient;
			this.klient=null;
			kl.remove();
		}
		
		while(umowy.iterator().hasNext()) {
			umowy.iterator().next().delUmowa();;
		}
		
		//umowy.forEach(p -> p.delUmowa());
		sprzedawcy.remove(this);
	}

	
}

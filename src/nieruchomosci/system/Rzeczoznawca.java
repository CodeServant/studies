package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Rzeczoznawca extends Pracownik {
	private List<String> specjalnosci = new ArrayList<>();
	private List<String> wyksztalcenie = new ArrayList<>();
	private int praktyka;
	private BigDecimal pensja;
	private static Set<Rzeczoznawca> rzeczoznawcy = new HashSet<>();
	private Set<Wycena> wyceny = new HashSet<>();

	protected Rzeczoznawca(Osoba osoba, LocalDate dataRozpoczecia, String rodzajUmowy, String login, String haslo,
			int praktyka, BigDecimal pensja) throws Exception {
		super(osoba, dataRozpoczecia, rodzajUmowy, login, haslo);
		this.setPraktyka(praktyka);
		this.setPensja(pensja);
		rzeczoznawcy.add(this);
	}

	public void setPensja(BigDecimal pensja) throws Exception {
		if (pensja.compareTo(Pracownik.getPensjaMinimalna()) < 0)
			throw new Exception(
					"podana pensja " + pensja + " mniejsza od pensji minimalnej: " + Pracownik.getPensjaMinimalna());
		this.pensja = pensja;
	}

	public void setPraktyka(int praktyka) {
		this.praktyka = praktyka;
	}

	protected void addWycena(Wycena wycena) throws Exception {
		final Rzeczoznawca rzecz = wycena.getRzeczoznawca();
		if (!this.wyceny.contains(wycena) && rzecz == this) {
			this.wyceny.add(wycena);
		} else if (rzecz != null)
			throw new Exception("wycena ma juz rzeczoznawce");
	}

	protected void delWycena(Wycena wycena) throws Exception {
		final Rzeczoznawca rzecz = wycena.getRzeczoznawca();
		if (this.wyceny.contains(wycena) && rzecz == null) {
			this.wyceny.remove(wycena);
		} else if (rzecz != null)
			throw new Exception("można usunąć jedynie wycene, która nie jest już połączona z rzeczoznawcą");
	}

	public static int liczbaRzeczoznawcow() {
		return rzeczoznawcy.size();
	}

	public static boolean jestRzeczoznawca(iOsoba osoba) {
		Osoba os = osoba.getOsoba();
		if (!os.isPracownik())
			return false;
		Pracownik prac;
		try {
			prac = os.getPracownik();
			return rzeczoznawcy.contains(prac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(rzeczoznawcy);
	}

	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		rzeczoznawcy = (HashSet<Rzeczoznawca>) oStream.readObject();
	}

	@Override
	public void remove() {

		// usuwanie wycen
		if (this.getOsoba() != null) {
			// this.wyceny.forEach(w -> w.remove());
			while (this.wyceny.iterator().hasNext())
				this.wyceny.iterator().next().remove();
			// usuwanie z ekstensji
			rzeczoznawcy.remove(this);
		}

		super.remove();
	}
}

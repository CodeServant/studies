package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Handlowiec extends Pracownik {
	private BigDecimal podstawaPensji;
	private BigDecimal prowizja;
	private static Set<Handlowiec> handlowcy = new HashSet<>();
	private Set<Prezentacja> prezentacje = new HashSet<>();

	protected Handlowiec(Osoba osoba, LocalDate dataRozpoczecia, String rodzajUmowy, String login, String haslo,
			BigDecimal podstawaPensji, BigDecimal prowizja) throws Exception {
		super(osoba, dataRozpoczecia, rodzajUmowy, login, haslo);
		setProwizja(prowizja);
		setPodstawaPensji(podstawaPensji);
		handlowcy.add(this);
	}

	public BigDecimal wyliczPensje(BigDecimal sprzedaz) {
		return podstawaPensji.add(prowizja.multiply(sprzedaz));
	}

	public void setProwizja(BigDecimal prowizja) throws Exception {
		if (!(prowizja.compareTo(new BigDecimal(0)) > 0 && prowizja.compareTo(new BigDecimal(1)) < 0))
			throw new Exception("zła prowizja");
		this.prowizja = prowizja;
	}

	public void setPodstawaPensji(BigDecimal podstawaPensji) throws Exception {
		if (podstawaPensji.compareTo(Pracownik.getPensjaMinimalna()) < 0)
			throw new Exception("podana pensja " + podstawaPensji + " mniejsza od pensji minimalnej: "
					+ Pracownik.getPensjaMinimalna());
		this.podstawaPensji = podstawaPensji;
	}

	public static boolean jestHandlowecem(iOsoba osoba) {
		Osoba os = osoba.getOsoba();
		if (!os.isPracownik())
			return false;
		Pracownik prac;
		try {
			prac = os.getPracownik();
			return handlowcy.contains(prac);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Set<Prezentacja> getPrezentacje() {
		return new HashSet<>(this.prezentacje);
	}

	public boolean addPrezentacja(Prezentacja prezentacja) throws Exception {
//		final Set<Handlowiec> handl = prezentacja.getHandlowcy();
//		if (!this.prezentacje.contains(prezentacja) && handl.contains(this)) {
//			this.prezentacje.add(prezentacja);
//			return true;
//		} else
//			return false;
		if (!this.prezentacje.contains(prezentacja) && prezentacja != null) {
			this.prezentacje.add(prezentacja);
			Collection<Handlowiec> h = new ArrayList<>();
			h.add(this);
			prezentacja.addHandlowcy(h);
			return true;
		} else
			return true;
	}

	public static int liczbaHandlowcow() {
		return handlowcy.size();
	}

	public boolean delPrezentacja(Prezentacja prezentacja) {


		if (this.prezentacje.contains(prezentacja)) {
			this.prezentacje.remove(prezentacja);
			prezentacja.delHandlowiec(this);
			return true;
		} else
			return false;

	}

	@Override
	public void remove() {

		// usuwanie prezentacji
		Handlowiec h = this;
		// this.prezentacje.forEach(p->h.delPrezentacja(p));
		while (this.prezentacje.iterator().hasNext())
			this.prezentacje.iterator().next().delHandlowiec(h);
		// usuwanie z ekstensji
		this.handlowcy.remove(this);
		super.remove();
	}

	public static Collection<Handlowiec> getHandlowcy() {
		return new ArrayList<>(handlowcy);
	}

	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(handlowcy);
		
	}

	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		handlowcy = (HashSet<Handlowiec>) oStream.readObject();
	}

}

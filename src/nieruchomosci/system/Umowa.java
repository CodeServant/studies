package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

import javax.swing.text.html.Option;

public class Umowa implements Serializable {
	private LocalDate dataOd;
	private LocalDate dataDo;
	private BigDecimal prowizjaPosrednika = new BigDecimal("0.1");
	private LocalDate dataRealizacji; // nullable
	private String aktWlasnosci;
	private boolean oplacona;
	private boolean zatwierdzona;
	private List<Nieruchomosc> nieruchomosci = new ArrayList<>();
	private Sprzedajacy sprzedajacy;

	private static Set<Umowa> umowy = new HashSet<>();

	public Umowa(List<Nieruchomosc> nieruchomosci, Sprzedajacy sprzedawca, LocalDate dataOd, LocalDate dataDo,
			String aktWlasnosci) {
		try {
			this.addNieruchomosci(nieruchomosci);
			this.setSprzedawca(sprzedawca);
			nieruchomosci.forEach(n -> n.addUmowa(this));
			sprzedawca.addUmowa(this);
			if (dataOd == null || dataDo == null || aktWlasnosci == null)
				throw new NullPointerException();
			this.dataOd = dataOd;
			this.dataDo = dataDo;
			this.aktWlasnosci = aktWlasnosci;
			umowy.add(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addNieruchomosci(List<Nieruchomosc> nieruchomosci) throws Exception {
		if (nieruchomosci.size() == 0)
			throw new Exception("musisz podac przynajmniej 1 nieruchomosc");
		this.nieruchomosci.addAll(nieruchomosci);
	}

	private void setSprzedawca(Sprzedajacy sprzedawca) {
		if (sprzedawca == null)
			throw new NullPointerException();
		this.sprzedajacy = sprzedawca;
	}

	public boolean zrealizowana() {
		return this.dataRealizacji != null;
	}

	public Set<Nieruchomosc> getNieruchomosci() {
		return new HashSet<Nieruchomosc>(nieruchomosci);
	}

	public Sprzedajacy getSprzedajacy() {
		return this.sprzedajacy;
	}

	public Optional<LocalDate> getDataRealizacji() {
		Optional<LocalDate> wynik = Optional.of(dataRealizacji);
		return wynik;
	}

	public void zatwierdz() {
		this.zatwierdzona = true;
	}

	public void oplac() {
		this.oplacona = true;
	}

	/**
	 * Usuwa wszelkie powiazania tej umowy i jej obiektow powiazanych.
	 */
	public void delUmowa() {

		if (this.sprzedajacy != null) {
			Sprzedajacy tmpSp = this.sprzedajacy;
			this.sprzedajacy = null;
			try {
				tmpSp.delUmowa(this);
//				for (Nieruchomosc nier : this.nieruchomosci) {
//					this.nieruchomosci.remove(nier);
//					nier.delUmowa(this);
//				}
				while (this.nieruchomosci.iterator().hasNext()) {
					Nieruchomosc nier = this.nieruchomosci.iterator().next();
					this.nieruchomosci.remove(nier);
					nier.delUmowa(this);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		umowy.remove(this);
	}

	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(umowy);
	}

	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		umowy = (Set<Umowa>) oStream.readObject();
	}

	public boolean delNieruchomosc(Nieruchomosc nieruchomosc) {

		if (this.nieruchomosci.contains(nieruchomosc)) {
			this.nieruchomosci.remove(nieruchomosc);
			nieruchomosc.delUmowa(this);
			return true;
		}
		if (this.nieruchomosci.size() == 0)
			this.delUmowa();
		return false;
	}

	public void zrealizuj(LocalDate data) throws Exception {
		if (data == null)
			throw new NullPointerException();
		if (data.compareTo(this.dataOd) < 0 || data.compareTo(this.dataDo) > 0)
			throw new Exception("zła data niie miesici sie w ramach umowy");
		this.dataRealizacji = data;
	}

	public static void zatwierdz(Umowa umowa) {
		umowa.zatwierdz();
	}

	protected void zmienAsocjacjeNieruchomosci(Nieruchomosc z, Nieruchomosc na) {
		if (this.nieruchomosci.remove(z))
			this.nieruchomosci.add(na);
	}

	public static List<Umowa> zrealizowane() {
		return umowy.stream().filter(u -> u.zrealizowana()).collect(Collectors.toList());
	}

	public static int liczbaUmow() {
		return umowy.size();
	}
}

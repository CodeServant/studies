package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Wycena jest tworzona przez rzeczoznawce i dotyczy nieruchomosci.
 *
 */
public class Wycena implements Serializable {
	private Rzeczoznawca rzeczoznawca;
	private Nieruchomosc nieruchomosc;

	private LocalDate data;
	private BigDecimal wartosc;
	private List<String> komentarzeDoWyceny = new ArrayList<>();
	private static Set<Wycena> wyceny = new HashSet<>();
	
	public Wycena(Rzeczoznawca rzeczoznawca, Nieruchomosc nieruchomosc, LocalDate data, BigDecimal wartosc)
			throws Exception {
		if (rzeczoznawca == null || nieruchomosc == null || data == null || wartosc == null)
			throw new NullPointerException();
		if (!Nieruchomosc.contains(nieruchomosc))
			throw new Exception("nieruchomosci nie ma w bazie");
		this.rzeczoznawca = rzeczoznawca;
		this.nieruchomosc = nieruchomosc;
		this.data = data;
		this.wartosc = wartosc;

		try {
			this.rzeczoznawca.addWycena(this);
			this.nieruchomosc.addWycena(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		wyceny.add(this);

	}
	
	/**
	 * Pobierz wartosc wyceny.
	 * @return
	 */
	public BigDecimal getWartosc() {
		return this.wartosc;
	}

	public String toString() {
		return this.data + ", " + this.wartosc + " " + this.liczbaKomentarzy() + " komentarzy";
	}
	/**
	 * Data jakiego okresu dotyczy dana wycena,
	 * @return
	 */
	public LocalDate getDataWyceny() {
		return this.data;
	}
	
	/**
	 * Usuwa te wycene z systemu.
	 */
	public void remove() {
		Rzeczoznawca tmpRzcz = this.rzeczoznawca;
		Nieruchomosc tmpNier = this.nieruchomosc;
		this.rzeczoznawca = null;
		this.nieruchomosc = null;
		try {
			tmpNier.delWycena(this);
			tmpRzcz.delWycena(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		wyceny.remove(this);
	}
	
	/**
	 * Dodaj komentarz do wyceny.
	 * @param komentarz
	 */
	public void addKomentarz(String komentarz) {
		if (komentarz == null)
			throw new NullPointerException();
		this.komentarzeDoWyceny.add(komentarz);
	}

	public List<String> getKomentarze() {
		return new ArrayList<>(this.komentarzeDoWyceny);
	}

	public Rzeczoznawca getRzeczoznawca() {
		return this.rzeczoznawca;
	}

	public static Collection<Wycena> filtruj(Predicate<Wycena> pred) {
		return wyceny.stream().filter(pred).collect(Collectors.toList());
	}

	public static int liczbaWycen() {
		return wyceny.size();
	}

	public int liczbaKomentarzy() {
		return this.komentarzeDoWyceny.size();
	}
	
	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(wyceny);
	}

	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		wyceny = (Set<Wycena>) oStream.readObject();
	}

	public Nieruchomosc getNieruchomosc() {
		return this.nieruchomosc;
	}

	protected void zmienAsocjacjeNieruchomosci(Nieruchomosc nieruchomosc2) {
		this.nieruchomosc = nieruchomosc2;
	}
}

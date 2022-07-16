package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Zabudowa extends Nieruchomosc {
	private static Set<Zabudowa> zabudowy = new HashSet<>();
	private int metraz;
	private int rokBudowy;
	private int liczbaPieter;
	private List<String> pomieszczenia = new ArrayList<>();

	protected Zabudowa(Nieruchomosc nieruchomosc, int metraz, int rokBudowy, int liczbaPieter, String[] pomieszczenia)
			throws Exception {

		this(nieruchomosc.getCenaOfetowa(), nieruchomosc.getLokalizacja(), nieruchomosc.getPodatek(), metraz, rokBudowy,
				liczbaPieter, pomieszczenia);
		nieruchomosc.aktualizujAsocjacje(this);
	}

	protected Zabudowa(BigDecimal cenaOfetowa, Lokalizacja lokalizacja, BigDecimal podatek, int metraz, int rokBudowy,
			int liczbaPieter, String[] pomieszczenia) throws Exception {
		super(cenaOfetowa, lokalizacja, podatek);
		this.metraz = metraz;
		this.rokBudowy = rokBudowy;
		this.liczbaPieter = liczbaPieter;
		addPomieszczenie(pomieszczenia);
		zabudowy.add(this);
	}

	public static List<Zabudowa> filtrujZabudowy(Predicate<Zabudowa> pred) {
		return zabudowy.stream().filter(pred).collect(Collectors.toList());
	}

	public boolean addPomieszczenie(String pomiszczenie) {
		if (!pomieszczenia.contains(pomiszczenie)) {
			if (pomiszczenie == null)
				throw new NullPointerException();
			pomieszczenia.add(pomiszczenie);
			return true;
		} else
			return false;
	}

	public void addPomieszczenie(String[] pomieszczenia) throws Exception {
		if (pomieszczenia.length == 0)
			throw new Exception("nie ma pomieszczeń podanych");

		for (String pomieszczenie : pomieszczenia) {
			this.addPomieszczenie(pomieszczenie);
		}
	}

	public void remove() {
		super.remove();
		zabudowy.remove(this);
	}

	public static int liczbaZabudow() {
		return zabudowy.size();
	}

	protected void changeBehaviour() {
		super.changeBehaviour();
		zabudowy.remove(this);
	}

	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(zabudowy);
	}
	
	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		zabudowy = (Set<Zabudowa>) oStream.readObject();
	}


	public int liczbaPomieszczen() {
		return pomieszczenia.size();
	}
}

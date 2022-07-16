package nieruchomosci.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Mieszkanie extends Zabudowa {
	private int pietro;
	private RodzajeMieszkan rodzajMieszkania;
	private static Set<Mieszkanie> mieszkania = new HashSet<>();

	public Mieszkanie(Nieruchomosc nieruchomosc, int metraz, int rokBudowy, int liczbaPieter, String[] pomieszczenia,
			int pietro, RodzajeMieszkan rodzaj) throws Exception {
		super(nieruchomosc, metraz, rokBudowy, liczbaPieter, pomieszczenia);
		if (rodzaj == null)
			throw new NullPointerException("rodzaj is null");
		this.rodzajMieszkania = rodzaj;
		this.pietro = pietro;
		mieszkania.add(this);
	}

	public Mieszkanie(BigDecimal cenaOfetowa, Lokalizacja lokalizacja, BigDecimal podatek, int metraz, int rokBudowy,
			int liczbaPieter, String[] pomieszczenia, int pietro, RodzajeMieszkan rodzaj) throws Exception {
		super(cenaOfetowa, lokalizacja, podatek, metraz, rokBudowy, liczbaPieter, pomieszczenia);
		if (rodzaj == null)
			throw new NullPointerException("rodzaj is null");
		this.rodzajMieszkania = rodzaj;
		this.pietro = pietro;
		mieszkania.add(this);
	}

	public static int liczbaMieszkan() {
		return mieszkania.size();
	}

	public static List<Mieszkanie> filtrujMieszkania(Predicate<Mieszkanie> pred) {
		return mieszkania.stream().filter(pred).collect(Collectors.toList());
	}

	public void remove() {
		super.remove();
		mieszkania.remove(this);
	}

	public static void writeExtents(ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(mieszkania);
	}
	public static void readExtents(ObjectInputStream oStream) throws IOException, ClassNotFoundException {
		mieszkania = (Set<Mieszkanie>) oStream.readObject();
	}

	@Override
	protected void changeBehaviour() {
		super.changeBehaviour();
		mieszkania.remove(this);
	}
}

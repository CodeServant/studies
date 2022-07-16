package nieruchomosci.system;

/**
 * iOsoba jest obiektem klasy Osoba.
 *
 */
public interface iOsoba {
	public default String getImie(){
		return getOsoba().getImie();
	};

	public default String getNazwisko(){
		return getOsoba().getNazwisko();
	};

	public default String getPesel() {
		return getOsoba().getPesel();
	}
	
	public Osoba getOsoba();
}

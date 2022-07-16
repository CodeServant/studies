package nieruchomosci.system;

/**
 * iKlient jest obiektem klasy klient. Z racji tego, ze mozna implementowac
 * wiele interfejsow, obiekty moga byc jednoczesnie pracownikiem i klientem.
 * 
 *
 */
public interface iKlient extends iOsoba {
	public default void zmienOsobowosc(String nowa) {
		getKlient().zmienOsobowosc(nowa);
	}

	public default void addMail(String nowy) {
		getKlient().addMail(nowy);
	}

	public default void addTelefon(String nowy) {
		getKlient().addTelefon(nowy);
	}

	public default boolean maKontakt() {
		return getKlient().maKontakt();
	}

	public Klient getKlient();
}

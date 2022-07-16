package nieruchomosci.system;

public interface iKupujacy extends iKlient {

	public default void setPreferowanaFormaFinansowania(String nowa) {
		getKupujacy().setPreferowanaFormaFinansowania(nowa);
	}

	@Override
	public default Osoba getOsoba() {
		return this.getKlient().getOsoba();
	}

	@Override
	public default Klient getKlient() {
		return getKupujacy().getKlient();
	}

	public Kupujacy getKupujacy();
}

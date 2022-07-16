package nieruchomosci.system;

public interface iSprzedajacy extends iKlient {
	public Sprzedajacy getSprzedajacy();
	
	
	
	@Override
	public default Klient getKlient() {
		return getSprzedajacy().getKlient();
	}
	
}

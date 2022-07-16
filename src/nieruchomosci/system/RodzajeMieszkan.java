package nieruchomosci.system;

public enum RodzajeMieszkan {

	APARTAMENT("Apartament"), PENTHOUSE("Penthouse"), BLIZNIAK("Blizniak"), BLOK("Blok");

	String rodzaj;

	private RodzajeMieszkan(String rodzaj) {
		this.rodzaj = rodzaj;
	}
}

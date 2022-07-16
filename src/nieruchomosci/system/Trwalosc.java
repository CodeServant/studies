package nieruchomosci.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Trwalosc {
	private File plik;

	public Trwalosc(String fileName) {
		plik = new File(fileName);
	}

	public void zapisz() throws FileNotFoundException, IOException {
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(plik));
		Dom.writeExtents(os);
		Dzialka.writeExtents(os);
		Klient.writeExtents(os);
		Kupujacy.writeExtents(os);
		Mieszkanie.writeExtents(os);
		Nieruchomosc.writeExtents(os);
		Osoba.writeExtents(os);
		Pracownik.writeExtents(os);
		Handlowiec.writeExtents(os);
		Prezentacja.writeExtents(os);
		Rzeczoznawca.writeExtents(os);
		Sprzedajacy.writeExtents(os);
		Umowa.writeExtents(os);
		Wycena.writeExtents(os);
		Zabudowa.writeExtents(os);
	}

	public void odczytaj() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream os = new ObjectInputStream(new FileInputStream(plik));
		Dom.readExtents(os);
		Dzialka.readExtents(os);
		Klient.readExtents(os);
		Kupujacy.readExtents(os);
		Mieszkanie.readExtents(os);
		Nieruchomosc.readExtents(os);
		Osoba.readExtents(os);
		Pracownik.readExtents(os);
		Handlowiec.readExtents(os);
		Prezentacja.readExtents(os);
		Rzeczoznawca.readExtents(os);
		Sprzedajacy.readExtents(os);
		Umowa.readExtents(os);
		Wycena.readExtents(os);
		Zabudowa.readExtents(os);
	}
}

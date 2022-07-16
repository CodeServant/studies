package utp.cls3.zad2;

import java.util.Formatter;

public class Purchase {
	public int idKlienta;
	public String nazwisko;
	public String imie;
	public String nazwaTowaru;
	public double cena;
	public double zakupionaIlosc;

	public Purchase(int idKlienta, String nazwisko, String imie, String nazwaTowaru, double cena,
			double zakupionaIlosc) {
		this.idKlienta = idKlienta;
		this.nazwisko = nazwisko;
		this.imie = imie;
		this.nazwaTowaru = nazwaTowaru;
		this.cena = cena;
		this.zakupionaIlosc = zakupionaIlosc;
	}

	public String toString() {
		String divisor = ";";
		return this.getIdKlienta() + divisor + nazwisko + " " + imie + divisor + nazwaTowaru + divisor + cena + divisor
				+ zakupionaIlosc;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public Double getCena() {
		return cena;
	}

	public Double getIlosc() {
		return zakupionaIlosc;
	}

	public Double getKoszt() {
		return zakupionaIlosc * cena;
	}

	public String getIdKlienta() {
		Formatter frmtr = new java.util.Formatter();
		String s = frmtr.format("%05d", this.idKlienta).toString();
		return "c" + s;
	}

	private String simplePadding(int var) {
		int paddingLength = 5;
		String strVar = String.valueOf(var);
		int idLength = strVar.length();
		String padding = "";
		for (int i = 0; i < paddingLength - idLength; i++)
			padding += "0";
		return padding;
	}
}

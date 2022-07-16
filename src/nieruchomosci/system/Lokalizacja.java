package nieruchomosci.system;

import java.io.Serializable;

/**
 * Opisuje atrybut zlorzony Lokalizacji.
 * 
 * @author s18543
 *
 */
public class Lokalizacja implements Serializable {
	private String miasto;
	private String ulica;
	private String kraj;
	private String nr;
	private static final String domyslnyKraj = "Polska";

	/**
	 * Tworzy lokalizację z domyslnym krajem jakim jest Polska;
	 * 
	 * @param miasto
	 * @param ulica
	 * @param nr
	 */
	public Lokalizacja(String miasto, String ulica, String nr) {
		this(domyslnyKraj, miasto, ulica, nr);
	}

	public Lokalizacja(String kraj, String miasto, String ulica, String nr) {
		if (miasto == null || kraj == null)
			throw new NullPointerException("miasto i państwo nie może być null");
		this.miasto = miasto;
		this.kraj = kraj;

		this.ulica = ulica;
		if (ulica == null && nr != null)
			throw new NullPointerException("lokalizacja nieprawidłowo zbudowana");
		this.nr = nr;
	}

	public String getKraj() {
		return this.kraj;
	}

	public String getMiasto() {
		return this.miasto;
	}

	public String getUlica() throws NullPointerException {
		if (ulica != null)
			throw new NullPointerException("ulica jest null");
		return this.ulica;
	}

	public String toString() {
		return kraj+", "+miasto+", "+(ulica==null?"":ulica.toString())+", "+(nr==null?"":nr.toString());
	}

	public String getNr() throws NullPointerException {
		if (this.nr != null)
			throw new NullPointerException("nr jest null");
		return this.nr;
	}
}

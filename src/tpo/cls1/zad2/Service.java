package tpo.cls1.zad2;

import java.io.*;
import java.net.*;
import javax.json.*;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Service {
	public String kodWaluta; // kod waluty państwa podanego przez użytkownika
	Service(String str){
		this.kodWaluta = getCurrencyCode(str);
	}
	String getWeather(String city) {
		final String apiKey = "2f31d491454a7c10221fac103780ea40";
		try {
			URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			InputStream inStream = request.getInputStream();
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream));
			StringBuilder strBuild = new StringBuilder();
			String line;
			while( (line = buffReader.readLine()) != null) {
				strBuild.append(line);
			}
			
			return strBuild.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	Double getRateFor(String curr) {
		String url_str = "https://api.exchangerate.host/convert?from="+curr+"&to="+this.kodWaluta;
		URL url;
		try {
			url = new URL(url_str);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonParser jp = Json.createParser(request.getInputStream());
			while(jp.hasNext()) {
				
				if(jp.next() == Event.KEY_NAME) {
					String key = jp.getString();
					if(key.equals("rate")) {
						jp.next();
						return jp.getBigDecimal().doubleValue();
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	Double getNBPRate() {
		if(this.kodWaluta.equals("PLN")) return 1.0;
		try {
			Document doc = Jsoup.connect("https://www.nbp.pl/kursy/kursya.html").get();
			Element tabelaA = doc.getElementsByTag("table").get(0);
			doc = Jsoup.connect("https://www.nbp.pl/kursy/kursyb.html").get();
			Element tabelaB = doc.getElementsByTag("table").get(0);
			Double wynik = szukajWTabelce(tabelaA);
			if(wynik == null)
				wynik = szukajWTabelce(tabelaB);
			return wynik;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Jezeli nie znajdzie to zwraca null.
	 * @param countryName
	 * @return
	 */
	public static String getCurrencyCode(String countryName) {
		for (int i=0; i<Locale.getAvailableLocales().length; i++) {
			Locale lokacja = Locale.getAvailableLocales()[i];
			if(  lokacja.getDisplayCountry().equals(countryName) )
				return Currency.getInstance(lokacja).getCurrencyCode();
		}
		return null;
	}
	private Double szukajWTabelce(Element table) {
		Elements els = table.getElementsByTag("tbody").get(0).getElementsByTag("tr");
		
		for(int i=0; i<els.size(); i++) {
			Elements tds = els.get(i).getElementsByTag("td");
			Element td = tds.get(1);
			String[] waluta = td.text().split(" ");
			if(waluta[1].equals(this.kodWaluta)) {
				return Double.valueOf(tds.get(2).text().replace(",", "."));
			}
		}
		return null;
		
	}
}  

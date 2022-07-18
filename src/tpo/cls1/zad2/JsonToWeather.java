package tpo.cls1.zad2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonToWeather {
	public String json;
	public JsonToWeather(String inJson) {
		json = inJson;
	}
	private double kalvinToCecius(Double kalvin) {
		return kalvin-273.15;
	}
	public double temperatureCelcius() {
		Double kelv = Double.parseDouble(this.retrieve("temp"));
		return kalvinToCecius(kelv);
	}
	private String retrieve(String q) {
		Pattern pat = Pattern.compile("\\\""+q+"\\\":((\\w||\\s||\\.])*)");
		Matcher mat = pat.matcher(json);
		mat.find();
		return mat.group(1);
	}
	
}

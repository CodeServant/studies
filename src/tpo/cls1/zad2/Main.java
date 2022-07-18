package tpo.cls1.zad2;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.*;
import javafx.stage.Stage;
import java.util.*;

import javafx.scene.Node;

public class Main extends Application {
	public static void main(String[] args) {
		Service s = new Service("Poland");
		String weatherJson = s.getWeather("Warsaw");
		Double rate1 = s.getRateFor("USD");
		Double rate2 = s.getNBPRate();
		// ...
		// część uruchamiająca GUI
		launch(args);
	}

	@Override
	public void start(Stage mainStage) throws Exception {
		Button button1 = new Button("confirm");
		TextField textField1 = new TextField();
		TextField textField2 = new TextField();
		TextField textField3 = new TextField();
		final WebView webView = new WebView();
		final WebEngine webEngine = webView.getEngine();

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(webView);
		webEngine.load("https://wikipedia.org");
		GridPane gridPane = new GridPane();
		gridPane.setMinSize(400, 200);
		gridPane.add(button1, 0, 0);
		gridPane.add(textField1, 1, 0);
		gridPane.add(textField2, 2, 0);
		gridPane.add(textField3, 3, 0);
		textField1.setTooltip(new Tooltip("country name"));
		textField1.setPromptText("Poland");
		textField2.setTooltip(new Tooltip("city name"));
		textField2.setPromptText("Warsaw");
		textField3.setTooltip(new Tooltip("currency code"));
		textField3.setPromptText("USD");
		Text text = new Text();
		button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				String country = textField1.getText();
				String city = textField2.getText();
				String curr = textField3.getText();
				Service service = new Service(country);
				String jsonWeather = service.getWeather(city);
				String wikiURL = "https://en.wikipedia.org/wiki/" + city;
				webEngine.load(wikiURL);

				String message = "current rate for " + curr + " is: " + service.getRateFor(curr).toString() + " "
						+ Currency.getInstance(service.kodWaluta).getSymbol() + "\ncurrent NBP rate for currency of "
						+ country + " is: " + service.getNBPRate().toString() + " PLN" + "\nit is "
						+ Math.round(new JsonToWeather(jsonWeather).temperatureCelcius()) + "℃ in " + city;
				text.setText(message);
			}
		});

		LinkedList<Node> listNode = new LinkedList<>();

		gridPane.add(text, 0, 1);
		gridPane.add(webView, 0, 2, 6, 8);
		Scene scene = new Scene(gridPane, 900, 500);
		scene.setFill(Color.BISQUE);
		mainStage.setTitle("TPO1_OM_S18543");
		mainStage.setScene(scene);
		mainStage.show();
	}
}

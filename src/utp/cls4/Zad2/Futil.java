package utp.cls4.Zad2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class Futil {
	public static void processDir(String dir, String resFIle) {
		try {
			FileOutputStream oStream = new FileOutputStream(resFIle);
			OutputStreamWriter oStreamWriter = new OutputStreamWriter(oStream, Charset.forName("UTF-8"));
			Files.walk(Paths.get(dir)).forEach(fileConsumer(oStreamWriter));
			oStreamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Consumer<Path> fileConsumer(OutputStreamWriter writer) {
		return new Consumer<Path>() {
			public void accept(Path file) {
				if (!Files.isDirectory(file))
					try {
						FileInputStream inStream = new FileInputStream(file.toFile());
						Charset sp1250 = Charset.forName("windows-1250");

						InputStreamReader reader = new InputStreamReader(inStream, sp1250);

						int rd;

						while ((rd = reader.read()) >= 0) {
							writer.write(rd);
						}
						inStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		};
	}
}

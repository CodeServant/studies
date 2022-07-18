package tpo.cls1.zad1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.SortedMap;

public class Futil {
	
	public static void processDir(String dirName, String  resultFileName) {
		Path dir = Paths.get(dirName);
		Path file = Paths.get(resultFileName);
		MyFV filevisitor = new MyFV(file);
		try {
			Files.walkFileTree(dir, filevisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

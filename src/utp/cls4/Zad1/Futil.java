package utp.cls4.Zad1;

import java.io.*;
import java.nio.file.*;

public class Futil {

	public static void processDir(String dirName, String resultFileName) {
		Path dir = Paths.get(dirName);
		Path file = Paths.get(resultFileName);
		MyFV filevisitor;
		try {
			filevisitor = new MyFV(file);
			Files.walkFileTree(dir, filevisitor);
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}

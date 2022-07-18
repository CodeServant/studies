package utp.cls4.Zad1;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.*;

public class MyFV implements FileVisitor<Path> {
	protected Path outPath; // wyjściowy plik
	private int depth; // głębokość na jakiej się aktualnie znajduje
	private OutputStreamWriter oStreamWriter;

	public MyFV(Path outPath) throws FileNotFoundException {
		this.outPath = outPath;
		FileOutputStream oStream = new FileOutputStream(outPath.toFile().getAbsolutePath());
		oStreamWriter = new OutputStreamWriter(oStream, Charset.forName("UTF-8"));
		depth = 0;
	}

	/**
	 * Dodaje inBuf do naszego wewnetrznego buffera zamieniajac kodowanie z sp1250
	 * do utf8
	 * 
	 * @param inBuf
	 * @throws IOException
	 */
	public void cp1250ToUtf8(InputStream in) throws IOException {
		Charset utf8 = Charset.forName("UTF-8");
		Charset sp1250 = Charset.forName("windows-1250");

		InputStreamReader reader = new InputStreamReader(in, sp1250);
		
		int rd;

		while ((rd = reader.read()) >= 0) {
			oStreamWriter.write(rd);
		}
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		depth++;
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		FileInputStream inStream = new FileInputStream(file.toFile());
		cp1250ToUtf8(inStream);
		inStream.close();
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		exc.printStackTrace();
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		if (exc != null)
			exc.printStackTrace();

		depth--;
		if(depth==0) oStreamWriter.close();
		return FileVisitResult.CONTINUE;
	}

	public void close() throws IOException {
		oStreamWriter.close();
	}
}

package tpo.cls1.zad1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.*;

public class MyFV implements FileVisitor<Path> {
	ByteBuffer buffer; // buffor danych pliku w UTF
	Path outPath; // wyjściowy plik
	int depth; // głębokość na jakiej się aktualnie znajduje
	public MyFV(Path outPath){
		this.buffer = ByteBuffer.allocate(0);
		this.outPath = outPath;
		depth=0;
	}
	
	/**
	 * Dodaje inBuf do naszego wewnetrznego buffera zamieniajac kodowanie z sp1250 do utf8
	 * @param inBuf
	 */
	public void cp1250ToUtf8(ByteBuffer inBuf) {
		Charset utf8 = Charset.forName("UTF-8");
		Charset sp1250 = Charset.forName("windows-1250");
		
		CharBuffer charBuf = sp1250.decode(inBuf);
		
		ByteBuffer staryBuf = this.buffer;
		ByteBuffer encoded = utf8.encode(charBuf);
		buffer = ByteBuffer.allocate(staryBuf.limit()+encoded.limit()); //zwiększenie wielkości naszego buffera
		buffer.put(staryBuf);
		
		buffer.put(encoded);
		buffer.flip();
		
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		depth++;
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	 	FileChannel fc = FileChannel.open(file);
	 	ByteBuffer byteBuff = ByteBuffer.allocate((int)attrs.size());
	 	fc.read(byteBuff);
	 	byteBuff.rewind();
	 	cp1250ToUtf8(byteBuff);
	 	return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		exc.printStackTrace();
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		if(exc != null)
			exc.printStackTrace();
		if(depth==1)
			flush();
		depth--;
		return FileVisitResult.CONTINUE;
	}

	private void flush(){
		FileChannel fc;
		try {
			fc = new FileOutputStream(outPath.toFile()).getChannel();
			fc.write(buffer);
			fc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

package utp.cls2.zad1;
import java.util.function.*;

public class InputConverter<E> {
	public E dane;
	public InputConverter(E dane){
		this.dane=dane;
	}
	//mam podejrzenie że ta metoda nie zwraca nie ale void przezże wyjście do odpowiedniej zmiennej
	public <T> T convertBy(Function... fs) {
		Function func=fs[fs.length-1];
		for(int i=fs.length-2; i>=0; i--) {
			func = func.compose(fs[i]);
		}
		
		return (T)func.apply(dane);
		
	}
	
}

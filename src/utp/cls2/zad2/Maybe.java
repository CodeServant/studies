package utp.cls2.zad2;

import java.util.NoSuchElementException;
import java.util.function.*;

public class Maybe<T> {
	T element;
	
	private Maybe() {
		
	}
	
	private Maybe(T x){
		this.element = x;
	}
	
	/**
	 * ta metoda statyczna zwraca obiekt Maybe, „opakowujący” wartość x, dowolnego typu referencyjnego.
	 * @param <T>
	 * @param x
	 * @return
	 */
	public static <T> Maybe<T> of(T x){
		return new Maybe<T>(x);
	}
	
	/**
	 * jeżeli w obiekcie Maybe znajduje się wartość, wykonywana jest operacja cons z tą wartością jako
	 *  argumentem, w przeciwnym razie - gdy obiekt Maybe jest pusty - nic się nie dzieje.
	 * @param cons
	 */
	public void ifPresent(Consumer<T> cons) {
		if(this.isPresent())
			cons.accept(this.element);
	}
	
	/**
	 * jeżeli w obiekcie  jest wartość, wykonywana jest funkcja func z tą wartością jako 
	 * argumentem i zwracany jest jej wynik „zapakowany” w nowy obiekt klasy Maybe 
	 * (to opakowanie jest niezbędne, bo wynik mógłby być null, a tego chcemy uniknąć w ewentualnym 
	 * dalszym przetwarzaniu; jeśli wynikiem funkcji jest null, zwracany jest pusty obiekt klasy Maybe).
	 * @param func
	 * @return
	 */
	public <R> Maybe<R> map(Function<T, R> func) {
		if(this.isPresent())
			return new Maybe<R>(func.apply(element));
		else
			return new Maybe<R>();
	}
	
	/**
	 * zwraca zawartość obiektu Maybe, ale jeśli jest on pusty, powinna zgłosić wyjątek NoSuchElementException.
	 * @return
	 */
	public T get() throws NoSuchElementException{
		if(!this.isPresent()) throw new NoSuchElementException("maybe is empty");
		return this.element;
	}
	
	/**
	 * zwraca true jeśli w obiekcie Maybe zawarta jest wartośc, a false - gdy jest on pusty
	 * @return
	 */
	public boolean isPresent() {
		return this.element!=null;
	}
	
	/**
	 * zwraca zawartość obiektu Maybe lub domyślną wartosć defVal, jeśli obiekt Maybe jest pusty.
	 * @param defVal dommyślna wartość jeżeli obiekt Maybe jest pusty
	 * @return
	 */
	public T orElse(T defVal) {
		if(this.isPresent()) return this.element;
		else return defVal;
	}
	
	/**
	 * zwraca  to Maybe, jeśli spełniony jest warunek pred lub to Maybe jest puste; zwraca puste
	 *  Maybe, jeśli warunek pred jest niespełniony.
	 * @param pred
	 * @return
	 */
	Maybe<T> filter(Predicate<T> pred) {
		if(this.isPresent() && pred.test(element)) return this;
		else return new Maybe<T>();
	}
	public String toString() {
		if(this.isPresent()) return "Maybe has value " + this.element.toString();
		else return "Maybe is empty";
	}
}

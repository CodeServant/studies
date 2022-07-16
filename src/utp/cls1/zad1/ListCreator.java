package utp.cls1.zad1;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class ListCreator<E> { // Uwaga: klasa musi być sparametrtyzowana
	//definicja listy wewnętrznej
	
	public List <E> lista;
	//konstruktor wkładający listę do list creator
	
	private ListCreator(List <E> l){
		lista=l;
	}
	
	public static <E> ListCreator<E> collectFrom(List<E> src) {
		return new ListCreator(src);
	}
	
	public ListCreator<E> when(Selector<E> sel){
		List<E> l = new LinkedList<E>();
		for(E element : this.lista) {
			if(sel.select(element))
				l.add(element);
		}
		return new ListCreator(l);
	}
	
	public <V> List<V> mapEvery(Mapper<E, V> map){
		List<V> l = new LinkedList<V>();
		for(E argument : this.lista) {
			l.add(map.map(argument));
		}
		return l;
	}
}  

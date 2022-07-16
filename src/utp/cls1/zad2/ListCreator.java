package utp.cls1.zad2;

import java.util.*;
import java.util.function.*;



public class ListCreator<E> {
	//będzie trzeba użyć interfejsów predicate i function
	public List <E> lista;
	//konstruktor wkładający listę do list creator
	
	private ListCreator(List <E> l){
		lista=l;
	}
	
	public static <E> ListCreator<E> collectFrom(List<E> src) {
		return new ListCreator(src);
	}
	
	public ListCreator<E> when(Predicate<E> sel){
		List<E> l = new LinkedList<E>();
		for(E element : this.lista) {
			if(sel.test(element))
				l.add(element);
		}
		return new ListCreator(l);
	}
	
	public <V> List<V> mapEvery(Function<E, V> map){
		List<V> l = new LinkedList<V>();
		for(E argument : this.lista) {
			l.add(map.apply(argument));
		}
		return l;
	}
}

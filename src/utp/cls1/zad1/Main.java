package utp.cls1.zad1;



import java.util.*;

public class Main {
  public Main() {
    List<Integer> src1 = Arrays.asList(1, 7, 9, 11, 12);
    System.out.println(test1(src1));

    List<String> src2 = Arrays.asList("a", "zzzz", "vvvvvvv" );
    System.out.println(test2(src2));
  }

  public List<Integer> test1(List<Integer> src) {
    Selector sel = new Selector<Integer>() {
    	public boolean select(Integer arg) {
    		return arg<10;
    	}
    };/*<-- definicja selektora; bez lambda-wyrażeń; nazwa zmiennej sel */
    Mapper map = new Mapper<Integer, Integer>(){
    	public Integer map(Integer arg) {
    		return arg+10;
    	}
    };/*<-- definicja mappera; bez lambda-wyrażeń; nazwa zmiennej map */
    return ListCreator.collectFrom(src).when(sel).mapEvery(map);
  }

  public List<String> test2(List<String> src) {
    Selector sel = new Selector<String>() {
    	public boolean select(String arg) {
    		return arg.length()>3;
    	}
    };/*<-- definicja selektora; bez lambda-wyrażeń; nazwa zmiennej sel */
    Mapper map = new Mapper<String, Integer>(){
    	public Integer map(String arg) {
    		return arg.length()+10;
    	}
    };  /*<-- definicja mappera; bez lambda-wyrażeń; nazwa zmiennej map */
    return  ListCreator.collectFrom(src).when(sel).mapEvery(map);
  }

  public static void main(String[] args) {
    new Main();
  }
}

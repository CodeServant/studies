package utp.cls2.zad1;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

/*<--
 *  niezbędne importy
 */
public class Main {
  public static void main(String[] args) {
	  Function<String, List<String>> flines = s -> {
			ArrayList lista = new ArrayList<String>();
			try {
				FileReader fr = new FileReader(s);
				BufferedReader br = new BufferedReader(fr);
				for(String line=br.readLine(); line!=null; line=br.readLine()) {
					
					lista.add(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return lista;
		};
		Function<List<String>, String> join = list -> {
			StringBuffer strb = new StringBuffer();
			int i=1;
			for(String s: list) {
				if(i<list.size())
					strb.append(s+" ");
				else
					strb.append(s);
				i++;
			}
			return strb.toString();
		};
		Function<String, List<Integer>> collectInts = s -> {
				Pattern ptrn = Pattern.compile("(-?\\d+)"); //uzupeĹ‚niÄ‡ regex
				Matcher mthr = ptrn.matcher(s);
				ArrayList<Integer> outList = new ArrayList<>();
				while(mthr.find()) {
					outList.add(Integer.parseInt(mthr.group(0)));
				}
				return outList;
		};
		Function<List<Integer>, Integer> sum = list -> {
			Integer outSum = 0;
			for(Integer i: list) {
				outSum+=i;
			}
			return outSum;
		};


    String fname = System.getProperty("user.home") + "/LamComFile.txt"; 
    InputConverter<String> fileConv = new InputConverter<>(fname);
    List<String> lines = fileConv.convertBy(flines);
    String text = fileConv.convertBy(flines, join);
    List<Integer> ints = fileConv.convertBy(flines, join, collectInts);
    Integer sumints = fileConv.convertBy(flines, join, collectInts, sum);

    System.out.println(lines);
    System.out.println(text);
    System.out.println(ints);
    System.out.println(sumints);

    List<String> arglist = Arrays.asList(args);
    InputConverter<List<String>> slistConv = new InputConverter<>(arglist);  
    sumints = slistConv.convertBy(join, collectInts, sum);
    System.out.println(sumints);

  }
}

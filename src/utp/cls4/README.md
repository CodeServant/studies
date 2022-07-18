<h1>utp-cls3</h1>

<h2>AnagraStream - zad1</h2>
<br>
Na     liście słów z http://wiki.puzzlers.org/pub/wordlists/unixdict.txt znaleźć     wszystkie anagramy.<br>Wypisać słowa z&nbsp;maksymalną liczbą anagramów oraz wszystkie ich anagramy w postaci:<br><br>slowo anagram1 anagram2 ....<br><br>Program ma być bardzo krótki , dzięki zastosowaniu przetwarzania strumieniowego (java.util.stream).<br>Bez tego rozwiązanie uzyska 0 punktów.<br><br><br> 


<h2>WallkTreeA - zad2</h2>
<br>
Katalog {user.home}/UTP6dir &nbsp;zawiera pliki
tekstowe (z rozszerzeniem .txt) umieszczone w różnych podkatalogach.
Kodowanie plików to Cp1250. <br>Przeglądając
rekursywnie drzewo katalogowe, zaczynające się od {user.home}/UTP6dir,
&nbsp;wczytać wszystkie te pliki. i połączoną ich zawartość zapisać do
pliku o nazwie UTP6res.txt, znadującym się w katalogu projektu.
Kodowanie pliku <br>UTP6res.txt winno być UTF-8.<br><br>Poniższy gotowy fragment&nbsp;winien wykonać całą robotę:<br><pre>      public class Main {<br>        public static void main(String[] args) {<br>          String dirName = System.getProperty("user.home")+"/UTP6dir";<br>          String resultFileName = "UTP6res.txt";<br>          Futil.processDir(dirName, resultFileName);<br>        }<br>      }</pre>Uwagi:<br><ul><li>pliku Main.java nie wolno w żaden sposób modyfikować,</li><li>trzeba dostarczyć definicji klasy Futil,</li><li>oczywiście, nazwa katalogu i pliku oraz ich położenie są obowiązkowe,</li><li>należy
zastosować FileVisitor do przeglądania katalogu,</li><li>proszę nie stosować środkow przetwarzania strumieniowego, na to będa oddzielne zadania,,</li><li>nalezy zalożyć, że na starcie programu&nbsp; wynikowego pliku nie ma.</li></ul><br><br><br><br>


<h2>WallkTreeB - zad3</h2><br>
Katalog {user.home}/UTP6dir &nbsp;zawiera pliki
tekstowe (z rozszerzeniem .txt) umieszczone w różnych podkatalogach.
Kodowanie plików to Cp1250. <br>Przeglądając
rekursywnie drzewo katalogowe, zaczynające się od {user.home}/UTP6dir,
&nbsp;wczytaać wszystkie te pliki. i połączoną ich zawartość zapisać do
pliku o nazwie UTP6res.txt, znadującym się w katalogu projektu.
Kodowanie pliku <br>UTP6res.txt winno być UTF-8.<br><br>Poniższy gotowy fragment&nbsp;winien wykonać całą robotę:<br><pre>      public class Main {<br>        public static void main(String[] args) {<br>          String dirName = System.getProperty("user.home")+"/UTP6dir";<br>          String resultFileName = "UTP6res.txt";<br>          Futil.processDir(dirName, resultFileName);<br>        }<br>      }</pre>Uwagi:<br><ul><li>pliku Main.java nie wolno w żaden sposób modyfikować,</li><li>trzeba dostarczyć definicji klasy Futil,</li><li>oczywiście, nazwa katalogu i pliku oraz ich położenie są obowiązkowe,</li><li style="font-weight: bold;">należy zastosować metody przetwarzania strumieniowego (chodzi o java.util.stream), bez tego uzyskujemy 0 punktów..</li></ul><br><br><br><br>

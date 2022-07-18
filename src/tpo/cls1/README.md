<h2>file channels - zad1</h2>
<br>
Katalog {user.home}/TPO1dir
&nbsp;zawiera pliki tekstowe umieszczone w tym katalogu i jego różnych
podkatalogach. Kodowanie plików to Cp1250. <br>Przeglądając
rekursywnie drzewo katalogowe, zaczynające się od {user.home}/TPO1dir,
&nbsp;wczytywać te pliki i dopisywać ich zawartości do pliku o nazwie
TPO1res.txt, znadującym się w katalogu projektu. Kodowanie pliku
TPO1res.txt winno być UTF-8, a po każdym uruchomieniu programu plik ten
powinien zawierać tylko aktualnie przeczytane&nbsp;dane z &nbsp;plików
katalogu/podkatalogow. <br><br>Poniższy gotowy fragment&nbsp;winien wykonać całą robotę:<br><pre>      public class Main {<br>        public static void main(String[] args) {<br>          String dirName = System.getProperty("user.home")+"/TPO1dir";<br>          String resultFileName = "TPO1res.txt";<br>          Futil.processDir(dirName, resultFileName);<br>        }<br>      }</pre>Uwagi:<br><ul><li>pliku Main.java nie wolno w żaden sposób modyfikować,</li><li>trzeba dostarczyć definicji klasy Futil,</li><li>oczywiście, nazwa katalogu i pliku oraz ich położenie są obowiązkowe,</li><li>należy
zastosować FileVisitor do przeglądania katalogu oraz kanały plikowe
(klasa FileChannel) do odczytu/zapisu plików (bez tego rozwiązanie nie
uzyska punktów).</li><li>w
wynikach testów mogą być przedstawione dodatkowe zalecenia co do
sposobu wykonania zadania (o ile rozwiązanie nie będzie jeszcze ich
uwzględniać),.</li></ul>

<h2>Web Clients - zad2</h2>
<br>
Napisać aplikację, udostępniającą GUI, w którym po podanu miasta i nazwy kraju pokazywane są:<br><ol><li>Informacje o aktualnej pogodzie w tym mieście.</li><li>Informacje o kursie wymiany walutu kraju wobec&nbsp;podanej przez uzytkownika waluty.</li><li>Informacje o kursie NBP złotego wobec tej waluty podanego kraju.</li><li>Strona wiki z opisem miasta.</li></ol>W
p. 1 użyć serwisu api.openweathermap.org, w p. 2 - serwisu&nbsp;exchangerate.host, w p. 3 - informacji ze stron NBP:
http://www.nbp.pl/kursy/kursya.html i
http://www.nbp.pl/kursy/kursyb.html.<br>W p. 4 użyć klasy WebEngine z JavaFX dla wbudowania przeglądarki w aplikację Swingową.<br><br>Program winien zawierać klasę Service z konstruktorem <span style="font-weight: bold;">Service(String kraj)</span> i&nbsp;metodami::<br><ul><li>String
getWeather(String miasto) - zwraca informację o pogodzie w podanym
mieście danego kraju&nbsp;w formacie JSON (to ma być pełna informacja
uzyskana z serwisu openweather - po prostu tekst w formacie JSON),</li><li>Double getRateFor(String kod_waluty) - zwraca kurs waluty danego kraju wobec waluty podanej jako argument,</li><li>Double getNBPRate() - zwraca kurs złotego wobec waluty danego kraju</li></ul>Następujące przykładowa klasa &nbsp;pokazuje możliwe użycie tych metod:<br><pre>public class Main {<br>  public static void main(String[] args) {<br>    Service s = new Service("Poland");<br>    String weatherJson = s.getWeather("Warsaw");<br>    Double rate1 = s.getRateFor("USD");<br>    Double rate2 = s.getNBPRate();<br>    // ...<br>    // część uruchamiająca GUI<br>  }<br>}<br><br></pre><span style="font-weight: bold;">Uwaga 1: zdefiniowanie pokazanych metod w sposób niezalezny od GUI jest obowiązkowe.</span><br><span style="font-weight: bold;">Uwaga 2</span>:
&nbsp;W katalogu projektu (np. w podkatalogu lib) nalezy umiescic
wykorzystywane&nbsp;JARy (w przeciwnym razie program nie przejdzie
kompilacji) i skonfigurowac Build Path tak, by wskazania na te JARy
byly w Build Path zawarte.
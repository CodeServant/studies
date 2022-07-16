package nieruchomosci.testy;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.security.DomainLoadStoreParameter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

import nieruchomosci.system.*;

public class TestSystemu {

	@AfterEach
	public void cleanSystem() {
		while (Osoba.getAllExtents().iterator().hasNext()) {
			Osoba.getAllExtents().iterator().next().remove();
		}
		while (Nieruchomosc.filtrujNieruchomosci(n -> true).iterator().hasNext()) {
			Nieruchomosc.filtrujNieruchomosci(n -> true).iterator().next().remove();
		}
	}

	@Test
	public void testUsuwanieObiektow() {
		try {
			// inicjowanie nieruchomosci
			String[] pomieszczenia = { "kuchnia", "salon", "sypialnia" };
			Dzialka dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),
					new BigDecimal(500), "mieszkalna", 1200);
			Dom dom = Dom.constructDom(dzialka, new BigDecimal(100000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			Mieszkanie mieszkanie = new Mieszkanie(dom, 100, 1, 2021, pomieszczenia, 5, RodzajeMieszkan.APARTAMENT);

			Dom dom2 = Dom.constructDom(dzialka, new BigDecimal(200000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			Dom dom3 = Dom.constructDom(dzialka, new BigDecimal(300000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			assertTrue(dzialka.getDomy().contains(dom2));
			assertTrue(dzialka.getDomy().contains(dom3));
			assertEquals(4, Nieruchomosc.liczbaNieruchomosci());
			assertEquals(2, Dom.liczbaDomow());
			assertEquals(1, Dzialka.liczbaDzialek());
			assertEquals(3, Zabudowa.liczbaZabudow());
			assertEquals(1, Mieszkanie.liczbaMieszkan());
			// inicjowanie osób
			Osoba osoba = new Osoba("Jan", "Kowalski", "1234pesel0");
			Osoba osoba2 = new Osoba("Jan", "Kalwasinki", "1242354122");
			Klient klient2 = Klient.createKlient(osoba2, LocalDate.now(), "osoba prywatna");
			Kupujacy kupujacy2 = Kupujacy.createKupujacy(klient2, "gotówka");
			Klient klient = Klient.createKlient(osoba, LocalDate.now(), "osoba prywatna");
			Sprzedajacy sprzedajacy = Sprzedajacy.sprzedajacyZNumerem(klient, "123456789");
			Kupujacy kupujacy = Kupujacy.createKupujacy(klient, "gotówka");
			Rzeczoznawca rzeczoznawca = Pracownik.createRzeczoznawca(osoba, LocalDate.of(2015, 11, 20), "o pracę",
					"jan", "1234", 5, new BigDecimal(2000));
			Handlowiec handlowiec2 = Pracownik.createHandlowiec(osoba2, LocalDate.of(2003, 10, 15), "o pracę", "john",
					"1234", new BigDecimal(2000), new BigDecimal("0.1"));
			assertEquals(2, osoba.liczbaOsob());
			assertEquals(2, klient.liczbaKlientow());
			assertEquals(1, sprzedajacy.liczbaSprzedajacych());
			assertEquals(2, kupujacy.liczbaKupujacych());
			assertEquals(2, Pracownik.liczbaPracownikow());
			assertEquals(1, Rzeczoznawca.liczbaRzeczoznawcow());
			assertEquals(1, Handlowiec.liczbaHandlowcow());
			// inicjowanie wyceny
			Wycena wyc = new Wycena(rzeczoznawca, dom2, LocalDate.of(2022, 2, 3), new BigDecimal(10000));
			new Wycena(rzeczoznawca, dom2, LocalDate.of(2022, 3, 3), new BigDecimal(10000));
			assertEquals(2, Wycena.filtruj((w) -> true).size());
			// usuwanie rzeczoznawcy
			rzeczoznawca.remove();
			assertEquals(0, Wycena.filtruj((w) -> true).size());
			assertEquals(1, osoba.liczbaOsob());
			assertEquals(1, klient.liczbaKlientow());
			assertEquals(0, sprzedajacy.liczbaSprzedajacych());
			assertEquals(1, kupujacy.liczbaKupujacych());
			assertEquals(1, Pracownik.liczbaPracownikow());
			assertEquals(0, Rzeczoznawca.liczbaRzeczoznawcow());
			assertEquals(1, Handlowiec.liczbaHandlowcow());
			handlowiec2.remove();
			assertEquals(0, osoba.liczbaOsob());
			assertEquals(0, klient.liczbaKlientow());
			assertEquals(0, sprzedajacy.liczbaSprzedajacych());
			assertEquals(0, kupujacy.liczbaKupujacych());
			assertEquals(0, Pracownik.liczbaPracownikow());
			assertEquals(0, Rzeczoznawca.liczbaRzeczoznawcow());
			assertEquals(0, Handlowiec.liczbaHandlowcow());

			mieszkanie.remove();
			assertEquals(3, Nieruchomosc.liczbaNieruchomosci());
			assertEquals(2, Dom.liczbaDomow());
			assertEquals(1, Dzialka.liczbaDzialek());
			assertEquals(2, Zabudowa.liczbaZabudow());
			assertEquals(0, Mieszkanie.liczbaMieszkan());

			dom2.remove();
			assertEquals(2, Nieruchomosc.liczbaNieruchomosci());
			assertEquals(1, Dom.liczbaDomow());
			assertEquals(1, Dzialka.liczbaDzialek());
			assertEquals(1, Zabudowa.liczbaZabudow());
			assertEquals(0, Mieszkanie.liczbaMieszkan());

			cleanSystem();
			assertEquals(0, Nieruchomosc.liczbaNieruchomosci());
			assertEquals(0, Dom.liczbaDomow());
			assertEquals(0, Dzialka.liczbaDzialek());
			assertEquals(0, Zabudowa.liczbaZabudow());
			assertEquals(0, Mieszkanie.liczbaMieszkan());

			// prezentacja
			dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),
					new BigDecimal(500), "mieszkalna", 1200);
			dom = Dom.constructDom(dzialka, new BigDecimal(100000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			mieszkanie = new Mieszkanie(dom, 100, 1, 2021, pomieszczenia, 5, RodzajeMieszkan.APARTAMENT);

			dom2 = Dom.constructDom(dzialka, new BigDecimal(200000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			dom3 = Dom.constructDom(dzialka, new BigDecimal(300000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			osoba = new Osoba("Jan", "Kowalski", "1234pesel0");
			osoba2 = new Osoba("Jan", "Kalwasinki", "1242354122");
			klient2 = Klient.createKlient(osoba2, LocalDate.now(), "osoba prywatna");
			kupujacy2 = Kupujacy.createKupujacy(klient2, "gotówka");
			klient = Klient.createKlient(osoba, LocalDate.now(), "osoba prywatna");
			sprzedajacy = Sprzedajacy.sprzedajacyZNumerem(klient, "123456789");
			kupujacy = Kupujacy.createKupujacy(klient, "gotówka");
			rzeczoznawca = Pracownik.createRzeczoznawca(osoba, LocalDate.of(2015, 11, 20), "o pracę", "jan", "1234", 5,
					new BigDecimal(2000));
			handlowiec2 = Pracownik.createHandlowiec(osoba2, LocalDate.of(2003, 10, 15), "o pracę", "john", "1234",
					new BigDecimal(2000), new BigDecimal("0.1"));
			wyc = new Wycena(rzeczoznawca, dom2, LocalDate.of(2022, 2, 3), new BigDecimal(10000));
			new Wycena(rzeczoznawca, dom2, LocalDate.of(2022, 3, 3), new BigDecimal(10000));
			List<Handlowiec> hds = new ArrayList<>();
			hds.add(handlowiec2);
			List<Kupujacy> kps = new ArrayList<>();
			kps.add(kupujacy);
			kps.add(kupujacy2);
			Prezentacja prez = new Prezentacja(dom, hds, kps, LocalDate.of(2023, 3, 3));

			cleanSystem();
			dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),
					new BigDecimal(500), "mieszkalna", 1200);
			dom = Dom.constructDom(dzialka, new BigDecimal(100000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			mieszkanie = new Mieszkanie(dom, 100, 1, 2021, pomieszczenia, 5, RodzajeMieszkan.APARTAMENT);

			dom2 = Dom.constructDom(dzialka, new BigDecimal(200000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			dom3 = Dom.constructDom(dzialka, new BigDecimal(300000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			osoba = new Osoba("Jan", "Kowalski", "1234pesel0");
			osoba2 = new Osoba("Jan", "Kalwasinki", "1242354122");
			klient2 = Klient.createKlient(osoba2, LocalDate.now(), "osoba prywatna");
			kupujacy2 = Kupujacy.createKupujacy(klient2, "gotówka");
			klient = Klient.createKlient(osoba, LocalDate.now(), "osoba prywatna");
			sprzedajacy = Sprzedajacy.sprzedajacyZNumerem(klient, "123456789");
			kupujacy = Kupujacy.createKupujacy(klient, "gotówka");
			rzeczoznawca = Pracownik.createRzeczoznawca(osoba, LocalDate.of(2015, 11, 20), "o pracę", "jan", "1234", 5,
					new BigDecimal(2000));
			handlowiec2 = Pracownik.createHandlowiec(osoba2, LocalDate.of(2003, 10, 15), "o pracę", "john", "1234",
					new BigDecimal(2000), new BigDecimal("0.1"));
			wyc = new Wycena(rzeczoznawca, dom2, LocalDate.of(2022, 2, 3), new BigDecimal(10000));

			cleanSystem();
			assertEquals(0, Nieruchomosc.liczbaNieruchomosci());
			assertEquals(0, Dom.liczbaDomow());
			assertEquals(0, Dzialka.liczbaDzialek());
			assertEquals(0, Zabudowa.liczbaZabudow());
			assertEquals(0, Mieszkanie.liczbaMieszkan());

			assertEquals(0, Osoba.liczbaOsob());
			assertEquals(0, Pracownik.liczbaPracownikow());
			assertEquals(0, Klient.liczbaKlientow());
			assertEquals(0, Kupujacy.liczbaKupujacych());
			assertEquals(0, Sprzedajacy.liczbaSprzedajacych());
			assertEquals(0, Rzeczoznawca.liczbaRzeczoznawcow());
			assertEquals(0, Handlowiec.liczbaHandlowcow());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testNieruchomosci() {
		String[] pomieszczenia = { "kuchnia", "salon", "sypialnia" };

		Dzialka dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),
				new BigDecimal(500), "mieszkalna", 1200);
		Dom dom = null;
		try {
			dom = Dom.constructDom(dzialka, new BigDecimal(100000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
		} catch (Exception e) {
			fail("dom nie buduje się poprawnie");
		}
		assertTrue(Dom.extenseContaint(dom));
		assertEquals(dzialka.getLokalizacja().getMiasto(), "Warszawa");
		assertEquals(dzialka.getLokalizacja().getKraj(), "Polska");
		assertEquals(Dom.liczbaDomow(), 1);
		assertEquals(Zabudowa.liczbaZabudow(), 1);
		assertEquals(Nieruchomosc.liczbaNieruchomosci(), 2);
		assertEquals(Dzialka.liczbaDzialek(), 1);
		assertEquals(dom, dzialka.getDomy().get(0));
		assertEquals(dzialka, dom.getDzialka());
		assertEquals(3, dom.liczbaPomieszczen());
		dom.addPomieszczenie("jadalnia");
		assertEquals(dom.liczbaPomieszczen(), 4);
		dom.addPomieszczenie("jadalnia");
		assertEquals(4, dom.liczbaPomieszczen());
		// wartości po zmianie na inny typ nieruchomości
		Mieszkanie mieszkanie = null;
		try {
			mieszkanie = new Mieszkanie(dom, 100, 1, 2021, pomieszczenia, 5, RodzajeMieszkan.APARTAMENT);
		} catch (Exception e) {
			fail("inicjowanie mieszkania niepowiedzione");
		}
		assertEquals(0, Dom.liczbaDomow());
		assertEquals(1, Zabudowa.liczbaZabudow());
		assertEquals(2, Nieruchomosc.liczbaNieruchomosci());
		assertEquals(1, Dzialka.liczbaDzialek());
		assertEquals(0, dzialka.getDomy().size());
		assertEquals(null, dom.getDzialka());
		Dzialka.usunDzialke(dzialka);
		assertEquals(0, Dzialka.liczbaDzialek());
		assertEquals(1, Nieruchomosc.liczbaNieruchomosci());
		assertEquals("Warszawa", mieszkanie.getLokalizacja().getMiasto());
		assertEquals(0, mieszkanie.getAtrakcje().size());
		mieszkanie.addAtrakcje("piaskownica");
		assertEquals("piaskownica", mieszkanie.getAtrakcje().get(0));
	}

	@Test
	public void testOsoby() {
		try {
			Osoba osoba = new Osoba("Jan", "Kowalski", "1234567890");

			assertThrows(Exception.class, () -> new Osoba("Jan", "Kwaśniewski", "1234567890"),
					"osoba o tym peselu już istnieje");

			assertEquals("Jan", osoba.getImie());
			assertEquals(1, Osoba.liczbaOsob());
			assertEquals(0, Klient.liczbaKlientow());
			Klient klient = Klient.createKlient(osoba, LocalDate.now(), "osoba prywatna");
			assertTrue(osoba.isKlient());
			assertFalse(osoba.isPracownik());
			assertFalse(klient.isKupujacy());
			assertFalse(klient.isSprzedajacy());
			assertEquals("Jan", klient.getImie());

			Integer[] expectedArray = { 1, 1, 0, 0, 0 };
			Integer[] actualArray = { Osoba.liczbaOsob(), Klient.liczbaKlientow(), Sprzedajacy.liczbaSprzedajacych(),
					Pracownik.liczbaPracownikow(), Kupujacy.liczbaKupujacych() };
			assertArrayEquals(expectedArray, actualArray);

			Sprzedajacy sprzedajacy = Sprzedajacy.sprzedajacyZNumerem(klient, "123456789");
			Kupujacy kupujacy = Kupujacy.createKupujacy(klient, "gotówka");
			expectedArray = new Integer[] { 1, 1, 1, 0, 1 };
			actualArray = new Integer[] { Osoba.liczbaOsob(), Klient.liczbaKlientow(),
					Sprzedajacy.liczbaSprzedajacych(), Pracownik.liczbaPracownikow(), Kupujacy.liczbaKupujacych() };
			assertArrayEquals(expectedArray, actualArray);

			assertTrue(osoba.isKlient());
			assertFalse(osoba.isPracownik());
			assertTrue(klient.isSprzedajacy());
			assertTrue(klient.isKupujacy());
			assertSame(klient, osoba.getKlient());
			assertSame(sprzedajacy, osoba.getKlient().getSprzedajacy());
			assertSame(osoba, kupujacy.getOsoba());
			assertSame("Jan", kupujacy.getImie());
			assertSame(klient, kupujacy.getKlient());
			assertSame(klient, sprzedajacy.getKlient());
			Rzeczoznawca pracownik = Pracownik.createRzeczoznawca(osoba, LocalDate.of(2015, 11, 20), "o pracę", "jan",
					"1234", 5, new BigDecimal(2000));
			Osoba os2 = new Osoba("Jan2", "Kowalski", "1246567890");

			assertThrows(Exception.class, () -> Pracownik.createRzeczoznawca(os2, LocalDate.of(2015, 11, 20), "o pracę",
					"jan", "1234", 5, new BigDecimal(2000)), "login powinien być unikalny");
			os2.remove();

			expectedArray = new Integer[] { 1, 1, 1, 1, 1, 1, 0 };
			actualArray = new Integer[] { Osoba.liczbaOsob(), Klient.liczbaKlientow(),
					Sprzedajacy.liczbaSprzedajacych(), Pracownik.liczbaPracownikow(), Kupujacy.liczbaKupujacych(),
					Rzeczoznawca.liczbaRzeczoznawcow(), Handlowiec.liczbaHandlowcow() };
			assertArrayEquals(expectedArray, actualArray);
			assertThrows(Exception.class, () -> Pracownik.createRzeczoznawca(osoba, LocalDate.of(2015, 11, 20),
					"o pracę", "jan", "1234", 5, new BigDecimal(200)));
			// sprawdzam czy po nieudanym stworzeniu pracownika będą dobre licznosci
			expectedArray = new Integer[] { 1, 1, 1, 1, 1, 1, 0 };
			actualArray = new Integer[] { Osoba.liczbaOsob(), Klient.liczbaKlientow(),
					Sprzedajacy.liczbaSprzedajacych(), Pracownik.liczbaPracownikow(), Kupujacy.liczbaKupujacych(),
					Rzeczoznawca.liczbaRzeczoznawcow(), Handlowiec.liczbaHandlowcow() };
			assertThrows(Exception.class,
					() -> Pracownik.createHandlowiec(osoba, LocalDate.of(2003, 10, 15), "o pracę", "john", "1234",
							new BigDecimal(2000), new BigDecimal("0.1")),
					"nie moze być jednosczesnie handlowcem i rzeczoznawca");
			assertEquals("jan", pracownik.getLogin());
			assertTrue(pracownik.verifyPassword("1234"));
			expectedArray = new Integer[] { 1, 1, 1, 1, 1, 1, 0 };
			actualArray = new Integer[] { Osoba.liczbaOsob(), Klient.liczbaKlientow(),
					Sprzedajacy.liczbaSprzedajacych(), Pracownik.liczbaPracownikow(), Kupujacy.liczbaKupujacych(),
					Rzeczoznawca.liczbaRzeczoznawcow(), Handlowiec.liczbaHandlowcow() };

			assertTrue(!Handlowiec.jestHandlowecem(klient));
			assertTrue(Rzeczoznawca.jestRzeczoznawca(klient));

			// test powtarzania i usuwania
			Osoba os3 = new Osoba("Jan", "Kowalski", "9876543210");
			os3.remove();
			os3 = new Osoba("John", "Kowalski", "9876543210");

			cleanSystem();
			assertEquals(0, Nieruchomosc.liczbaNieruchomosci());
			assertEquals(0, Dom.liczbaDomow());
			assertEquals(0, Dzialka.liczbaDzialek());
			assertEquals(0, Zabudowa.liczbaZabudow());
			assertEquals(0, Mieszkanie.liczbaMieszkan());

			assertEquals(0, Osoba.liczbaOsob());
			assertEquals(0, Pracownik.liczbaPracownikow());
			assertEquals(0, Klient.liczbaKlientow());
			assertEquals(0, Kupujacy.liczbaKupujacych());
			assertEquals(0, Sprzedajacy.liczbaSprzedajacych());
			assertEquals(0, Rzeczoznawca.liczbaRzeczoznawcow());
			Handlowiec.getHandlowcy()
					.forEach(h -> System.out.print(h.getImie() + " " + h.getPesel() + " " + h.getLogin()));
			;
			assertEquals(0, Handlowiec.liczbaHandlowcow());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testPrezentacja() {

		try {
			Dzialka dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),
					new BigDecimal(500), "mieszkalna", 1200);
			Osoba osKupuj = new Osoba("Marek", "Zakupowicz", "3234567890");
			Osoba osHandl = new Osoba("Jaroslaw", "Handlarczyk", "2234567890");
			Klient klient = Klient.createKlient(osKupuj, LocalDate.of(2000, 10, 10), "osoba prywatna");

			Handlowiec handl = Pracownik.createHandlowiec(osHandl, LocalDate.of(2015, 11, 20), "o pracę", "jan", "1234",
					new BigDecimal(2000), new BigDecimal("0.1"));
			Kupujacy kupujacy = Kupujacy.createKupujacy(klient, "gotówka");
			List<Kupujacy> listaKupujacych = new ArrayList<>();
			listaKupujacych.add(kupujacy);
			List<Handlowiec> listaHandlowcow = new ArrayList<>();
			listaHandlowcow.add(handl);
			Prezentacja prezentacja = new Prezentacja(dzialka, listaHandlowcow, listaKupujacych,
					LocalDate.of(2023, 10, 10));

			// asocjacje nieruchomości
			assertSame(dzialka, prezentacja.getNieruchomosc());
			assertTrue(dzialka.getPrezentacje().contains(prezentacja));
			// asocjacje handlarza
			assertTrue(prezentacja.getHandlowcy().contains(handl));
			assertTrue(handl.getPrezentacje().contains(prezentacja));
			// asocjacje kupujaceego
			assertTrue(prezentacja.getNabywcy().contains(kupujacy));
			assertTrue(kupujacy.getPrezentacje().contains(prezentacja));
			assertEquals(1, Prezentacja.liczbaPrezentacji());

			// usuwanie
			prezentacja.remove();
			assertEquals(0, Prezentacja.liczbaPrezentacji());
			// asocjacje nieruchomości
			assertNotSame(dzialka, prezentacja.getNieruchomosc());
			assertFalse(dzialka.getPrezentacje().contains(prezentacja));
			// asocjacje handlarza
			assertFalse(prezentacja.getHandlowcy().contains(handl));
			assertFalse(handl.getPrezentacje().contains(prezentacja));
			// asocjacje kupujaceego
			assertFalse(prezentacja.getNabywcy().contains(kupujacy));
			assertFalse(kupujacy.getPrezentacje().contains(prezentacja));

			prezentacja = new Prezentacja(dzialka, listaHandlowcow, listaKupujacych, LocalDate.of(2023, 10, 10));
			assertEquals(1, Prezentacja.liczbaPrezentacji());
			handl.delPrezentacja(prezentacja);
			assertEquals(0, Prezentacja.liczbaPrezentacji());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testWycena() {
		try {
			String[] pomieszczenia = { "kuchnia", "salon", "sypialnia" };
			Dzialka dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),
					new BigDecimal(500), "mieszkalna", 1200);
			Dom dom = Dom.constructDom(dzialka, new BigDecimal(100000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			Mieszkanie mieszkanie = new Mieszkanie(dom, 100, 1, 2021, pomieszczenia, 5, RodzajeMieszkan.APARTAMENT);

			Dom dom2 = Dom.constructDom(dzialka, new BigDecimal(200000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			Dom dom3 = Dom.constructDom(dzialka, new BigDecimal(300000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			Osoba osoba = new Osoba("Jan", "Kowalski", "1234pesel0");
			Osoba osoba2 = new Osoba("Jan", "Kalwasinki", "1242354122");

			Rzeczoznawca rzeczoznawca = Pracownik.createRzeczoznawca(osoba, LocalDate.of(2015, 11, 20), "o pracę",
					"jan", "1234", 5, new BigDecimal(2000));
			Handlowiec handlowiec2 = Pracownik.createHandlowiec(osoba2, LocalDate.of(2003, 10, 15), "o pracę", "john",
					"1234", new BigDecimal(2000), new BigDecimal("0.1"));

			assertThrows(Exception.class, () -> new Wycena(rzeczoznawca, dom, LocalDate.now(), new BigDecimal(10000)));
			assertEquals(0, Wycena.liczbaWycen());
			Wycena wyc = new Wycena(rzeczoznawca, mieszkanie, LocalDate.now(), new BigDecimal(10000)); // nieruchomosc
			
			assertEquals(1, Wycena.liczbaWycen());
			assertSame(rzeczoznawca, wyc.getRzeczoznawca());
			assertSame(mieszkanie, wyc.getNieruchomosc());
			wyc.remove();
			assertEquals(0, Wycena.liczbaWycen());
			assertNotSame(rzeczoznawca, wyc.getRzeczoznawca());
			assertNotSame(mieszkanie, wyc.getNieruchomosc());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUmowa() {

		try {
			String[] pomieszczenia = { "kuchnia", "salon", "sypialnia" };
			Dzialka dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),

					new BigDecimal(500), "mieszkalna", 1200);
			Dom dom = Dom.constructDom(dzialka, new BigDecimal(100000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			Mieszkanie mieszkanie = new Mieszkanie(dom, 100, 1, 2021, pomieszczenia, 5, RodzajeMieszkan.APARTAMENT);

			Dom dom2 = Dom.constructDom(dzialka, new BigDecimal(200000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			Dom dom3 = Dom.constructDom(dzialka, new BigDecimal(300000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			Osoba osoba = new Osoba("Jan", "Kowalski", "1234pesel0");
			Osoba osoba2 = new Osoba("Jan", "Kalwasinki", "1242354122");
			Klient klient2 = Klient.createKlient(osoba2, LocalDate.now(), "osoba prywatna");
			Kupujacy kupujacy2 = Kupujacy.createKupujacy(klient2, "gotówka");
			Klient klient = Klient.createKlient(osoba, LocalDate.now(), "osoba prywatna");
			Sprzedajacy sprzedajacy = Sprzedajacy.sprzedajacyZNumerem(klient, "123456789");
			Kupujacy kupujacy = Kupujacy.createKupujacy(klient, "gotówka");
			Rzeczoznawca rzeczoznawca = Pracownik.createRzeczoznawca(osoba, LocalDate.of(2015, 11, 20), "o pracę",
					"jan", "1234", 5, new BigDecimal(2000));
			Handlowiec handlowiec2 = Pracownik.createHandlowiec(osoba2, LocalDate.of(2003, 10, 15), "o pracę", "john",
					"1234", new BigDecimal(2000), new BigDecimal("0.1"));
			Wycena wyc = new Wycena(rzeczoznawca, dom2, LocalDate.of(2022, 2, 3), new BigDecimal(10000));

			List<Nieruchomosc> nieruchms = new ArrayList<Nieruchomosc>();
			nieruchms.add(mieszkanie);
			nieruchms.add(dom3);
			Umowa umowa = new Umowa(nieruchms, sprzedajacy, LocalDate.of(2020, 1, 1), LocalDate.of(2022, 1, 1),
					"idaktu");
			assertEquals(1, Umowa.liczbaUmow());
			assertEquals(2, umowa.getNieruchomosci().size());
			assertTrue(umowa.getNieruchomosci().contains(nieruchms.get(0)));
			assertSame(sprzedajacy, umowa.getSprzedajacy());
			umowa.delUmowa();
			assertEquals(0, Umowa.liczbaUmow());
			assertFalse(umowa.getNieruchomosci().contains(nieruchms.get(0)));
			assertNotSame(sprzedajacy, umowa.getSprzedajacy());

			umowa = new Umowa(nieruchms, sprzedajacy, LocalDate.of(2020, 1, 1), LocalDate.of(2022, 1, 1), "idaktu");
			assertEquals(1, Umowa.liczbaUmow());
			mieszkanie.delUmowa(umowa);
			assertEquals(1, Umowa.liczbaUmow());
			assertFalse(umowa.getNieruchomosci().contains(mieszkanie));
			osoba.remove();
			osoba2.remove();
			assertEquals(0, Umowa.liczbaUmow());

			// nowa init
			dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),

					new BigDecimal(500), "mieszkalna", 1200);
			dom = Dom.constructDom(dzialka, new BigDecimal(100000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			mieszkanie = new Mieszkanie(dom, 100, 1, 2021, pomieszczenia, 5, RodzajeMieszkan.APARTAMENT);

			dom2 = Dom.constructDom(dzialka, new BigDecimal(200000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			dom3 = Dom.constructDom(dzialka, new BigDecimal(300000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			osoba = new Osoba("Jan", "Kowalski", "1234pesel0");
			osoba2 = new Osoba("Jan", "Kalwasinki", "1242354122");
			klient2 = Klient.createKlient(osoba2, LocalDate.now(), "osoba prywatna");
			kupujacy2 = Kupujacy.createKupujacy(klient2, "gotówka");
			klient = Klient.createKlient(osoba, LocalDate.now(), "osoba prywatna");
			sprzedajacy = Sprzedajacy.sprzedajacyZNumerem(klient, "123456789");
			kupujacy = Kupujacy.createKupujacy(klient, "gotówka");
			rzeczoznawca = Pracownik.createRzeczoznawca(osoba, LocalDate.of(2015, 11, 20), "o pracę", "jan", "1234", 5,
					new BigDecimal(2000));
			handlowiec2 = Pracownik.createHandlowiec(osoba2, LocalDate.of(2003, 10, 15), "o pracę", "john", "1234",
					new BigDecimal(2000), new BigDecimal("0.1"));
			wyc = new Wycena(rzeczoznawca, dom2, LocalDate.of(2022, 2, 3), new BigDecimal(10000));

			nieruchms = new ArrayList<Nieruchomosc>();
			nieruchms.add(mieszkanie);
			nieruchms.add(dom3);
			umowa = new Umowa(nieruchms, sprzedajacy, LocalDate.of(2020, 1, 1), LocalDate.of(2022, 1, 1), "idaktu");
			sprzedajacy.remove();
			osoba2.remove();
			assertEquals(0, Umowa.liczbaUmow());

			// nowa init
			dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),

					new BigDecimal(500), "mieszkalna", 1200);
			dom = Dom.constructDom(dzialka, new BigDecimal(100000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			mieszkanie = new Mieszkanie(dom, 100, 1, 2021, pomieszczenia, 5, RodzajeMieszkan.APARTAMENT);

			dom2 = Dom.constructDom(dzialka, new BigDecimal(200000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			dom3 = Dom.constructDom(dzialka, new BigDecimal(300000), new BigDecimal(500), 200, 2020, 2, pomieszczenia,
					110);
			osoba = new Osoba("Jan", "Kowalski", "1234pesel0");
			osoba2 = new Osoba("Jan", "Kalwasinki", "1242354122");
			klient2 = Klient.createKlient(osoba2, LocalDate.now(), "osoba prywatna");
			kupujacy2 = Kupujacy.createKupujacy(klient2, "gotówka");
			klient = Klient.createKlient(osoba, LocalDate.now(), "osoba prywatna");
			sprzedajacy = Sprzedajacy.sprzedajacyZNumerem(klient, "123456789");
			kupujacy = Kupujacy.createKupujacy(klient, "gotówka");
			rzeczoznawca = Pracownik.createRzeczoznawca(osoba, LocalDate.of(2015, 11, 20), "o pracę", "jan", "1234", 5,
					new BigDecimal(2000));
			handlowiec2 = Pracownik.createHandlowiec(osoba2, LocalDate.of(2003, 10, 15), "o pracę", "john", "1234",
					new BigDecimal(2000), new BigDecimal("0.1"));
			wyc = new Wycena(rzeczoznawca, dom2, LocalDate.of(2022, 2, 3), new BigDecimal(10000));

			nieruchms = new ArrayList<Nieruchomosc>();
			nieruchms.add(mieszkanie);
			nieruchms.add(dom3);
			umowa = new Umowa(nieruchms, sprzedajacy, LocalDate.of(2020, 1, 1), LocalDate.of(2022, 1, 1), "idaktu");
			kupujacy.remove();
			assertEquals(0, Umowa.liczbaUmow());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testKompozycja() {
		try {
			String[] pomieszczenia = { "kuchnia", "salon", "sypialnia" };

			// usuwanie całości przy usuwaniu części
			Dzialka dzialka = new Dzialka(new BigDecimal(100000), new Lokalizacja("Warszawa", "Kwiatowa", "1d"),
					new BigDecimal(500), "mieszkalna", 1200);
			Dom dom = Dom.constructDom(dzialka, new BigDecimal(100000), new BigDecimal(500), 200, 2020, 2,
					pomieszczenia, 110);
			assertTrue(dzialka.getDomy().contains(dom));

			dzialka.remove();

			assertEquals(0, Nieruchomosc.liczbaNieruchomosci());
			assertEquals(0, Zabudowa.liczbaZabudow());
			assertEquals(0, Dom.liczbaDomow());
			assertEquals(0, Dzialka.liczbaDzialek());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

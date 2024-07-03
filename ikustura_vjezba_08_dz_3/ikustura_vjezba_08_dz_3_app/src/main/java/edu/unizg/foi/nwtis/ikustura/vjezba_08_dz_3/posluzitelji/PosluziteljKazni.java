package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.konfiguracije.*;

// TODO: Auto-generated Javadoc
/**
 * Klasa PoslužiteljKazni - zapisuje kazne, daje statističke podatke o kaznama i vraća najnoviju kaznu vozila
 */
public class PosluziteljKazni {
  
  /** sdf - objekt za rukovanje s datumom*/
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
  
  /** The mrežna vrata - mrežna vrata poslužitelja kazni */
  int mreznaVrata;
  
  /** Predložak kazna - regularni izraz prema kojem provjeravamo komande */
  private Pattern predlozakKazna = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+) (?<brzina>-?\\d+([.]\\d+)?) "
      + "(?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) "
      + "(?<gpsSirinaRadar>\\d+[.]\\d+) (?<gpsDuzinaRadar>\\d+[.]\\d+)$");

  /** Poklapanje kazna - koristimo za provjeru teksta s predloškom*/
  private Matcher poklapanjeKazna;
  
  /** Predložak vozilo - regularni izraz prema kojem provjeravamo komandu za dohvaćanje kazne vozila */
  private Pattern predlozakVozilo = Pattern.compile
      ("^VOZILO (?<id>\\d+) (?<vrijemeOd>\\d+) (?<vrijemeDo>\\d+)$");
  
  /** Poklapanje vozilo - koristi se za provjeru teksta s predloškom predlozakVozilo */
  private Matcher poklapanjeVozilo;
  
  /** The predlozak statistika - regularni izraz prema kojem provjeravamo komandu za dohvat statistike kazni */
  private Pattern predlozakStatistika = Pattern.compile
      ("^STATISTIKA (?<vrijemeOd>\\d+) (?<vrijemeDo>\\d+)$");
  
  /** The poklapanje statistika - koristi se za provjeru poklapanja teksta s predlozakStatistika */
  private Matcher poklapanjeStatistika;
  
  private Pattern predlozakTest = Pattern.compile("^TEST$");
  private Matcher poklapanjeTest;
  public KaznaRestKlijent klijent = new KaznaRestKlijent();
  
  /** Sve kazne - red koji zapisuje sve kazne vozila */
  private volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();

  /**
   * Glavna metoda 
   *
   * @param args - konfiguracijska datoteka PoslužiteljaKazni
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    try {
      posluziteljKazni.preuzmiPostavke(args);

      posluziteljKazni.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Pokreni posluzitelja - otvaranje mrežne utičnice PoslužiteljaKazni
   */
  public void pokreniPosluzitelja() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader citac =
            new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
        OutputStream out = mreznaUticnica.getOutputStream();
        PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
        var redak = citac.readLine();

        mreznaUticnica.shutdownInput();
        pisac.println(obradaZahtjeva(redak));

        pisac.flush();
        mreznaUticnica.shutdownOutput();
        mreznaUticnica.close();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Obrada zahtjeva - obrada zahtjeva klijenta
   *
   * @param zahtjev - klijentov zahtjev
   * @return odgovor nakon obrade zahtjeva
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 40 Neispravna sintaksa komande.\n";
    }
    var odgovor = obradaZahtjevaKazna(zahtjev);
    if(odgovor == null) {
    	odgovor = obradaVozila(zahtjev);
    }
    if(odgovor == null) {
    	odgovor = obradaZahtjevaStatistika(zahtjev);
    }
    if(odgovor == null) {
      odgovor = obradaZahtjevaTest(zahtjev);
    }
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 40 Neispravna sintaksa komande!\n";
  }

  

private String obradaZahtjevaTest(String zahtjev) {
    this.poklapanjeTest = this.predlozakTest.matcher(zahtjev);
    var statusTest = this.poklapanjeTest.matches();
    if(statusTest) {
      return "OK";
    }
    return null;
  }

/**
 * Obrada zahtjeva kazna - obrađuje se zahtjev za dodavanje kazne
 *
 * @param zahtjev - Zahtjev za dodavanje nove kazne
 * @return rezultat OK ako je dodavanje uspješno, null ako nije 
 */
public String obradaZahtjevaKazna(String zahtjev) {
    this.poklapanjeKazna = this.predlozakKazna.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var kazna = new PodaciKazne(Integer.valueOf(this.poklapanjeKazna.group("id")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemePocetak")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemeKraj")),
          Double.valueOf(this.poklapanjeKazna.group("brzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirinaRadar")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzinaRadar")));

      this.sveKazne.add(kazna);
      System.out.println("Id: " + kazna.id() + " Vrijeme od: " + sdf.format(kazna.vrijemePocetak())
          + "  Vrijeme do: " + sdf.format(kazna.vrijemeKraj()) + " Brzina: " + kazna.brzina()
          + " GPS: " + kazna.gpsSirina() + ", " + kazna.gpsDuzina());
      //klijent.postKaznaJSON(kazna);
      return "OK\n";
    }
    return null;
  }
  
  /**
   * Obrada vozila - dohvaćanje najnovije kazne
   *
   * @param zahtjev zahtjev za dohvaćanje najnovije kazne
   * @return rezultat OK ako je dohvaćanje uspješno, null ako nije
   */
  public String obradaVozila(String zahtjev) {
	  this.poklapanjeVozilo = this.predlozakVozilo.matcher(zahtjev);
	  var statusVozilo = poklapanjeVozilo.matches();
	  if(statusVozilo) {
		  PodaciKazne pronadenaKazna = null;
		  for (PodaciKazne kazna : sveKazne) {
			  if(kazna.id() == Integer.valueOf(this.poklapanjeVozilo.group("id"))
			     && kazna.vrijemePocetak() >= Long.valueOf(this.poklapanjeVozilo.group("vrijemeOd"))
			     && kazna.vrijemeKraj() <= Long.valueOf(this.poklapanjeVozilo.group("vrijemeDo"))
			     ) {
				  if(pronadenaKazna == null || pronadenaKazna.vrijemePocetak() < kazna.vrijemePocetak()) {
					  pronadenaKazna = kazna;
				  }
			  }
		  }
		  if(pronadenaKazna != null) {
			  return "OK " + pronadenaKazna.vrijemePocetak() + " " + pronadenaKazna.brzina() + " " + 
					  pronadenaKazna.gpsSirinaRadar() + " " + pronadenaKazna.gpsDuzinaRadar()+ "\n";
		  }
		  else {
			  return "ERROR 41 Ne postoji kazna za vozilo u zadanom vremenskom periodu.\n";
		  } 
	  }
	  return null;
  }
  
  /**
   * Obrada zahtjeva statistika - obrađuje se zahtjev 
   *
   * @param zahtjev Zahtjev za dohvaćanje broja kazni vozila
   * @return rezultat OK ako je dohvaćanje uspješno, null ako nije, pogrešku ako nema kazni u rasponu
   */
  public String obradaZahtjevaStatistika(String zahtjev) {
		this.poklapanjeStatistika = this.predlozakStatistika.matcher(zahtjev);
		var statistika = poklapanjeStatistika.matches();
		if(statistika) {
			Map<Integer, Integer> brojKazniPoVozilu = new ConcurrentHashMap<>();
			for(PodaciKazne kazna : sveKazne) {
				if(kazna.vrijemePocetak() >= Long.valueOf(this.poklapanjeStatistika.group("vrijemeOd")) &&
				kazna.vrijemeKraj() <= Long.valueOf(this.poklapanjeStatistika.group("vrijemeDo"))) {
					brojKazniPoVozilu.put(kazna.id(), brojKazniPoVozilu.getOrDefault(kazna.id(), 0)+1);
				}
			}
			String odgovor = "OK\n";
			for(Map.Entry<Integer, Integer> zapis : brojKazniPoVozilu.entrySet()) {
			
				odgovor += zapis.getKey() + " " + zapis.getValue() + "; ";
			}
			if(odgovor == "OK\n") {
				return "ERROR 49 ne postoje kazne vozila u zadanom vremenskom rasponu\n";
			}
			else {
				return odgovor;
			}
		}
		return null;
	}

  /**
   * Preuzmi postavke.
   *
   * @param args Argumenti koje zadaje klijent
   * @throws NeispravnaKonfiguracija Neispravna konfiguracija
   * @throws NumberFormatException Iznimka za rukovanje s pogreškama u parsiranju
   * @throws UnknownHostException Iznimka za probleme s dohvaćanjem IP adrese
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.mreznaVrata = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
    
  }
}

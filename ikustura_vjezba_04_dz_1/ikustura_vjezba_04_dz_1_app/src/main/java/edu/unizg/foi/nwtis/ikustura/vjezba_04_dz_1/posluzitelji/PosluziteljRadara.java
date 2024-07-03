package edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji.radnici.RadnikZaRadare;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

// TODO: Auto-generated Javadoc
/**
 * Klasa PosluziteljRadara - registracija poslužitelja radara i višedretveni rad 
 */
public class PosluziteljRadara {

	/** Tvornica dretvi - za kreiranje dretvi */
	private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();
	
	/** Podaci radara - zapis iz konfiguracijske datoteke */
	private PodaciRadara podaciRadara;
	
	/** Pracenje vozila - lista vozila koji imaju preveliku brzinu */
	public volatile Queue<BrzoVozilo> pracenjeVozila = new ConcurrentLinkedQueue<>();
	
  /**
   * Glavna metoda 
   *
   * @param args Argumenti za registriranje radara, brisanje određenog radara ili svih radara
   */
  public static void main(String[] args) {
    PosluziteljRadara posluziteljRadara = new PosluziteljRadara();
    int brojArgumenata = args.length;
    if(brojArgumenata == 1 || brojArgumenata == 3) {
      try {
        posluziteljRadara.preuzmiPostavke(args);
        if(brojArgumenata == 1) {
          posluziteljRadara.registracijaPosluzitelja();
          posluziteljRadara.pokreniPosluzitelja();
        }
        if(brojArgumenata == 3) {
          posluziteljRadara.obrisiRadar(args);
        }

      } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
        System.out.println(e.getMessage());
        return;
      }
    }
   
  }

  /**
   * Obriši radar - šalje se komanda za brisanje radara poslužitelju
   *
   * @param args - definirani argumenti u main funkciji
   * @return OK ako je sve u redu, podrešku ako format nije ispravan 
   */
  private String obrisiRadar(String[] args) {
    var komanda = new StringBuilder ();
    var razmak = " ";
    komanda.append("OBRIŠI").append(razmak).append(args[2]).append("\n");
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(), 
        this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());
    if(odgovor == null) {
      return "ERROR 30 Format komande nije ispravan \n";
    }
    return "OK\n";
  }

  /**
   * Registracija posluzitelja - dodavanje novog radara, slanje zahtjeva poslužitelju
   *
   * @return istina, ako je uspješno
   */
  private boolean registracijaPosluzitelja() {
	var komanda = new StringBuilder ();
	var razmak = " ";
	komanda.append("RADAR").append(razmak).append(this.podaciRadara.id()).append(razmak).
	append(this.podaciRadara.adresaRadara()).append(razmak).
	append(this.podaciRadara.mreznaVrataRadara()).append(razmak).
	append(this.podaciRadara.gpsSirina()).append(razmak).
	append(this.podaciRadara.gpsDuzina()).append(razmak).
	append(this.podaciRadara.maksUdaljenost());
	var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(), 
			this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());
	if(odgovor == null) {
		return false;
	}
	else {
		return true;
	}
}

/**
 * Pokretanje Radnika za radare kreiranjem nove dretve 
 */
public void pokreniPosluzitelja() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = 
    		new ServerSocket(this.podaciRadara.mreznaVrataRadara())) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        var radnik = new RadnikZaRadare(mreznaUticnica, this.podaciRadara, this);
        var dretva = tvornicaDretvi.newThread(radnik);
        dretva.start();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Preuzmi postavke -  preuzimaju se postavke iz konfiguracijske datoteke.
   *
   * @param args Argumenti koje zadaje klijent
   * @throws NeispravnaKonfiguracija Neispravna konfiguracija
   * @throws NumberFormatException Iznimka za rukovanje s pogreškama u parsiranju
   * @throws UnknownHostException Iznimka za probleme s dohvaćanjem IP adreseion
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    this.podaciRadara = new PodaciRadara(
    		Integer.parseInt(konfig.dajPostavku("id")),
    		InetAddress.getLocalHost().getHostName(),
    		Integer.parseInt(konfig.dajPostavku("mreznaVrataRadara")),
    		Integer.parseInt(konfig.dajPostavku("maksBrzina")),
    		Integer.parseInt(konfig.dajPostavku("maksTrajanje")),
    		Integer.parseInt(konfig.dajPostavku("maksUdaljenost")),
    		konfig.dajPostavku("adresaRegistracije"),
    		Integer.parseInt(konfig.dajPostavku("mreznaVrataRegistracije")),
    		konfig.dajPostavku("adresaKazne"),
    		Integer.parseInt(konfig.dajPostavku("mreznaVrataKazne")),
    		konfig.dajPostavku("postanskaAdresaRadara"),
    		Double.parseDouble(konfig.dajPostavku("gpsSirina")),
    		Double.parseDouble(konfig.dajPostavku("gpsDuzina")));

  }
}

package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.posluzitelji;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;


// TODO: Auto-generated Javadoc
/**
 * Klasa Centralni sustav
 * pokreće PoslužiteljaZaRegistracijuRadara i PoslužiteljaVozila
 */
public class CentralniSustav {
  
  /** Mrežna vrata radara - mrežna vrata radara iz konfiguracijske datoteke */
  public int mreznaVrataRadara;
  
  /** Mrežna vrata vozila - mrežna vrata vozila iz konfiguracijske datoteke */
  public int mreznaVrataVozila;
  
  /** Mrežna vrata nadzora */
  public int mreznaVrataNadzora;
  
  /** maksVozila - maksimalni broj vozila u simulaciji */
  public int maksVozila;
  
  /** Poslužitelj za registraciju radara - poslužitelj koji radi u jednodretvenom načinu rada*/
  private PosluziteljZaRegistracijuRadara posluziteljZaRegistracijuRadara;
  
  /** Poslužitelj za vozila - poslužitelj koji radi u višedretvenom načinu rada */
  private PosluziteljZaVozila posluziteljZaVozila;
  
  /** Tvornica dretvi - stvara nove dretve za poslužitelje */
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();
  
  /** Svi radari koji su trenutno aktivni */
  public ConcurrentHashMap<Integer, PodaciRadara> sviRadari =
      new ConcurrentHashMap<Integer, PodaciRadara>();
  
  /** Sva vozila */
  public ConcurrentHashMap<Integer, RedPodaciVozila> svaVozila =
      new ConcurrentHashMap<Integer, RedPodaciVozila>();
  public List<Integer> vozila = new ArrayList<>();

  /**
   * Glavna metoda
   *
   * @param args - konfiguracijska datoteka centralnog sustava
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    CentralniSustav centralniSustav = new CentralniSustav();
    try {
      centralniSustav.preuzmiPostavke(args);
      
      centralniSustav.pokreniPosluzitelje();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Pokreni posluzitelje - kreiraju se dretve za PosluziteljaZaRegistracijuRadara i PosluziteljaZaVozila
   */
  public void pokreniPosluzitelje() {
    posluziteljZaRegistracijuRadara = new PosluziteljZaRegistracijuRadara(this.mreznaVrataRadara, this);
    Thread posluziteljRadaraDretva = tvornicaDretvi.newThread(posluziteljZaRegistracijuRadara);
    posluziteljRadaraDretva.start();
    posluziteljZaVozila = new PosluziteljZaVozila(this.mreznaVrataVozila, this);
    Thread posluziteljZaVozilaDretva = tvornicaDretvi.newThread(posluziteljZaVozila);
    posluziteljZaVozilaDretva.start();
    try {
      posluziteljRadaraDretva.join();
      posluziteljZaVozilaDretva.join();
    }
    catch(InterruptedException e){
      e.printStackTrace();
    }
	 
  }

  /**
   * Preuzmi postavke - preuzimaju se postavke iz konfiguracijske datoteke .
   *
   * @param args Argumenti koje zadaje klijent
   * @throws NeispravnaKonfiguracija Neispravna konfiguracija
   * @throws NumberFormatException Iznimka za rukovanje s pogreškama u parsiranju
   * @throws UnknownHostException Iznimka za probleme s dohvaćanjem IP adrese
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.mreznaVrataRadara = Integer.valueOf(konfig.dajPostavku("mreznaVrataRadara"));
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.mreznaVrataNadzora = Integer.valueOf(konfig.dajPostavku("mreznaVrataNadzora"));
    this.maksVozila = Integer.valueOf(konfig.dajPostavku("maksVozila"));
  }
}
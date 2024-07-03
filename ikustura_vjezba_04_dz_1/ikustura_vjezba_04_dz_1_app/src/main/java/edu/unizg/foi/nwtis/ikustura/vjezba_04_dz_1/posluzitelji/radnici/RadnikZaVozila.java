package edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.pomocnici.GpsUdaljenostBrzina;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji.CentralniSustav;

// TODO: Auto-generated Javadoc
/**
 * Klasa RadnikZaVozila - provjerava udaljenost vozila od radara i šalje zahtjeve za vozila koja su u dosegu radara
 */
public class RadnikZaVozila implements Runnable {
  
  /** Mrežna vrata - mrežna vrata vozila. */
  private int mreznaVrata;
  
  /** Zahtjev - pročitana linija iz datoteke o svim podacima vozila */
  private String zahtjev;
  
  /** Centralni sustav - program koji pokreće poslužitelje */
  private CentralniSustav centralniSustav;
  
  /** Predlozak vozila - regularni izraz za provjeru zapisa iz csv datoteke */
  private Pattern predlozakVozila = Pattern
      .compile("^VOZILO (?<id>\\d+) (?<broj>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) "
          + "(?<snaga>-?\\d+([.]\\d+)?) (?<struja>-?\\d+([.]\\d+)?) (?<visina>-?\\d+([.]\\d+)?) "
          + "(?<gpsBrzina>-?\\d+([.]\\d+)?) (?<tempVozila>\\d+) (?<postotakBaterija>\\d+) "
          + "(?<naponBaterija>-?\\d+([.]\\d+)?) (?<kapacitetBaterija>\\d+) (?<tempBaterija>\\d+) "
          + "(?<preostaloKm>-?\\d+([.]\\d+)?) (?<ukupnoKm>-?\\d+([.]\\d+)?) "
          + "(?<gpsSirina>-?\\d+([.]\\d+)?) (?<gpsDuzina>-?\\d+([.]\\d+)?)$");
  
  /** Poklapanje vozila - provjera poklapanja zapisa i regularnog izraza predlozakVozila */
  private Matcher poklapanjeVozila;
  
  /**
   * Instanciranje novog radnika za vozila
   *
   * @param mreznaVrata mrežna vrata Vozila
   * @param komanda - zapis iz csv datoteke o vozilu
   * @param centralniSustav - objekt glavnog poslužitelja 
   */
  public RadnikZaVozila(int mreznaVrata, String komanda, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.zahtjev = komanda; 
    this.centralniSustav = centralniSustav;
  }
  
  /**
   * Run - pokretanje dretve, provjera regularnog izraza i udaljenosti vozila od radara
   * slanje zahtjeva prema poslužitelju
   */
  @Override
  public void run() {
    
    this.poklapanjeVozila = this.predlozakVozila.matcher(this.zahtjev);
    var status = poklapanjeVozila.matches();
    if(status) {
      
      Double gpsSirina = Double.valueOf(this.poklapanjeVozila.group("gpsSirina"));
      Double gpsDuzina = Double.valueOf(this.poklapanjeVozila.group("gpsDuzina"));
      
      for (var radar : this.centralniSustav.sviRadari.entrySet()) {
        
        Double udaljenost = GpsUdaljenostBrzina.udaljenostKm(gpsSirina, gpsDuzina, 
        radar.getValue().gpsSirina(), radar.getValue().gpsDuzina()) / 1000;
        if(udaljenost <= radar.getValue().maksUdaljenost()) {
          String komanda = vratiKomandu(poklapanjeVozila);
          var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju
              (radar.getValue().adresaRadara(), radar.getValue().mreznaVrataRadara(), komanda);
          
        }
      }
    }
  }

  /**
   * Vrati komandu - definiranje komande koja se šalje poslužitelju
   *
   * @param poklapanjeVozila podaci o vozilu iz zahtjeva
   * @return zahtjev koji se šalje poslužitelju za provjeru brzine 
   */
  public String vratiKomandu(Matcher poklapanjeVozila) {
    StringBuilder komanda = new StringBuilder();
    String razmak = " ";
    komanda.append("VOZILO").append(razmak).append(Integer.valueOf(this.poklapanjeVozila.group("id")))
    .append(razmak).append(Long.valueOf(this.poklapanjeVozila.group("vrijeme")))
    .append(razmak).append(Double.valueOf(this.poklapanjeVozila.group("brzina")))
    .append(razmak).append(Double.valueOf(this.poklapanjeVozila.group("gpsSirina")))
    .append(razmak).append(Double.valueOf(this.poklapanjeVozila.group("gpsDuzina"))).append("\n");
    return komanda.toString();
  }


}

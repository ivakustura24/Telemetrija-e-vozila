package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.klijenti;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

// TODO: Auto-generated Javadoc
/**
 * Klasa SimulatorVozila.
 */
public class SimulatorVozila {
  
  /** Mrežna vrata vozila - mrežna vrata vozila */
  public int mreznaVrataVozila; 
  
  /** Adresa vozila - adresa poslužitelja */
  public String adresaVozila;
  
  /** Trajanje sekundi - zapis iz konfiguracijske datoteke */
  public int trajanjeSek;
  
  /** Trajanje pauze - trajanje spavanja dretve nakon obavljenog posla */
  public int trajanjePauze;
  
  /** The csv datoteka iz koje čitamo zapise o vozilu */
  public static String csvDatoteka;
  
  /** Id vozila koje pokrećemo */
  public int idVozila;
  
  /** Izvršitelj - za upravljanje dretvama  */
  private static ExecutorService izvrsitelj = Executors.newVirtualThreadPerTaskExecutor();
  
  /**
   * Glavna metoda.
   *
   * @param args Argumenti koje korisnik zadaje - konfiguracijska datoteka, csv datoteka i id vozila 
   */
  public static void main(String[] args){
    
    SimulatorVozila simulatorVozila = new SimulatorVozila();
    int brojArgumenata = args.length;
    double korekcijaVremena = simulatorVozila.trajanjeSek / 1000;
    if(brojArgumenata == 3 ) {
      
        try {
         simulatorVozila.preuzmiPostavke(args);
         BufferedReader citac = new BufferedReader(new FileReader(csvDatoteka));
         String redak = citac.readLine();
         int brojac = 0;
         long prethodnoVrijeme = 0;
         long trenutnoVrijeme = 0;
         long vrijemeSpavanja = 0;
          while(redak !=null) {
            if(brojac != 0) {
              trenutnoVrijeme = simulatorVozila.dohvatiVrijeme(redak);
              vrijemeSpavanja = (long) ((trenutnoVrijeme - prethodnoVrijeme) * korekcijaVremena);
              Thread.sleep(vrijemeSpavanja);
              
              String komanda = simulatorVozila.kreirajKomandu(redak, simulatorVozila.idVozila, brojac );
              izvrsitelj.submit(() -> {
                try {
                    MrezneOperacije.posaljiZahtjevPosluzitelju(simulatorVozila.adresaVozila, simulatorVozila.mreznaVrataVozila, komanda);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            }
            prethodnoVrijeme = trenutnoVrijeme;
            Thread.sleep(simulatorVozila.trajanjePauze);
            redak = citac.readLine();
            brojac++;
          }
          
        } catch (NeispravnaKonfiguracija | NumberFormatException | IOException e ) {
          e.printStackTrace();
          return;
        
      } catch (InterruptedException e1) {
         //e1.printStackTrace();
        }
    }
  }


  /**
   * Dohvati vrijeme - dohvaća vrijeme za trenutni redak
   *
   * @param redak - redak iz csv datoteke 
   * @return vrijeme koje je parsirano u long i pročitano iz retka
   */
  private long dohvatiVrijeme(String redak) {
      int indeks = redak.indexOf(",");
      String vrijeme = redak.substring(0, indeks);
      long vrijemeParsirano = Long.parseLong(vrijeme);
    return vrijemeParsirano;
  }


  /**
   * Kreiraj komandu.
   *
   * @param redak linija iz csv datoteke
   * @param idVozila id vozila iz zapisa
   * @param brojRetka broj retka koji je pročitan
   * @return komanda šalje se poslužitelju 
   */
  private String kreirajKomandu(String redak, int idVozila, int brojRetka) {
    String podaci = redak.replace(",", " ");
    StringBuilder komanda= new StringBuilder();
    String razmak = " ";
    komanda.append("VOZILO").append(razmak).append(idVozila).
    append(razmak).append(brojRetka).append(razmak).append(podaci).append("\n");
    
    return komanda.toString();
  }

  /**
   * Preuzmi postavke - preuzimaju se postavke iz konfiguracijske datoteke.
   *
   * @param args Argumenti koje zadaje klijent
   * @throws NeispravnaKonfiguracija Neispravna konfiguracija
   * @throws NumberFormatException Iznimka za rukovanje s pogreškama u parsiranju
   * @throws UnknownHostException Iznimka za probleme s dohvaćanjem IP adrese
   */
  private void preuzmiPostavke(String[] args) 
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException { {
        Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
        this.adresaVozila = konfig.dajPostavku("adresaVozila").toString();
        this.mreznaVrataVozila = Integer.parseInt(konfig.dajPostavku("mreznaVrataVozila"));
        this.trajanjeSek = Integer.parseInt(konfig.dajPostavku("trajanjeSek"));
        this.trajanjePauze = Integer.parseInt(konfig.dajPostavku("trajanjePauze"));
        this.csvDatoteka = args[1];
        this.idVozila = Integer.parseInt(args[2]);
        
      }
  }
}

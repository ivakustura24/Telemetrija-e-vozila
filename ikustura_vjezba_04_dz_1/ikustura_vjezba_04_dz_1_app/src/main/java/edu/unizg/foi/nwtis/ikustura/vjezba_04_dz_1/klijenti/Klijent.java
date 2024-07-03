package edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.klijenti;

import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

// TODO: Auto-generated Javadoc
/**
 * Klasa Klijent - dohvaća podatke o najsvježijoj kazni i statistici o kaznama.
 */
public class Klijent {

  /** Adresa kazne iz konfiguracijske datoteke */
  private String adresaKazne;
  
  /** Mrežna vrata kazne iz konfiguracijske datoteke */
  private int mreznaVrataKazne;
  
  /** Komanda koja se šalje Poslužitelju Kazni */
  public String komanda;
  
  /**
   * Glavna metoda 
   *
   * @param args argumenti koje zadaje klijent za dohvaćanje statistike i kazni
   * @throws NumberFormatException Iznimka za rukovanje s pogreškama u parsiranju
   * @throws UnknownHostException Iznimka za probleme s dohvaćanjem IP adrese
   * @throws NeispravnaKonfiguracija Neispravna konfiguracija
   */
  public static void main(String[] args) throws NumberFormatException, UnknownHostException, NeispravnaKonfiguracija{
    Klijent klijent = new Klijent();
    int brojArgumenata = args.length;
    klijent.preuzmiPostavke(args);
    
    String vrijemeOd;
    String vrijemeDo;
    String idVozila;
    
    if(brojArgumenata == 3 || brojArgumenata == 4) {
      if(brojArgumenata == 3) {
        vrijemeOd = args[1];
        vrijemeDo = args[2];
        klijent.komanda = klijent.kreirajKomandu("0", vrijemeOd, vrijemeDo );
      }
      else {
        idVozila = args[1];
        vrijemeOd = args[2];
        vrijemeDo = args[3];
        klijent.komanda = klijent.kreirajKomandu(idVozila, vrijemeOd, vrijemeDo);
      }
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju
          (klijent.adresaKazne, klijent.mreznaVrataKazne, klijent.komanda);
      System.out.println(odgovor);
    }
    else {
      System.out.println("Neispavan broj argumenata");
    }
  }

  /**
   * Kreiraj komandu.
   *
   * @param id Id vozila čiju kaznu želimo dobiti
   * @param vrijemeOd Vrijeme od kojeg se traži kazna
   * @param vrijemeDo Vrijeme do kojeg se traži kazna
   * @return komanda za slanje prema poslužitelju
   */
  private String kreirajKomandu(String id, String vrijemeOd, String vrijemeDo) {
    StringBuilder komanda = new StringBuilder();
    String razmak = " ";
    if(id == "0") {
      komanda.append("STATISTIKA").append(razmak).
      append(vrijemeOd).append(razmak).append(vrijemeDo).append("\n");
    }
    else {
      komanda.append("VOZILO").append(razmak).append(id)
      .append(razmak).append(vrijemeOd).append(razmak).append(vrijemeDo).append("\n");
    }
    
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
  private void preuzmiPostavke(String[] args)throws 
  NeispravnaKonfiguracija, NumberFormatException, UnknownHostException { {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    this.adresaKazne = konfig.dajPostavku("adresaKazne");
    this.mreznaVrataKazne = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
    
  }
    
    
  }
  
  
}

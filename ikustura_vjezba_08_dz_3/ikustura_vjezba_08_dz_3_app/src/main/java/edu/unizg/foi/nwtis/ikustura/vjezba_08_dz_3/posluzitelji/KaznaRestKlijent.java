package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji;


import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class KaznaRestKlijent {


  
  public boolean postKaznaJSON(PodaciKazne kazna) throws ClientErrorException {
    KazneRest rk = new KazneRest();
    var odgovor = rk.postJSON(kazna);
    return odgovor;
  }


  
  
  static class KazneRest {
    
    private final WebTarget webTarget;
    private final Client klijent;
    private static String baseURI;

    public KazneRest() {
      try {
        preuzmiPostavke();
      } catch (NumberFormatException | UnknownHostException | NeispravnaKonfiguracija e) {
        e.printStackTrace();
      }
      final String BASE_URI = baseURI;
      klijent = ClientBuilder.newClient();
      webTarget = klijent.target(BASE_URI).path("nwtis/v1/api/kazne");
    }
    
    public boolean postJSON(PodaciKazne kazna) throws ClientErrorException {
      WebTarget resource = webTarget;
      if (kazna == null) {
        return false;
      }
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Kazna novaKazna = vratiKaznu(kazna);

      var odgovor =
          request.post(Entity.entity(novaKazna, MediaType.APPLICATION_JSON), String.class).toString();
      if (odgovor.trim().length() > 0) {
        return true;
      }

      return false;
    }
    public Kazna vratiKaznu(PodaciKazne kazna) {
      Kazna novaKazna =
          new Kazna(kazna.id(), kazna.vrijemePocetak(), kazna.vrijemeKraj(), kazna.brzina(),
              kazna.gpsSirina(), kazna.gpsDuzina(), kazna.gpsSirinaRadar(), kazna.gpsDuzinaRadar());
      return novaKazna;
    }
    public void preuzmiPostavke()
        throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
      Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju("NWTiS_DZ1_CS.txt");

      this.baseURI = konfig.dajPostavku("webservis.vozila.baseuri");
      
    }
  }

}

package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.posluzitelji;


import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.Kazna;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.PodaciKazne;
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
    private static final String BASE_URI = "http://localhost:9080/";

    public KazneRest() {
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
  }

}

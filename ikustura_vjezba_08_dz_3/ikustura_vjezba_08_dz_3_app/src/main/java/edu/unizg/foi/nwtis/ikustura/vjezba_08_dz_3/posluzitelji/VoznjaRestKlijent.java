package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji;


import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Voznja;
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

public class VoznjaRestKlijent {

  public boolean postVoznjaJSON(Voznja voznja) throws ClientErrorException {
    VoznjeRest vr = new VoznjeRest();
    var odgovor = vr.postJSON(voznja);
    return odgovor;
  }
  
  static class VoznjeRest {
    
    private final WebTarget webTarget;
    private final Client klijent;
    private static String baseURI;

    public VoznjeRest() {
      try {
        preuzmiPostavke();
      } catch (NumberFormatException | UnknownHostException | NeispravnaKonfiguracija e) {
        e.printStackTrace();
      }
      final String BASE_URI = baseURI;
      klijent = ClientBuilder.newClient();
      webTarget = klijent.target(BASE_URI).path("nwtis/v1/api/simulacije");
    }
    
    public boolean postJSON(Voznja voznja) throws ClientErrorException {
      WebTarget resource = webTarget;
      if (voznja == null) {
        return false;
      }
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      var odgovor =
          request.post(Entity.entity(voznja, MediaType.APPLICATION_JSON), String.class).toString();
      if (odgovor.trim().length() > 0) {
        return true;
      }

      return false;
    }
    public void preuzmiPostavke()
        throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
      Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju("NWTiS_DZ1_CS.txt");
      this.baseURI = konfig.dajPostavku("webservis.vozila.baseuri");
    }
  }

}

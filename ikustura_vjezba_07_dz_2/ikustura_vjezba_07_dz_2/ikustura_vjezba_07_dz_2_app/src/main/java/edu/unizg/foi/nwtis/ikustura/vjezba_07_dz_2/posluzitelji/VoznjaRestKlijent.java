package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.posluzitelji;


import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.Voznja;
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
    private static final String BASE_URI = "http://localhost:9080/";

    public VoznjeRest() {
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
  
  }

}

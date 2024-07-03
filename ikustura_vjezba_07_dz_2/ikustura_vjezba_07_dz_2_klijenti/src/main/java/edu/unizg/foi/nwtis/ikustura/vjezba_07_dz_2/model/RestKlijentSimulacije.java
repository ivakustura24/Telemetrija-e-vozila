package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.Kazna;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.Voznja;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextListener;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentKazne.
 */
public class RestKlijentSimulacije {

  /**
   * Kopntruktor klase.
   */
  public RestKlijentSimulacije() {}

  /**
   * Vraća sve kazne
   *
   * @return kazne
   */
  public List<Voznja> getVozilaJSON(long odVremena, long doVremena) {
    RestSimulacije rk = new RestSimulacije();
    List<Voznja> voznje = rk.getJSON_od_do(odVremena, doVremena);

    return voznje;
  }
  public List<Voznja> getVoznjeVozila(String id) {
    RestSimulacije rk = new RestSimulacije();
    List<Voznja> voznje = rk.getVoznjeVozila(id);

    return voznje;
  }


  public List<Voznja> getVoznjeVozilaInterval(String id, long odVremena, long doVremena) {
    RestSimulacije rk = new RestSimulacije();
    List<Voznja> voznje = rk.getVoznjeVozilaInterval(id,odVremena, doVremena);
    return voznje;
  }

  

  /**
   * Klasa RestKazne.
   */
  static class RestSimulacije{

    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** knstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestSimulacije() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/simulacije");
    }


    public List<Voznja> getVoznjeVozilaInterval(String id, long odVremena, long doVremena) {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznje = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznje));
      }

      return voznje;
    }


    public List<Voznja> getVoznjeVozila(String id) {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznje = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznje));
      }

      return voznje;
    }


    public List<Voznja> getJSON_od_do(long odVremena, long doVremena) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();

      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznje = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznje));
      }

      return voznje;
    }


    /**
     * Close.
     */
    public void close() {
      client.close();
    }
  }
}

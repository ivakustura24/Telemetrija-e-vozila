package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Voznja;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.slusaci.ContextListener;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentKazne.
 */
public class RestKlijentVozila {

  /**
   * Kopntruktor klase.
   */
  public RestKlijentVozila() {}

  /**
   * Vraća sve kazne
   *
   * @return kazne
   */
  public List<Voznja> getVozilaJSON(long odVremena, long doVremena) {
    RestVoznje rk = new RestVoznje();
    List<Voznja> voznje = rk.getJSON_od_do(odVremena, doVremena);

    return voznje;
  }
  public List<Voznja> getVoznjeVozila(String id) {
    RestVoznje rk = new RestVoznje();
    List<Voznja> voznje = rk.getVoznjeVozila(id);

    return voznje;
  }


  public List<Voznja> getVoznjeVozilaInterval(String id, long odVremena, long doVremena) {
    RestVoznje rk = new RestVoznje();
    List<Voznja> voznje = rk.getVoznjeVozilaInterval(id,odVremena, doVremena);
    return voznje;
  }

  public String pokreniVozilo(String id) {
    RestVoznje rk = new RestVoznje();
    String odgovor = rk.pokreniVozilo(id);
    return odgovor;
  }
  public String zaustaviVozilo(String id) {
    RestVoznje rk = new RestVoznje();
    String odgovor = rk.zaustaviVozilo(id);
    return odgovor;
  }
  

  /**
   * Klasa RestKazne.
   */
  static class RestVoznje {

    @Context
    private ServletContext servletContext;
    /** web target. */
    private final WebTarget webTarget;
    private final Client klijent;
    private static String baseURI;


    public RestVoznje() {
      ContextListener contextListener =
          (ContextListener) servletContext.getAttribute("contextListener");
      if (contextListener != null) {
        try {
          baseURI = contextListener.vratiKonfiguracijskePostavke("webservis.vozila.baseuri");
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("ContextListener nije dostupan!");
      }
      final String BASE_URI = baseURI;
      klijent = ClientBuilder.newClient();
      webTarget = klijent.target(BASE_URI).path("nwtis/v1/api/vozila");
    }


    public String pokreniVozilo(String id) {
      WebTarget resource = webTarget;
      String rezultat = " ";
      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}/start", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        rezultat = jb.fromJson(odgovor, String.class);
      }

      return rezultat;
    }
    public String zaustaviVozilo(String id) {
      WebTarget resource = webTarget;
      String rezultat = " ";
      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}/stop", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        rezultat = jb.fromJson(odgovor, String.class);
      }

      return rezultat;
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
      klijent.close();
    }
  }










 
}

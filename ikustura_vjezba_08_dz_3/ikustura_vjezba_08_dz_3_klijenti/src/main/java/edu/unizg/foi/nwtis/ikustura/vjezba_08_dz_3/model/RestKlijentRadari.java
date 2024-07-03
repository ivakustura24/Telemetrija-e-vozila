package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.model;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.slusaci.ContextListener;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestKlijentRadari {

  public RestKlijentRadari() {}

  public List<PodaciRadara> getRadariJSON() {
    RestRadari restRadar = new RestRadari();
    List<PodaciRadara> radari = restRadar.getJSON();

    return radari;
  }

  public List<PodaciRadara> getRadarJSON(String id) {
    RestRadari restRadar = new RestRadari();
    List<PodaciRadara> radari = restRadar.dohvatiRadar(id);

    return radari;
  }

  public String getProvjeraRadara(String id) {
    RestRadari restRadar = new RestRadari();
    String odgovor = restRadar.provjeriRadar(id);
    return odgovor;
  }

  public String obrisiRadare() {
    RestRadari restRadar = new RestRadari();
    String odgovor = restRadar.obrisiRadare();
    return odgovor;
  }

  public String resetirajRadare() {
    RestRadari restRadar = new RestRadari();
    String odgovor = restRadar.resetirajRadare();
    return odgovor;
  }

  public String getObrisiRadar(String id) {
    RestRadari restRadar = new RestRadari();
    String odgovor = restRadar.obrisiRadar(id);
    return odgovor;
  }

  static class RestRadari {
    @Context
    private ServletContext servletContext;
    /** web target. */
    private final WebTarget webTarget;
    private final Client klijent;
    private static String baseURI;

    public RestRadari() {
      ContextListener contextListener =
          (ContextListener) servletContext.getAttribute("contextListener");
      if (contextListener != null) {
        try {
          baseURI = contextListener.vratiKonfiguracijskePostavke("webservis.radari.baseuri");
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("ContextListener nije dostupan!");
      }
      final String BASE_URI = baseURI;
      klijent = ClientBuilder.newClient();
      webTarget = klijent.target(BASE_URI).path("nwtis/v1/api/radari");
    }



    public String resetirajRadare() {
      String rezultat = "";
      WebTarget resource = webTarget;
      resource = resource.path("reset");
      rezultat = resource.toString();
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        rezultat = jb.fromJson(odgovor, String.class);
      }

      return rezultat;
    }



    public String provjeriRadar(String id) throws ClientErrorException {
      String rezultat = "";
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}/provjeri", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        rezultat = jb.fromJson(odgovor, String.class);
      }

      return rezultat;

    }

    public List<PodaciRadara> getJSON() throws ClientErrorException {
      WebTarget resource = webTarget;
      List<PodaciRadara> radari = new ArrayList<PodaciRadara>();

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pradari = jb.fromJson(odgovor, PodaciRadara[].class);
        radari.addAll(Arrays.asList(pradari));
      }

      return radari;
    }

    public List<PodaciRadara> dohvatiRadar(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<PodaciRadara> radari = new ArrayList<PodaciRadara>();

      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pradari = jb.fromJson(odgovor, PodaciRadara[].class);
        radari.addAll(Arrays.asList(pradari));
      }

      return radari;
    }

    public String obrisiRadare() throws ClientErrorException {
      WebTarget resource = webTarget;
      String rezultat = " ";

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().delete();
      rezultat = restOdgovor.toString();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        rezultat = jb.fromJson(odgovor, String.class);
      }

      return rezultat;
    }

    public String obrisiRadar(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      String rezultat = " ";
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().delete();
      rezultat = restOdgovor.toString();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        rezultat = jb.fromJson(odgovor, String.class);
      }

      return rezultat;
    }
  }
}



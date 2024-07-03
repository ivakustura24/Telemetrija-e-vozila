package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("nwtis/v1/api/radari")
public class RadariResurs {

  

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response vratiSveRadare(@HeaderParam("Accept") String tipOdgovora) {
      var zahtjev = "RADAR SVI";
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("20.24.5.2", 8000, zahtjev);
      List<PodaciRadara> radari = dohvatiSveRadare(odgovor);
      if(radari == null) {
        Map<String, String> odgovorMap = new HashMap<>();
        odgovorMap.put("odgovor", "OK");
        return Response.status(Response.Status.OK)
            .entity(odgovorMap)
            .build();
      }
      Gson gson = new Gson();
      String jsonRezultat = gson.toJson(radari);
      return Response.status(Response.Status.OK)
          .entity(jsonRezultat)
          .build();
  }
  
  @Path("/{id}/provjeri")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response provjeriRadar(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
      String zahtjev = "RADAR " + id;
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("20.24.5.2", 8000, zahtjev);
      
      Map<String, String> odgovorMap = new HashMap<>();
      odgovorMap.put("odgovor", odgovor);
      Gson gson = new Gson();
      String jsonRezultat = gson.toJson(odgovor);
      return Response.status(Response.Status.OK).entity(jsonRezultat).build();
  }
  @Path("/reset")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response deregistracijaRadara(@HeaderParam("Accept") String tipOdgovora) {
      String zahtjev = "RADAR RESET";
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("20.24.5.2", 8000, zahtjev);
      
      Map<String, String> odgovorMap = new HashMap<>();
      odgovorMap.put("odgovor", odgovor);
      Gson gson = new Gson();
      String jsonRezultat = gson.toJson(odgovor);
      return Response.status(Response.Status.OK).entity(jsonRezultat).build();
  }
  @Path("/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response vratiRadar(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
      String zahtjev = "RADAR SVI";
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("20.24.5.2", 8000, zahtjev);
      
      List<PodaciRadara> radari = dohvatiSveRadare(odgovor);
      if(radari == null) {
        Map<String, String> odgovorMap = new HashMap<>();
        odgovorMap.put("odgovor", odgovor);
        return Response.status(Response.Status.OK)
            .entity(odgovorMap)
            .build();
      }
      List<PodaciRadara> radar = dohvatiRadar(id, radari);
      Gson gson = new Gson();
      String jsonRezultat = gson.toJson(radar);
      if(radar == null) {
        Map<String, String> mapOdgovor = new HashMap<>();
        mapOdgovor.put("odgovor", "Ne postoji radar");
        return Response.status(Response.Status.OK)
            .entity(mapOdgovor)
            .build();
      }
      return Response.status(Response.Status.OK)
          .entity(jsonRezultat)
          .build();
  }
  
  @DELETE
  @Produces({MediaType.APPLICATION_JSON})
  public Response obrisiSveRadare(@HeaderParam("Accept") String tipOdogovora) {
    String zahtjev = "RADAR OBRIŠI SVE";
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("20.24.5.2", 8000, zahtjev);
    if(odgovor == null) {
      odgovor ="ERROR 10 Neispravna sintaksa komande.\n";
    }
    Map<String, String> odgovorMap = new HashMap<>();
    odgovorMap.put("odgovor", odgovor);
    Gson gson = new Gson();
    String jsonRezultat = gson.toJson(odgovor);
    return Response.status(Response.Status.OK)
        .entity(jsonRezultat)
        .build();
  }
  @Path("/{id}")
  @DELETE
  @Produces({MediaType.APPLICATION_JSON})
  public Response obrisiRadar(@HeaderParam("Accept") String tipOdogovora, 
      @PathParam("id") int id) {
    String zahtjev = "RADAR OBRIŠI " + id;
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("20.24.5.2", 8000, zahtjev);
    if(odgovor == null) {
      odgovor ="ERROR 10 Neispravna sintaksa komande.\n";
    }
    Map<String, String> odgovorMap = new HashMap<>();
    odgovorMap.put("odgovor", odgovor);
    Gson gson = new Gson();
    String jsonRezultat = gson.toJson(odgovor);
    return Response.status(Response.Status.OK)
        .entity(jsonRezultat)
        .build();
  }

  public List<PodaciRadara> dohvatiSveRadare(String odgovor) {
    List<PodaciRadara> radari = new ArrayList<>();
    if (odgovor != null) {
      String podaci = odgovor.replace("OK", "").replace("{", "").replace("}", "");
      if (podaci.length() < 6) {
        return null;
      }
      String[] podaciRadara = podaci.split("\\], \\[");
      for (int i = 0; i < podaciRadara.length; i++) {
        podaciRadara[i] = podaciRadara[i].replaceAll("[\\[\\]]", "").trim();
      }
      for (int i = 0; i < podaciRadara.length; i++) {
        String[] radar = podaciRadara[i].split("\\s+");
        int id = Integer.parseInt(radar[0]);
        String adresa = radar[1];
        int vrata = Integer.parseInt(radar[2]);
        double gpsSirina = Double.parseDouble(radar[3]);
        double gpsDuzina = Double.parseDouble(radar[4]);
        int udaljenost = Integer.parseInt(radar[5]);
        PodaciRadara noviRadar = new PodaciRadara(id, adresa, vrata, 0, 0, udaljenost, null, 0,
            null, 0, null, gpsSirina, gpsDuzina);
        radari.add(noviRadar);
      }
      return radari;
    }
    return null;
  }
  public List<PodaciRadara> dohvatiRadar(int id, List<PodaciRadara> radari) {
    List<PodaciRadara> radariSvi = new ArrayList<PodaciRadara>();
    for(PodaciRadara radar : radari) {
      if(radar.id() == id) {
        radariSvi.add(radar);
      }
    }
    return radariSvi;
  }
}

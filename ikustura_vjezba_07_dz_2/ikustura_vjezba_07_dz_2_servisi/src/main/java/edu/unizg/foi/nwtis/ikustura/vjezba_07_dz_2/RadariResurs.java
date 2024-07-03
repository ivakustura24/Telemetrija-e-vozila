package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.ResursDAO;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("nwtis/v1/api/radari")
public class RadariResurs {
  private int mreznaVrata;
  private ResursDAO resursDAO = null;
  public RadariResurs() {
    resursDAO = new ResursDAO();
    try {
      preuzmiPostavke();
      
    } catch (NumberFormatException | UnknownHostException | NeispravnaKonfiguracija e) {
      e.printStackTrace();
    } 
  }
  public void preuzmiPostavke()
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju("NWTiS_REST_R.txt");

    this.mreznaVrata = Integer.valueOf(konfig.dajPostavku("mreznaVrataRegistracije"));
    
  }
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response vratiSveRadare(@HeaderParam("Accept") String tipOdgovora) {
      var zahtjev = "RADAR SVI";
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("localhost", this.mreznaVrata, zahtjev);
      List<PodaciRadara> radari = resursDAO.dohvatiSveRadare(odgovor);
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
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("localhost", this.mreznaVrata, zahtjev);
      
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
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("localhost", this.mreznaVrata, zahtjev);
      
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
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("localhost", this.mreznaVrata, zahtjev);
      
      List<PodaciRadara> radari = resursDAO.dohvatiSveRadare(odgovor);
      if(radari == null) {
        Map<String, String> odgovorMap = new HashMap<>();
        odgovorMap.put("odgovor", odgovor);
        return Response.status(Response.Status.OK)
            .entity(odgovorMap)
            .build();
      }
      List<PodaciRadara> radar = resursDAO.dohvatiRadar(id, radari);
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
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("localhost", this.mreznaVrata, zahtjev);
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
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("localhost", this.mreznaVrata, zahtjev);
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
}

package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.Kazna;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.KaznaDAO;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.ResursDAO;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.Voznja;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.PracenaVoznjaDAO;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nwtis/v1/api/vozila")
public class VozilaResurs extends SviResursi {

  private PracenaVoznjaDAO pracenaVoznjaDao = null;
  private int mreznaVrataVozila;

  public VozilaResurs() {

    try {
      preuzmiPostavke();

    } catch (NumberFormatException | UnknownHostException | NeispravnaKonfiguracija e) {
      e.printStackTrace();
    }
  }

  @PostConstruct
  private void pripremiVoznjaDAO() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      var vezaBP = this.vezaBazaPodataka.getVezaBazaPodataka();
      this.pracenaVoznjaDao = new PracenaVoznjaDAO(vezaBP);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  @Path("/vozilo/{id}/start")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response pokreniVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    String komanda = "VOZILO START " + id;
    var odgovor =
        MrezneOperacije.posaljiZahtjevPosluzitelju("localhost", this.mreznaVrataVozila, komanda);
    Map<String, String> odgovorMap = new HashMap<>();
    odgovorMap.put("odgovor", odgovor);
    Gson gson = new Gson();
    String jsonRezultat = gson.toJson(odgovor);
    return Response.status(Response.Status.OK).entity(jsonRezultat).build();
  }

  @Path("/vozilo/{id}/stop")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response zaustaviVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    String komanda = "VOZILO STOP " + id;
    var odgovor =
        MrezneOperacije.posaljiZahtjevPosluzitelju("localhost", this.mreznaVrataVozila, komanda);
    System.out.println("odgovor vozila " + odgovor);
    Map<String, String> odgovorMap = new HashMap<>();
    Gson gson = new Gson();
    String jsonRezultat = gson.toJson(odgovor);
    return Response.status(Response.Status.OK).entity(jsonRezultat).build();
  }

  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response vratiPraceneVoznjeVozila(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena > 0 && doVremena > 0) {
      return Response.status(Response.Status.OK)
          .entity(pracenaVoznjaDao.vratiPraceneVoznjeVozilaInterval(id, odVremena, doVremena)).build();
    }
    return Response.status(Response.Status.OK).entity(pracenaVoznjaDao.vratiPraceneVoznjeVozila(id)).build();
  }


  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response vratiPraceneVoznjeInterval(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {

    System.out.println("ovdje sam 2");
    return Response.status(Response.Status.OK)
        .entity(pracenaVoznjaDao.vratiPraceneVoznjeInterval(odVremena, doVremena)).build();
  }

  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response posttJsonDodajKaznu(@HeaderParam("Accept") String tipOdgovora, Voznja voznja) {

    var odgovor = pracenaVoznjaDao.dodajPracenuVoznju(voznja);
    if (odgovor) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis vozila u bazu podataka.").build();
    }
  }

  public void preuzmiPostavke()
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju("NWTiS_REST_V.txt");

    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    System.out.println("mreVraVozila " + this.mreznaVrataVozila);
  }
}

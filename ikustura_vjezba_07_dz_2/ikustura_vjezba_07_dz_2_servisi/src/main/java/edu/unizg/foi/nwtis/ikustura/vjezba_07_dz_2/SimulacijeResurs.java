package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2;

import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.PracenaVoznjaDAO;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.Voznja;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.VoznjaDAO;
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

@Path("nwtis/v1/api/simulacije")
public class SimulacijeResurs extends SviResursi {
  
  private VoznjaDAO VoznjaDao = null;
  private int mreznaVrataVozila;

  public SimulacijeResurs() {

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
      this.VoznjaDao = new VoznjaDAO(vezaBP);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response vratiPraceneVoznjeInterval(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {

    return Response.status(Response.Status.OK)
        .entity(VoznjaDao.vratiVoznjeInterval(odVremena, doVremena)).build();
  }
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response vratiPraceneVoznjeVozila(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena > 0 && doVremena > 0) {
      return Response.status(Response.Status.OK)
          .entity(VoznjaDao.vratiVoznjeVozilaInterval(id, odVremena, doVremena)).build();
    }
    return Response.status(Response.Status.OK).entity(VoznjaDao.vratiVoznjeVozila(id)).build();
  }
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response posttJsonDodajKaznu(@HeaderParam("Accept") String tipOdgovora, Voznja voznja) {

    var odgovor = VoznjaDao.dodajVoznju(voznja);
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

  }
}

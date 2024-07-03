package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Pracenevoznje;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Voznje;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.pomocnici.PraceneVoznjeFacade;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.pomocnici.VozilaFacade;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Voznja;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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
@RequestScoped
public class VozilaResurs {

  @Inject
  PraceneVoznjeFacade praceneVoznjeFacade;
  @Inject
  VozilaFacade vozilaFacade;

  @Path("/vozilo/{id}/start")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response pokreniVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    String komanda = "VOZILO START " + id;
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("20.24.5.2", 8001, komanda);
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
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("20.24.5.2", 8001, komanda);
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
          .entity(pretvoriUListu(
              praceneVoznjeFacade.vratiPraceneVoznjeVozilaInterval(id, odVremena, doVremena)))
          .build();
    }
    return Response.status(Response.Status.OK)
        .entity(pretvoriUListu(praceneVoznjeFacade.vratiPraceneVoznjeVozila(id))).build();
  }


  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response vratiPraceneVoznjeInterval(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {

    return Response.status(Response.Status.OK)
        .entity(
            pretvoriUListu(praceneVoznjeFacade.vratiPraceneVoznjeInterval(odVremena, doVremena)))
        .build();
  }

  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response posttJsonDodajPracenuVoznju(@HeaderParam("Accept") String tipOdgovora,
      Voznja voznja) {
    var pracenaVoznja = pretvoriVoznju(voznja);
    boolean odgovor = praceneVoznjeFacade.dodajPracenuVoznju(pracenaVoznja);
    if (odgovor) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis vozila u bazu podataka.").build();
    }
  }

  private Pracenevoznje pretvoriVoznju(Voznja voznja) {
    var praceneVoznje = new Pracenevoznje();
    praceneVoznje.setVrijeme(voznja.getVrijeme());
    praceneVoznje.setTempvozila(voznja.getTempVozila());
    praceneVoznje.setBroj(voznja.getBroj());
    praceneVoznje.setVisina(voznja.getVisina());
    praceneVoznje.setTempbaterija(voznja.getTempBaterija());
    praceneVoznje.setBrzina(voznja.getBrzina());
    praceneVoznje.setSnaga(voznja.getSnaga());
    praceneVoznje.setNaponbaterija(voznja.getNaponBaterija());
    praceneVoznje.setKapacitetbaterija(voznja.getKapacitetBaterija());
    praceneVoznje.setStruja(voznja.getStruja());
    praceneVoznje.setGpsbrzina(voznja.getGpsBrzina());
    praceneVoznje.setPreostalokm(voznja.getPreostaloKm());
    praceneVoznje.setGpssirina(voznja.getGpsSirina());
    praceneVoznje.setGpsduzina(voznja.getGpsDuzina());
    praceneVoznje.setPostotakbaterija(voznja.getPostotakBaterija());
    praceneVoznje.setUkupnokm(voznja.getUkupnoKm());

    var vozilo = vozilaFacade.find(voznja.getId());
    praceneVoznje.setVozila(vozilo);
    return praceneVoznje;
  }

  private Voznja pretvoriVoznju(Pracenevoznje praceneVoznje) {
    if (praceneVoznje == null) {
      return null;
    }
    Voznja voznja = new Voznja(praceneVoznje.getVozila().getVozilo(), praceneVoznje.getBroj(),
        praceneVoznje.getVrijeme(), praceneVoznje.getBrzina(), praceneVoznje.getSnaga(),
        praceneVoznje.getStruja(), praceneVoznje.getVisina(), praceneVoznje.getGpsbrzina(),
        praceneVoznje.getTempvozila(), praceneVoznje.getPostotakbaterija(),
        praceneVoznje.getNaponbaterija(), praceneVoznje.getKapacitetbaterija(),
        praceneVoznje.getTempbaterija(), praceneVoznje.getPreostalokm(),
        praceneVoznje.getUkupnokm(), praceneVoznje.getGpssirina(), praceneVoznje.getGpsduzina());
    return voznja;
  }

  private List<Voznja> pretvoriUListu(List<Pracenevoznje> voznje) {
    var praceneVoznjeLista = new ArrayList<Voznja>();
    for (Pracenevoznje v : voznje) {
      var voznja = pretvoriVoznju(v);
      praceneVoznjeLista.add(voznja);
    }
    return praceneVoznjeLista;
  }

}

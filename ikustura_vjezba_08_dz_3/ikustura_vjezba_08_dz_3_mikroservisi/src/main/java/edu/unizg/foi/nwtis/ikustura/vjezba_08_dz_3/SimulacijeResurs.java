package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Voznje;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.pomocnici.VozilaFacade;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.pomocnici.VoznjeFacade;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Voznja;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
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
public class SimulacijeResurs {


  @Inject
  VoznjeFacade voznjeFacade;
  @Inject
  VozilaFacade vozilaFacade;

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonVoznje(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena > 0 && doVremena > 0) {
      return Response.status(Response.Status.OK)
          .entity(pretvoriUListu(voznjeFacade.vratiVoznjeInterval(odVremena, doVremena))).build();
    }
    return Response.status(Response.Status.OK).entity(pretvoriUListu(voznjeFacade.vratiSveVoznje()))
        .build();
  }

  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response vratiPraceneVoznjeVozila(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena > 0 && doVremena > 0) {
      return Response.status(Response.Status.OK)
          .entity(pretvoriUListu(voznjeFacade.vratiVoznjeVozilaInterval(id, odVremena, doVremena)))
          .build();
    }
    return Response.status(Response.Status.OK)
        .entity(pretvoriUListu(voznjeFacade.vratiVoznjeVozila(id))).build();
  }

  @POST
  @Produces({MediaType.APPLICATION_JSON})
  @Transactional(TxType.REQUIRED)
  public Response posttJsonDodajKaznu(@HeaderParam("Accept") String tipOdgovora, Voznja voznja) {
    var voznje = pretvoriVoznju(voznja);
    var odgovor = voznjeFacade.dodajVoznju(voznje);
    if (odgovor) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis vozila u bazu podataka.").build();
    }
  }


  private Voznje pretvoriVoznju(Voznja voznja) {
    var voznje = new Voznje();
    voznje.setVrijeme(voznja.getVrijeme());
    voznje.setTempvozila(voznja.getTempVozila());
    voznje.setBroj(voznja.getBroj());
    voznje.setVisina(voznja.getVisina());
    voznje.setTempbaterija(voznja.getTempBaterija());
    voznje.setBrzina(voznja.getBrzina());
    voznje.setSnaga(voznja.getSnaga());
    voznje.setNaponbaterija(voznja.getNaponBaterija());
    voznje.setKapacitetbaterija(voznja.getKapacitetBaterija());
    voznje.setStruja(voznja.getStruja());
    voznje.setGpsbrzina(voznja.getGpsBrzina());
    voznje.setPreostalokm(voznja.getPreostaloKm());
    voznje.setGpssirina(voznja.getGpsSirina());
    voznje.setGpsduzina(voznja.getGpsDuzina());
    voznje.setPostotakbaterija(voznja.getPostotakBaterija());
    voznje.setUkupnokm(voznja.getUkupnoKm());

    var vozilo = vozilaFacade.find(voznja.getId());
    voznje.setId(vozilo);
    return voznje;
  }

  private Voznja pretvoriVoznju(Voznje voznje) {
    if (voznje == null) {
      return null;
    }
    Voznja voznja = new Voznja(voznje.getId().getVozilo(), voznje.getBroj(),
        voznje.getVrijeme(), voznje.getBrzina(), voznje.getSnaga(), voznje.getStruja(),
        voznje.getVisina(), voznje.getGpsbrzina(), voznje.getTempvozila(),
        voznje.getPostotakbaterija(), voznje.getNaponbaterija(), voznje.getKapacitetbaterija(),
        voznje.getTempbaterija(), voznje.getPreostalokm(), voznje.getUkupnokm(),
        voznje.getGpssirina(), voznje.getGpsduzina());
    return voznja;
  }

  private List<Voznja> pretvoriUListu(List<Voznje> voznje) {
    var voznjeLista = new ArrayList<Voznja>();
    for (Voznje v : voznje) {
      var voznja = pretvoriVoznju(v);
      voznjeLista.add(voznja);
    }
    return voznjeLista;
  }

}

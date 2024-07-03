/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.model.RestKlijentKazne;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.model.RestKlijentVozila;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Voznja;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("vozila")
@RequestScoped
public class KontrolerVozilo {

  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("vozilaPocetak.jsp")
  public void pocetak() {}

  @POST
  @Path("pretrazivanjeVoznji")
  @View("voznje.jsp")
  public void json(@FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
    RestKlijentVozila klijent = new RestKlijentVozila();
    List<Voznja> voznje = klijent.getVozilaJSON(odVremena, doVremena);
    model.put("voznje", voznje);
  }

  @POST
  @Path("pretrazivanjeVoznjiVozila")
  @View("voznje.jsp")
  public void vratiVoznjeVozila(@FormParam("id") String id) {
    RestKlijentVozila klijent = new RestKlijentVozila();
    List<Voznja> voznje = klijent.getVoznjeVozila(id);
    model.put("voznje", voznje);
  }

  @POST
  @Path("pretrazivanjeVoznjiInterval")
  @View("voznje.jsp")
  public void vratiVoznjeVozila(@FormParam("idVozila") String id, @FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    RestKlijentVozila klijent = new RestKlijentVozila();
    List<Voznja> voznje = klijent.getVoznjeVozilaInterval(id,odVremena, doVremena);
    model.put("voznje", voznje);

  }
  @POST
  @Path("pokreniVozilo")
  @View("pokreniVozilo.jsp")
  public void pokreniVozilo(@FormParam("idVozilo") String id) {
    RestKlijentVozila klijent = new RestKlijentVozila();
    String odgovor = klijent.pokreniVozilo(id);
    model.put("odgovor", odgovor);

  }
  @POST
  @Path("zaustaviVozilo")
  @View("pokreniVozilo.jsp")
  public void zaustaviVozilo(@FormParam("IdVozilo") String id) {
    RestKlijentVozila klijent = new RestKlijentVozila();
    String odgovor = klijent.zaustaviVozilo(id);
    model.put("odgovor", odgovor);

  }


}


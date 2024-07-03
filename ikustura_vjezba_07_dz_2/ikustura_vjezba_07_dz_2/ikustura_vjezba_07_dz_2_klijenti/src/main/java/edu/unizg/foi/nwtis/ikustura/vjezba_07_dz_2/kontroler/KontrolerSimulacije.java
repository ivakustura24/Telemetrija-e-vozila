/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.model.RestKlijentSimulacije;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.Voznja;
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
@Path("simulacije")
@RequestScoped
public class KontrolerSimulacije {

  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  private String kreirajKomandu(String redak, String idVozila, int brojRetka) {
    String podaci = redak.replace(",", " ");
    StringBuilder komanda= new StringBuilder();
    String razmak = " ";
    komanda.append("VOZILO").append(razmak).append(idVozila).
    append(razmak).append(brojRetka).append(razmak).append(podaci).append("\n");
    
    return komanda.toString();
  }

  @GET
  @Path("pocetak")
  @View("simulacijePocetak.jsp")
  public void pocetak() {}

  @POST
  @Path("pretrazivanjeVoznji")
  @View("simulacije.jsp")
  public void json(@FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
    RestKlijentSimulacije klijent = new RestKlijentSimulacije();
    List<Voznja> voznje = klijent.getVozilaJSON(odVremena, doVremena);
    model.put("voznje", voznje);
  }

  @POST
  @Path("pretrazivanjeVoznjiVozila")
  @View("simulacije.jsp")
  public void vratiVoznjeVozila(@FormParam("id") String id) {
    RestKlijentSimulacije klijent = new RestKlijentSimulacije();
    List<Voznja> voznje = klijent.getVoznjeVozila(id);
    model.put("voznje", voznje);
  }

  @POST
  @Path("pretrazivanjeVoznjiInterval")
  @View("simulacije.jsp")
  public void vratiVoznjeVozila(@FormParam("idVozila") String id,
      @FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
    RestKlijentSimulacije klijent = new RestKlijentSimulacije();
    List<Voznja> voznje = klijent.getVoznjeVozilaInterval(id, odVremena, doVremena);
    model.put("voznje", voznje);

  }



}


/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.kontroler;

import java.io.IOException;
import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.model.RestKlijentSimulacije;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Voznja;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.slusaci.ContextListener;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;


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

  @Context
  private ServletContext servletContext;
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
  @POST
  @Path("pokreniSimulaciju")
  @View("simulacije.jsp")
  public void pokreniSimulacijuVozila(@FormParam("csvDatoteka") String datoteka,
      @FormParam("trajanjePauze") int pauza, @FormParam("trajanjeSekundi") int sekunde) 
          throws InterruptedException, IOException{
      ContextListener contextListener = (ContextListener) servletContext.getAttribute("contextListener");
      if (contextListener != null) {
        contextListener.ucitajKonfiguraciju(datoteka, pauza, sekunde);
    } else {
        System.out.println("ContextListener nije dostupan!");
    }
  }



}


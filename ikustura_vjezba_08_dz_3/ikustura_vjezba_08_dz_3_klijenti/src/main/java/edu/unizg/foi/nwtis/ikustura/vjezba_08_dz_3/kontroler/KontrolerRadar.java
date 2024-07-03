/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.model.RestKlijentRadari;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.PodaciRadara;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("radari")
@RequestScoped
public class KontrolerRadar {

  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("radariPocetak.jsp")
  public void pocetak() {}

  @GET
  @Path("ispisRadara")
  @View("radariIspis.jsp")
  public void json() {
    RestKlijentRadari r = new RestKlijentRadari();
    List<PodaciRadara> radari = r.getRadariJSON();
    if(radari != null) {
      model.put("radari", radari);
    }
  }
  
  @POST
  @Path("vratiRadar")
  @View("radariIspis.jsp")
  public void vratiRadar(@FormParam("idRadar") String id) {
    RestKlijentRadari r = new RestKlijentRadari();
    List<PodaciRadara> radari = null;
    radari = r.getRadarJSON(id);
    if(radari != null) {
      model.put("radari", radari);
    }
    
    
  }
  @POST
  @Path("provjeriRadar")
  @View("provjeriRadar.jsp")
  public void provjeriRadar(@FormParam("id") String id) {
    RestKlijentRadari r = new RestKlijentRadari();
    String odgovor = r.getProvjeraRadara(id);
    model.put("odgovor", odgovor);
    
  }
  @GET
  @Path("obrisiRadare")
  @View("obrisiRadare.jsp")
  public void obrisiSRadare() {
   
  }
  
  @POST
  @Path("obrisiSveRadare")
  @View("obrisiRadare.jsp")
  public void obrisiSveRadare() {
    RestKlijentRadari r = new RestKlijentRadari();
    String odgovor = r.obrisiRadare();
    model.put("odgovor", odgovor);
  }
  
  @GET
  @Path("resetirajRadare")
  @View("provjeriRadar.jsp")
  public void resetirajRadare() {
    RestKlijentRadari r = new RestKlijentRadari();
    String odgovor = r.resetirajRadare();
    model.put("odgovor", odgovor);
  }
  
  @POST
  @Path("obrisiRadar")
  @View("obrisiRadare.jsp")
  public void obrisiRadar(@FormParam("idRadar") String id) {
    RestKlijentRadari r = new RestKlijentRadari();
    String odgovor = r.getObrisiRadar(id);
    if(odgovor !=null) {
      model.put("odgovor", odgovor);
    }
    
    
  }

}

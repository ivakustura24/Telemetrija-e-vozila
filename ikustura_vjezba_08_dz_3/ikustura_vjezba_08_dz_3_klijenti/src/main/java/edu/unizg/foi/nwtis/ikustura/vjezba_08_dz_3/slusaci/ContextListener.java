package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.slusaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ContextListener implements ServletContextListener {

  private ServletContext context = null;

  @Override
  public void contextInitialized(ServletContextEvent event) {
      context = event.getServletContext();
      context.setAttribute("contextListener", this);
  }

  public void ucitajKonfiguraciju(String datoteka, int trajanjePauza, int trajanjeSekunde) 
      throws InterruptedException, IOException {
      String path = context.getRealPath("/WEB-INF") + java.io.File.separator;
      double korekcijaVremena = trajanjeSekunde / 1000;
      BufferedReader citac = new BufferedReader(new FileReader(path + datoteka));
      String redak = citac.readLine();
      int brojac = 0;
      long prethodnoVrijeme = 0;
      long trenutnoVrijeme = 0;
      long vrijemeSpavanja = 0;
       while(redak !=null) {
         if(brojac != 0) {
           trenutnoVrijeme = 0;
           vrijemeSpavanja = (long) ((trenutnoVrijeme - prethodnoVrijeme) * korekcijaVremena);
           Thread.sleep(vrijemeSpavanja);
           int id = dohvatiId(datoteka);
           String komanda = kreirajKomandu(redak, id, brojac);
             try {
                 MrezneOperacije.posaljiZahtjevPosluzitelju("20.24.5.2", 8001, komanda);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         prethodnoVrijeme = trenutnoVrijeme;
         Thread.sleep(trajanjePauza);
         redak = citac.readLine();
         brojac++;
       }
  }

  private int dohvatiId(String datoteka) {
    String[] prviDio = datoteka.split("\\.");
    String podjela = prviDio[0];
    String[] dio = podjela.split("V");
    String broj = dio[1];

    return Integer.parseInt(broj);
  }

  private String kreirajKomandu(String redak, int id, int brojac) {
    String podaci = redak.replace(",", " ");
    StringBuilder komanda= new StringBuilder();
    String razmak = " ";
    komanda.append("VOZILO").append(razmak).append(id).
    append(razmak).append(brojac).append(razmak).append(podaci).append("\n");
    
    return komanda.toString();

  }
  public String vratiKonfiguracijskePostavke(String naziv) throws IOException {
    String vrijednost = "";
    String path = context.getRealPath("/WEB-INF") + java.io.File.separator;
    Properties properties = new Properties();
    InputStream input = new FileInputStream(path + "konfiguracija.txt");
    properties.load(input);
    vrijednost = properties.getProperty(naziv);
    return vrijednost;
  }
  @Override
  public void contextDestroyed(ServletContextEvent event) {
      context = event.getServletContext();
  }
}

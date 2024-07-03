package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.slusaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public final class ContextListener implements ServletContextListener {

  private ServletContext context = null;

  @Override
  public void contextInitialized(ServletContextEvent event) {
      context = event.getServletContext();
      context.setAttribute("contextListener", this);
  }

  public void ucitajKonfiguraciju(String datoteka) 
      throws InterruptedException, IOException {
      String path = context.getRealPath("/WEB-INF") + java.io.File.separator;
      BufferedReader citac = new BufferedReader(new FileReader(path + datoteka));
      
       
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
      context = event.getServletContext();
  }
}

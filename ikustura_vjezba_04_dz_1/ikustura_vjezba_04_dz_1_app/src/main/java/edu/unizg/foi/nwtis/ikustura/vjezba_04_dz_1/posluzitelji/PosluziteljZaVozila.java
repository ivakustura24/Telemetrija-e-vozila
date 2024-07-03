package edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji.radnici.RadnikZaVozila;

// TODO: Auto-generated Javadoc
/**
 * Klasa PosluziteljZaVozila.
 */
public class PosluziteljZaVozila implements Runnable {

  /** Mrežna vrata - mrežna vrata poslužitelja za vozila */
  private int mreznaVrata;
  
  /** Centralni sustav - objekt klase centralni sustav koji pokreće poslužitelje  */
  private CentralniSustav centralniSustav;

  
  /**
   * Instanciranje novog poslužitelja za radar 
   *
   * @param mreznaVrataVozila - mrežna vrata vozila iz konfiguracijske datoteke centralnog sustava
   * @param centralniSustav - program koji pokreće poslužitelje
   */
  public PosluziteljZaVozila(int mreznaVrataVozila, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrataVozila;
    this.centralniSustav = centralniSustav;
  }

  /**
   * Run - pokretanje poslužitelja za vozila - otvaranje mrežne utičnice i čitanje zahtjeva
   */
  @Override
  public void run() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = 
            new ServerSocket(this.mreznaVrata)){
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader citac =new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
        OutputStream out = mreznaUticnica.getOutputStream();
        var redak = citac.readLine();
        this.pokreniPosluzitelja(redak);
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
    
  }

  /**
   * Pokreni posluzitelja - kreiranje dretve RadnikZaVozila
   *
   * @param komanda - zahtjev koji se prosljeđuje RadnikuZaVozila
   */
  private void pokreniPosluzitelja(String komanda) {
    var tvornicaDretvi = Executors.newCachedThreadPool() ;
    tvornicaDretvi.submit(new RadnikZaVozila(mreznaVrata, komanda, centralniSustav));
  }
   
}

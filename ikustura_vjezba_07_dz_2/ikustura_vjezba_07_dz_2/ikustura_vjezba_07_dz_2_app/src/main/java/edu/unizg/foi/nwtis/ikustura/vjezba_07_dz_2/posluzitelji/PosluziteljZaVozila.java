package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaVozila;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

// TODO: Auto-generated Javadoc
/**
 * Klasa PosluziteljZaVozila.
 */
public class PosluziteljZaVozila implements Runnable {

  /** Mrežna vrata - mrežna vrata poslužitelja za vozila */
  private int mreznaVrata;
  
  /** Centralni sustav - objekt klase centralni sustav koji pokreće poslužitelje  */
  private CentralniSustav centralniSustav;
  ExecutorService tvornicaDretvi = Executors.newCachedThreadPool() ;
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
        this.pokreniPosluzitelja(mreznaUticnica);
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
  private void pokreniPosluzitelja(Socket mreznaUticnica) {
    
    tvornicaDretvi.submit(new RadnikZaVozila(mreznaVrata, centralniSustav ,mreznaUticnica));
    
  }
   
}

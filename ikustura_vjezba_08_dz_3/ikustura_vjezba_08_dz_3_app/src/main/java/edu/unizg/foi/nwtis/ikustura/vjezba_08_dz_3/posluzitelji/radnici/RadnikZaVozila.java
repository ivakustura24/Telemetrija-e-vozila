package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.Voznja;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.pomocnici.GpsUdaljenostBrzina;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.CentralniSustav;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.PracenaVoznjaRestKlijent;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.VoznjaRestKlijent;

// TODO: Auto-generated Javadoc
/**
 * Klasa RadnikZaVozila - provjerava udaljenost vozila od radara i šalje zahtjeve za vozila koja su
 * u dosegu radara
 */
public class RadnikZaVozila implements Runnable {

  /** Mrežna vrata - mrežna vrata vozila. */
  private int mreznaVrata;
  private Socket mreznaUticnica;

  /** Zahtjev - pročitana linija iz datoteke o svim podacima vozila */
  private String zahtjev;

  /** Centralni sustav - program koji pokreće poslužitelje */
  private CentralniSustav centralniSustav;
  public PracenaVoznjaRestKlijent restKlijent = new PracenaVoznjaRestKlijent();
  public VoznjaRestKlijent restKlijentVoznja = new VoznjaRestKlijent();
  /** Predlozak vozila - regularni izraz za provjeru zapisa iz csv datoteke */
  private Pattern predlozakVozila = Pattern
      .compile("^VOZILO (?<id>\\d+) (?<broj>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) "
          + "(?<snaga>-?\\d+([.]\\d+)?) (?<struja>-?\\d+([.]\\d+)?) (?<visina>-?\\d+([.]\\d+)?) "
          + "(?<gpsBrzina>-?\\d+([.]\\d+)?) (?<tempVozila>\\d+) (?<postotakBaterija>\\d+) "
          + "(?<naponBaterija>-?\\d+([.]\\d+)?) (?<kapacitetBaterija>\\d+) (?<tempBaterija>\\d+) "
          + "(?<preostaloKm>-?\\d+([.]\\d+)?) (?<ukupnoKm>-?\\d+([.]\\d+)?) "
          + "(?<gpsSirina>-?\\d+([.]\\d+)?) (?<gpsDuzina>-?\\d+([.]\\d+)?)$");

  /** Poklapanje vozila - provjera poklapanja zapisa i regularnog izraza predlozakVozila */
  private Matcher poklapanjeVozila;

  private Pattern predlozakProvjeraVozila = Pattern.compile("^VOZILO START (?<id>\\d+)$");
  private Matcher poklapanjeProvjeraVozila;

  private Pattern predlozakVozilaZaustavi = Pattern.compile("^VOZILO STOP (?<id>\\d+)$");
  private Matcher poklapanjeZaustaviVozilo;

  /**
   * Instanciranje novog radnika za vozila
   *
   * @param mreznaVrata mrežna vrata Vozila
   * @param komanda - zapis iz csv datoteke o vozilu
   * @param centralniSustav - objekt glavnog poslužitelja
   */
  public RadnikZaVozila(int mreznaVrata, CentralniSustav centralniSustav, Socket uticnica) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
    this.mreznaUticnica = uticnica;
  }

  /**
   * Run - pokretanje dretve, provjera regularnog izraza i udaljenosti vozila od radara slanje
   * zahtjeva prema poslužitelju
   */
  @Override
  public void run() {

    try {

      BufferedReader citac =
          new BufferedReader(new InputStreamReader(this.mreznaUticnica.getInputStream(), "utf8"));
      OutputStream out = mreznaUticnica.getOutputStream();
      PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
      var redak = citac.readLine();

      mreznaUticnica.shutdownInput();
      pisac.println(obradaZahtjeva(redak));
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      mreznaUticnica.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  private String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 20 Neispravna sintaksa komande\n";
    }
    obradaZahtjevaVozilo(zahtjev);
    var odgovor = obradaZahtjevaPokreni(zahtjev);
    if (odgovor == null) {
      odgovor = obradaZahtjevaZaustavi(zahtjev);
    }
    if (odgovor != null) {
      return odgovor;
    }
    return "ERROR 20 Neispravna sintaksa komande\n";
  }

  private String obradaZahtjevaPokreni(String zahtjev) {
    this.poklapanjeProvjeraVozila = this.predlozakProvjeraVozila.matcher(zahtjev);
    var status = this.poklapanjeProvjeraVozila.matches();
    if (status) {
      var id = Integer.valueOf(this.poklapanjeProvjeraVozila.group("id"));
      var postojiVozilo = provjeriVozilo(id);

      if (postojiVozilo == false) {
        this.centralniSustav.vozila.add(id);
      }
      return "OK\n";
    }
    return null;
  }

  private String obradaZahtjevaZaustavi(String zahtjev) {
    this.poklapanjeZaustaviVozilo = this.predlozakVozilaZaustavi.matcher(zahtjev);
    var status = this.poklapanjeZaustaviVozilo.matches();
    if (status) {
      var id = Integer.valueOf(this.poklapanjeZaustaviVozilo.group("id"));
      var postojiVozilo = provjeriVozilo(id);
      if (postojiVozilo == true) {
        this.centralniSustav.vozila.remove(id);
      }
      return "OK\n";
    }
    return null;
  }

  private boolean provjeriVozilo(Integer id) {
    if (this.centralniSustav.vozila.size() != 0) {
      for (Integer vozilo : this.centralniSustav.vozila) {
        if (vozilo.equals(id)) {
          return true;
        }
      }
      return false;
    }
    return false;
  }

  private void obradaZahtjevaVozilo(String zahtjev) {
    this.poklapanjeVozila = this.predlozakVozila.matcher(zahtjev);
    var status = poklapanjeVozila.matches();
    if (status) {

      Double gpsSirina = Double.valueOf(this.poklapanjeVozila.group("gpsSirina"));
      Double gpsDuzina = Double.valueOf(this.poklapanjeVozila.group("gpsDuzina"));
      Voznja voznja = kreirajVoznju(poklapanjeVozila);
      //boolean voznjaZabiljezena = restKlijentVoznja.postVoznjaJSON(voznja);
      for (var radar : this.centralniSustav.sviRadari.entrySet()) {

        Double udaljenost = GpsUdaljenostBrzina.udaljenostKm(gpsSirina, gpsDuzina,
            radar.getValue().gpsSirina(), radar.getValue().gpsDuzina()) / 1000;
        var id = Integer.valueOf(this.poklapanjeVozila.group("id"));
        boolean uspjesnoSlanje = false;


        if (udaljenost <= radar.getValue().maksUdaljenost()) {
          if (provjeriVozilo(id) == true) {
            //uspjesnoSlanje = restKlijent.postPracenaVoznjaJSON(voznja);
          }

          if (uspjesnoSlanje || !provjeriVozilo(id)) {
            String komanda = vratiKomandu(poklapanjeVozila);
            var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(
                radar.getValue().adresaRadara(), radar.getValue().mreznaVrataRadara(), komanda);

          }
        }
      }
    }
  }

  private Voznja kreirajVoznju(Matcher poklapanjeVozila) {

    int id = Integer.valueOf(poklapanjeVozila.group("id"));
    int broj = Integer.valueOf(poklapanjeVozila.group("broj"));
    long vrijeme = Long.valueOf(poklapanjeVozila.group("vrijeme"));
    double brzina = Double.valueOf(poklapanjeVozila.group("brzina"));
    double snaga = Double.valueOf(poklapanjeVozila.group("snaga"));
    double struja = Double.valueOf(poklapanjeVozila.group("struja"));
    double visina = Double.valueOf(poklapanjeVozila.group("visina"));
    double gpsBrzina = Double.valueOf(poklapanjeVozila.group("gpsBrzina"));
    int tempVozila = Integer.valueOf(poklapanjeVozila.group("tempVozila"));
    int postotakBaterija = Integer.valueOf(poklapanjeVozila.group("postotakBaterija"));
    double naponBaterija = Double.valueOf(poklapanjeVozila.group("naponBaterija"));
    int kapacitetBaterija = Integer.valueOf(poklapanjeVozila.group("kapacitetBaterija"));
    int tempBaterija = Integer.valueOf(poklapanjeVozila.group("tempBaterija"));
    double preostaloKm = Double.valueOf(poklapanjeVozila.group("preostaloKm"));
    double ukupnoKm = Double.valueOf(poklapanjeVozila.group("ukupnoKm"));
    double gpsSirina = Double.valueOf(poklapanjeVozila.group("gpsSirina"));
    double gpsDuzina = Double.valueOf(poklapanjeVozila.group("gpsDuzina"));

    Voznja novaVoznja = new Voznja(id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina,
        tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm,
        ukupnoKm, gpsSirina, gpsDuzina);
    return novaVoznja;
  }

  /**
   * Vrati komandu - definiranje komande koja se šalje poslužitelju
   *
   * @param poklapanjeVozila podaci o vozilu iz zahtjeva
   * @return zahtjev koji se šalje poslužitelju za provjeru brzine
   */
  public String vratiKomandu(Matcher poklapanjeVozila) {
    StringBuilder komanda = new StringBuilder();
    String razmak = " ";
    komanda.append("VOZILO").append(razmak)
        .append(Integer.valueOf(this.poklapanjeVozila.group("id"))).append(razmak)
        .append(Long.valueOf(this.poklapanjeVozila.group("vrijeme"))).append(razmak)
        .append(Double.valueOf(this.poklapanjeVozila.group("brzina"))).append(razmak)
        .append(Double.valueOf(this.poklapanjeVozila.group("gpsSirina"))).append(razmak)
        .append(Double.valueOf(this.poklapanjeVozila.group("gpsDuzina"))).append("\n");
    return komanda.toString();
  }


}

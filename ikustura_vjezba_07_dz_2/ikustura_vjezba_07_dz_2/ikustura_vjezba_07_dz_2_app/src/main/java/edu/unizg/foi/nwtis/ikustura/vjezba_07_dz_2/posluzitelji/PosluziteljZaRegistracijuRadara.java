package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.pomocnici.MrezneOperacije;


// TODO: Auto-generated Javadoc
/**
 * Klasa PosluziteljZaRegistracijuRadara - obavlja dodavanje i brisanje radara
 */
public class PosluziteljZaRegistracijuRadara implements Runnable {

  /** Mrežna vrata - mrežna vrata poslužitelja */
  private int mreznaVrata;

  /** Centralni sustav - poslužitelj koji pokreće druge poslužitelje */
  private CentralniSustav centralniSustav;

  /** Predložak registracije radara - regularni izraz za provjeru zahtjeva za dodavanje radara */
  private Pattern predlozakRegistracijeRadara =
      Pattern.compile("^RADAR (?<id>\\d+) (?<adresa>\\w+) (?<mreznaVrata>\\d+) "
          + "(?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<maksUdaljenost>\\d+)$");


  /**
   * Poklapanje registracije radara - provjera poklapanja zahtjeva i regularnog izraza
   * predlozakRegistracijeRadara
   */
  private Matcher poklapanjeRegistracijeRadara;

  /** Predlozak obrisi radar - regularni izraz za poklapanje brisanja jednog radara */
  private Pattern predlozakObrisiRadar = Pattern.compile("^RADAR OBRIŠI (?<id>\\d+)$");

  /**
   * Poklapanje brisanja radara - provjera poklapanja zahtjeva i regularnog izraza
   * poklapanjeBrisanja radara
   */
  private Matcher poklapanjeBrisanjaRadara;

  /** Predlozak obriši sve radare - regularni izraz za provjeru zahtjeva za brisanje svih radara */
  private Pattern predlozakObrisiSveRadare = Pattern.compile("^RADAR OBRIŠI SVE$");

  /** Poklapanje brisanja svih radara - provjera poklapanja zahtjeva i predlozakObrisiSveRadare */
  private Matcher poklapanjeBrisanjaSvihRadara;

  private Pattern predlozakProvjeriRadar = Pattern.compile("^RADAR (?<id>\\d+)$");
  private Matcher poklapanjeProvjeraRadara;

  private Pattern predlozakSviRadari = Pattern.compile("^RADAR SVI$");

  private Pattern predlozakDeregistracijaRadara = Pattern.compile("^RADAR RESET$");
  private Matcher poklapanjeDeregistracijaRadara;
  /** Poklapanje */
  private Matcher poklapanjeSviRadari;

  /**
   * Instanciranje novog poslužitelja radara
   *
   * @param mreznaVrata mrežna vrata PosluziteljaZaRegistracijuRadara
   * @param centralniSustav objekt klase CentralniSustav
   */
  public PosluziteljZaRegistracijuRadara(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }


  /**
   * Run - otvaranje mrežne utičnice poslužitelja, čitanje zahtjeva klijenta
   */
  @Override
  public void run() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader citac =
            new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
        OutputStream out = mreznaUticnica.getOutputStream();
        PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
        var redak = citac.readLine();

        mreznaUticnica.shutdownInput();
        pisac.println(obradaZahtjeva(redak));

        pisac.flush();
        mreznaUticnica.shutdownOutput();
        mreznaUticnica.close();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Obrada zahtjeva klijenta - obrađuju se zahtjevi za brisanje jednog i svih radara te
   * registracija radara
   *
   * @param zahtjev - zahtjev klijenta
   * @return OK ako je obrada zahtjeva uspješna, pogreška ako je neispravna sintaksa ili se dogodila
   *         neka druga pogreška
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 10 Neispravna sintaksa komande.\n";
    }
    var odgovor = obradaZahtjevaRegistracijeRadara(zahtjev);
    if (odgovor == null) {
      odgovor = obradaZahtjevaObrisiRadar(zahtjev);
    }
    if (odgovor == null) {
      odgovor = obradaZahtjevaObrisiSveRadare(zahtjev);
    }
    if (odgovor == null) {
      odgovor = obradaZahtjevaProvjeriRadar(zahtjev);
    }
    if (odgovor == null) {
      odgovor = obradaZahtjevaVratiSveRadare(zahtjev);
    }
    if (odgovor == null) {
      odgovor = obradaZahtjevaDeregistracijeRadara(zahtjev);
    }
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 10 Neispravna sintaksa komande.\n";
  }

  private String obradaZahtjevaDeregistracijeRadara(String zahtjev) {
    this.poklapanjeDeregistracijaRadara = this.predlozakDeregistracijaRadara.matcher(zahtjev);
    var statusPoklapanjeDeregistracija = poklapanjeDeregistracijaRadara.matches();
    if (statusPoklapanjeDeregistracija) {
      int ukupanBrojRadara = this.centralniSustav.sviRadari.size();
      int deregistriraniRadari = 0;
      for (Entry<Integer, PodaciRadara> radar : this.centralniSustav.sviRadari.entrySet()) {
        String komanda = "RADAR " + radar.getValue().id();
        String odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("localhost",
            radar.getValue().mreznaVrataRadara(), komanda);
        if (odgovor == null) {
          this.centralniSustav.sviRadari.remove(radar.getKey(), radar.getValue());
          deregistriraniRadari++;
        }
      }
      return "OK " + ukupanBrojRadara + " " + deregistriraniRadari + "\n";
    }
    return null;
  }


  private String obradaZahtjevaVratiSveRadare(String zahtjev) {
    this.poklapanjeSviRadari = this.predlozakSviRadari.matcher(zahtjev);
    var statusPoklapanjeSviRadari = poklapanjeSviRadari.matches();
    if (statusPoklapanjeSviRadari) {
      String sviRadari = "OK {";
      for (Entry<Integer, PodaciRadara> radar : this.centralniSustav.sviRadari.entrySet()) {
        sviRadari += "[" + radar.getValue().id() + " " + radar.getValue().adresaRadara() + " "
            + radar.getValue().mreznaVrataRadara() + " " + radar.getValue().gpsSirina() + " "
            + radar.getValue().gpsDuzina() + " " + radar.getValue().maksUdaljenost() + "], ";
      }
      if (sviRadari.length() > 4) {
        sviRadari = sviRadari.substring(0, sviRadari.length() - 2);
      }

      sviRadari += "}\n";
      return sviRadari;
    }
    return null;
  }


  public String obradaZahtjevaProvjeriRadar(String zahtjev) {
    this.poklapanjeProvjeraRadara = this.predlozakProvjeriRadar.matcher(zahtjev);
    var statusPoklapanjeProvjeraRadara = poklapanjeProvjeraRadara.matches();
    if (statusPoklapanjeProvjeraRadara) {
      for (Entry<Integer, PodaciRadara> radar : this.centralniSustav.sviRadari.entrySet()) {
        if (radar.getKey() == Integer.valueOf(this.poklapanjeProvjeraRadara.group("id"))) {
          return "OK\n";
        }
      }
      return "ERROR 12 Ne postoji radar sa zadanim id.\n";
    }

    return null;
  }



  /**
   * Obrada zahtjeva registracije radara - dodavanje novih radara
   *
   * @param zahtjev - zahtjev za dodavanje novih radara
   * @return OK, ako je sve uredu, pogreška ako je radar spremljen i null ako je sintaksa neispravna
   */
  public String obradaZahtjevaRegistracijeRadara(String zahtjev) {
    this.poklapanjeRegistracijeRadara = this.predlozakRegistracijeRadara.matcher(zahtjev);
    var statusRegistracijaRadara = poklapanjeRegistracijeRadara.matches();
    if (statusRegistracijaRadara) {
      var radar = new PodaciRadara(Integer.valueOf(this.poklapanjeRegistracijeRadara.group("id")),
          this.poklapanjeRegistracijeRadara.group("adresa"),
          Integer.valueOf(this.poklapanjeRegistracijeRadara.group("mreznaVrata")), -1, -1,
          Integer.valueOf(this.poklapanjeRegistracijeRadara.group("maksUdaljenost")), null, -1,
          null, -1, null, Double.valueOf(this.poklapanjeRegistracijeRadara.group("gpsSirina")),
          Double.valueOf(this.poklapanjeRegistracijeRadara.group("gpsDuzina")));
      if (this.centralniSustav.sviRadari.contains(radar)) {
        return "ERROR 11 radar je već spremljen!\n";
      } else {
        centralniSustav.sviRadari.put(radar.id(), radar);
        return "OK\n";
      }
    }
    return null;
  }

  /**
   * Obrada zahtjeva obrisi radar - obrađuje se zahtjev za brisanje jednog radara
   *
   * @param zahtjev - zahtjev za brisanje radara
   * @return OK, ako je sve uredu, pogreška ako je radar ne postoji i null ako je sintaksa
   *         neispravna
   */
  private String obradaZahtjevaObrisiRadar(String zahtjev) {
    this.poklapanjeBrisanjaRadara = this.predlozakObrisiRadar.matcher(zahtjev);
    var statusBrisanjaRadara = poklapanjeBrisanjaRadara.matches();
    if (statusBrisanjaRadara) {
      int id = Integer.valueOf(this.poklapanjeBrisanjaRadara.group("id"));
      if (this.centralniSustav.sviRadari.containsKey(id)) {
        this.centralniSustav.sviRadari.remove(id);
        return "OK\n";
      }
      return "ERROR 12 Ne postoji radar sa zadanim id!\n";
    }
    return null;
  }

  /**
   * Obrada zahtjeva obrisi sve radare - brisanje svih radara iz sustava
   *
   * @param zahtjev - zahtjev za brisanje svih radara
   * @return OK, ako je sve uredu, pogreška ako ne postoje zapisani radari i null ako je sintaksa
   *         neispravna
   */
  private String obradaZahtjevaObrisiSveRadare(String zahtjev) {
    this.poklapanjeBrisanjaSvihRadara = this.predlozakObrisiSveRadare.matcher(zahtjev);
    var statusBrisanjaSvihRadara = poklapanjeBrisanjaSvihRadara.matches();
    if (statusBrisanjaSvihRadara) {
      if (this.centralniSustav.sviRadari.size() != 0) {
        this.centralniSustav.sviRadari.clear();
        return "OK\n";
      }
      return "ERROR 19 Ne postoje zapisani radari!\n";
    }
    return null;
  }
}

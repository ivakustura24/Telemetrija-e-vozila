package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.PosluziteljRadara;

// TODO: Auto-generated Javadoc
/**
 * Klasa RadnikZaRadare.
 */
public class RadnikZaRadare implements Runnable {

  /** Mrežna utičnica poslužitelja za vozila */
  private Socket mreznaUticnica;

  /** Podaci radara iz konfiguracijske datoteke */
  private PodaciRadara podaciRadara;

  /** Posluzitelj radara - objekt poslužitelja radara, čuva spremljene podatke o brzim vozilima */
  public PosluziteljRadara posluziteljRadara;

  /** Predlozak brzine - regularni izraz koji provjerava zahtjev s trenutnim podacima vozila */
  private Pattern predlozakBrzine =
      Pattern.compile("^VOZILO (?<id>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) "
          + "(?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");

  /** Poklapanje brzine - provjerava se poklapanje regularnog izraza i predloška predlozakBrzine */
  private Matcher poklapanjeBrzine;

  private Pattern predlozakProvjeraRadara = Pattern.compile("^RADAR (?<id>\\d+)$");
  private Matcher poklapanjeProvjeraRadara;

  private Pattern predlozakResetRadara = Pattern.compile("^RADAR RESET$");
  private Matcher poklapanjeResetRadara;

  /**
   * Instanciranje novog objekta RadnikZaRadare
   *
   * @param mreznaUticnica - mrežna utičnica poslužitelja
   * @param podaciRadara - podaci o radaru iz konfiguracijske datoteke
   * @param posluziteljRadara - objekt poslužitelja
   */
  public RadnikZaRadare(Socket mreznaUticnica, PodaciRadara podaciRadara,
      PosluziteljRadara posluziteljRadara) {
    this.mreznaUticnica = mreznaUticnica;
    this.podaciRadara = podaciRadara;
    this.posluziteljRadara = posluziteljRadara;

  }

  /**
   * Run - pokretanje, otvaranje mrežne utičnice i čitanje zahtjeva
   */
  @Override
  public void run() {
    try {
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

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  /**
   * Obrada zahtjeva - obrada zahtjeva klijenta
   *
   * @param zahtjev - zahtjev klijenta s podacima o vozilu
   * @return OK, ako je sve uredu, pogreška ako je neispravna sintaksa ili PosluziteljKazni nije
   *         aktivan
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 30 Neispravna sintaksa komande!.\n";
    }
    var odgovor = obradaZahtjevaBrzine(zahtjev);

    if (odgovor == null) {
      odgovor = obradaZahtjevaProvjeraRadara(zahtjev);
    }
    if (odgovor == null) {
      odgovor = obradaZahtjevaResetRadara(zahtjev);
    }
    if (odgovor != null) {
      return odgovor;
    }
    return "ERROR 30 Neispravna sintaksa komande.\n";
  }


  private String obradaZahtjevaResetRadara(String zahtjev) {
    this.poklapanjeResetRadara = this.predlozakResetRadara.matcher(zahtjev);
    var status = poklapanjeResetRadara.matches();
    if (status) {
      String komanda = "RADAR " + this.podaciRadara.id()+ "\n";
      var odgovor =
          MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
              this.podaciRadara.mreznaVrataRegistracije(), komanda);
      if(odgovor.equals("OK")) {
        return "OK\n";
      }
      else {
        boolean uspjesnaRegistracija = this.posluziteljRadara.registracijaPosluzitelja();
        if(uspjesnaRegistracija) {
          return "OK\n";
        }
        else {
          return "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan!\n";
        }
     }
    }
    return null;
  }

  private String obradaZahtjevaProvjeraRadara(String zahtjev) {
    this.poklapanjeProvjeraRadara = this.predlozakProvjeraRadara.matcher(zahtjev);
    var status = poklapanjeProvjeraRadara.matches();
    if (status) {
      if (this.podaciRadara.id() == Integer.valueOf(this.poklapanjeProvjeraRadara.group("id"))) {
        String komanda = "TEST";
        var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaKazne(),
            this.podaciRadara.mreznaVrataKazne(), komanda);
        if (odgovor.equals("OK\n")) {
          return "OK\n";
        } else {
          return "ERROR 31 Radar nije aktivan\n";
        }
      }
    }
    return null;
  }

  /**
   * Obrada zahtjeva brzine - provjerava se vozi li vozilo brzo te koliko dugo, zapisuju se brza
   * vozila i šalju zahtjevi (kazne) prema PosluziteljKazni
   *
   * @param zahtjev s podacima o vozilu
   * @return OK, ako je sve uredu,pogreška ako je neispravna sintaksa ili PosluziteljKazni nije
   *         aktivan
   */
  public String obradaZahtjevaBrzine(String zahtjev) {
    this.poklapanjeBrzine = this.predlozakBrzine.matcher(zahtjev);
    var status = poklapanjeBrzine.matches();
    if (status) {
      BrzoVozilo brzoVozilo = vratiBrzoVozilo(poklapanjeBrzine);
      BrzoVozilo postojeceVozilo = vratiVozilo(brzoVozilo);
      if (Double.valueOf(this.poklapanjeBrzine.group("brzina")) > this.podaciRadara.maksBrzina()) {
        if (postojeceVozilo != null) {
          double vrijemeBrzeVoznje = brzoVozilo.vrijeme() - postojeceVozilo.vrijeme();
          if (vrijemeBrzeVoznje > this.podaciRadara.maksTrajanje()) {
            if (vrijemeBrzeVoznje < 2 * this.podaciRadara.maksTrajanje() * 1000) {
              String komanda = kreirajKomandu(postojeceVozilo, brzoVozilo);
              ponistiPodatkeVozila(postojeceVozilo);
              var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(
                  this.podaciRadara.adresaKazne(), this.podaciRadara.mreznaVrataKazne(), komanda);
              if (odgovor == null) {
                return "ERROR 31 Posluzitelj Kazni nije aktivan.\n";
              }
              return "OK\n";
            } else {
              ponistiPodatkeVozila(postojeceVozilo);
              return "OK\n";
            }
          } else {
            return "OK\n";
          }
        } else {
          this.posluziteljRadara.pracenjeVozila.add(brzoVozilo);
          return ("OK\n");
        }
      } else {
        if (postojeceVozilo != null) {
          ponistiPodatkeVozila(postojeceVozilo);
        }
        return ("OK\n");
      }
    }
    return "null";
  }

  /**
   * Vrati brzo vozilo - objekt s podacima o trenutnoj vožnji vozila
   *
   * @param poklapanjeBrzine podaci o vozilu iz zahtjeva
   * @return objekt BrzoVozilo
   */
  private BrzoVozilo vratiBrzoVozilo(Matcher poklapanjeBrzine) {
    BrzoVozilo brzoVozilo = new BrzoVozilo(Integer.valueOf(this.poklapanjeBrzine.group("id")), -1,
        Long.valueOf(this.poklapanjeBrzine.group("vrijeme")),
        Double.valueOf(this.poklapanjeBrzine.group("brzina")),
        Double.valueOf(this.poklapanjeBrzine.group("gpsSirina")),
        Double.valueOf(this.poklapanjeBrzine.group("gpsDuzina")), true);
    return brzoVozilo;
  }

  /**
   * Kreiraj komandu - definiranje komande za slanje prema PosluziteljKazni
   *
   * @param postojeceVozilo podaci vozila koji se već nalaze u redu praćenjeVozila
   * @param brzoVozilo - trenutni podaci o vozilu
   * @return zahtjev za kaznu
   */
  private String kreirajKomandu(BrzoVozilo postojeceVozilo, BrzoVozilo brzoVozilo) {
    StringBuilder komanda = new StringBuilder();
    komanda.append("VOZILO").append(" ").append(brzoVozilo.id()).append(" ")
        .append(postojeceVozilo.vrijeme()).append(" ").append(brzoVozilo.vrijeme()).append(" ")
        .append(brzoVozilo.brzina()).append(" ").append(brzoVozilo.gpsSirina()).append(" ")
        .append(brzoVozilo.gpsDuzina()).append(" ").append(this.podaciRadara.gpsSirina())
        .append(" ").append(this.podaciRadara.gpsDuzina()).append("\n");

    return komanda.toString();
  }

  /**
   * Ponisti podatke vozila - postavljanje statusa BrzoVozilo na false - u slučaju da je generirana
   * kazna, vozilo smanjilo brzinu ili vozi 2 puta duže vremenski nego što radar dopušta
   *
   * @param postojeceVozilo vozilo koje se nalazi u redu pracenjeVozila
   */
  public void ponistiPodatkeVozila(BrzoVozilo postojeceVozilo) {
    for (BrzoVozilo trazenoVozilo : this.posluziteljRadara.pracenjeVozila) {
      if (trazenoVozilo.id() == postojeceVozilo.id() && trazenoVozilo.status() == true) {
        BrzoVozilo brzoVozilo = trazenoVozilo.postaviStatus(false);
        posluziteljRadara.pracenjeVozila.remove(trazenoVozilo);
        posluziteljRadara.pracenjeVozila.add(brzoVozilo);
      }
    }
  }


  /**
   * Vrati vozilo - vozilo iz reda pracenje vozila
   *
   * @param brzoVozilo - podaci vozila koji se trenutno obrađuju
   * @return vozilo koje već postoji u redu pracenjeVozila, null ako vozilo ne postoji u redu
   */
  public BrzoVozilo vratiVozilo(BrzoVozilo brzoVozilo) {
    for (BrzoVozilo trazenoVozilo : this.posluziteljRadara.pracenjeVozila) {
      if (trazenoVozilo.id() == brzoVozilo.id() && trazenoVozilo.status() == true) {
        return trazenoVozilo;
      }
    }
    return null;
  }



}

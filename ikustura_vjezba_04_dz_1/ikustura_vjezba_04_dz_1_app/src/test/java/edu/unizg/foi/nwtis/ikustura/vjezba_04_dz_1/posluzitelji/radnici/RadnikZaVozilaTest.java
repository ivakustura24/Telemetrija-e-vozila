package edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji.radnici;

import static org.junit.jupiter.api.Assertions.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.pomocnici.GpsUdaljenostBrzina;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji.CentralniSustav;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RadnikZaVozilaTest {
  private Pattern predlozakVozila = Pattern
      .compile("^VOZILO (?<id>\\d+) (?<broj>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) "
          + "(?<snaga>-?\\d+([.]\\d+)?) (?<struja>-?\\d+([.]\\d+)?) (?<visina>-?\\d+([.]\\d+)?) "
          + "(?<gpsBrzina>-?\\d+([.]\\d+)?) (?<tempVozila>\\d+) (?<postotakBaterija>\\d+) "
          + "(?<naponBaterija>-?\\d+([.]\\d+)?) (?<kapacitetBaterija>\\d+) (?<tempBaterija>\\d+) "
          + "(?<preostaloKm>-?\\d+([.]\\d+)?) (?<ukupnoKm>-?\\d+([.]\\d+)?) "
          + "(?<gpsSirina>-?\\d+([.]\\d+)?) (?<gpsDuzina>-?\\d+([.]\\d+)?)$");
  private int mreznaVrata;
  private String komanda ;
  private Matcher poklapanjeVozila;
  private RadnikZaVozila radnikVozila;
  private String zahtjev;
  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {
    this.zahtjev = "VOZILO 1 101 1708073749078 0.02 0.8086 "
        + "0.02 214.2 1.337297 19 93 40.43 7314 "
        + "20 27.9 816.458 46.286644 16.35285";
//    
  }

  @AfterEach
  void tearDown() throws Exception {
  }


  @Test
  @Order(2)
  void testRun() {
    boolean ispravanIzracun = false;
    CentralniSustav centralniSustav = new CentralniSustav();
    this.poklapanjeVozila = this.predlozakVozila.matcher(zahtjev);
    var status = poklapanjeVozila.matches();
    assertTrue(status);
    Double gpsSirina = Double.valueOf(this.poklapanjeVozila.group("gpsSirina"));
    Double gpsDuzina = Double.valueOf(this.poklapanjeVozila.group("gpsDuzina"));
    if(status) {
      Double udaljenost = GpsUdaljenostBrzina.udaljenostKm(gpsSirina, gpsDuzina, 
          46.29950, 16.33001) / 1000;
          if(udaljenost <= 1000) {
            ispravanIzracun = true;
            
          }
      
    }
    assertTrue(ispravanIzracun);
  }

  @Test
  @Order(3)
  void testVratiKomandu() {
    String ocekivanaKomanda = "VOZILO 1 1708073749078 0.02 46.286644 16.35285\n";
    this.poklapanjeVozila = this.predlozakVozila.matcher(zahtjev);
    var status = poklapanjeVozila.matches();
    assertTrue(status);
    StringBuilder komanda = new StringBuilder();
    String razmak = " ";
    komanda.append("VOZILO").append(razmak).append(Integer.valueOf(this.poklapanjeVozila.group("id")))
    .append(razmak).append(Long.valueOf(this.poklapanjeVozila.group("vrijeme")))
    .append(razmak).append(Double.valueOf(this.poklapanjeVozila.group("brzina")))
    .append(razmak).append(Double.valueOf(this.poklapanjeVozila.group("gpsSirina")))
    .append(razmak).append(Double.valueOf(this.poklapanjeVozila.group("gpsDuzina"))).append("\n");
    assertEquals(ocekivanaKomanda, komanda.toString());
  }

}

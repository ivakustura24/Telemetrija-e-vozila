package edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji.radnici;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji.PosluziteljRadara;

class RadnikZaRadareTest {
  private Queue<BrzoVozilo> pracenjeVozila = new ConcurrentLinkedQueue<>();
  private int mreznaUticnica; 
  private PodaciRadara podaciRadara;
  private PosluziteljRadara posluziteljRadara = new PosluziteljRadara();
  private Pattern predlozakBrzine = Pattern.compile("^VOZILO (?<id>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) "
      + "(?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");
  private Matcher poklapanjeBrzine;
  
  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {
    pracenjeVozila.add(new BrzoVozilo(1, 123, 1711347009, 120.5, 45.813326, 15.97659, false));
    pracenjeVozila.add(new BrzoVozilo(2, 456, 1711348009, 110.0, 46.123456, 16.987654, false));
    pracenjeVozila.add(new BrzoVozilo(3, 789, 1711349009, 130.2, 47.13579, 17.24680, true));
  
  }

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void testRadnikZaRadare() {
    this.mreznaUticnica = 8000;
    this.podaciRadara = new PodaciRadara(1, "localhost", 8011,
        15, 7, 1000, "localhost", 8000, "localhost", 8020, 
        "Jalkovecka 52, 42000 Varazdin", 46.29950, 16.33001);
    this.posluziteljRadara = new PosluziteljRadara();
  }


  @Test
  void testObradaZahtjeva() {
    String zahtjev = "VOZILO 1 1711348009 21.767 46.286602 16.353136";
    this.poklapanjeBrzine = this.predlozakBrzine.matcher(zahtjev);
    var status = poklapanjeBrzine.matches(); 
    assertTrue(status);
  }


  @Test
  void testPonistiPodatkeVozila() {
    int id = 1 ; 
    boolean postavljenStatus = false;
    BrzoVozilo postojeceVozilo = new BrzoVozilo (3, 789, 1711349009, 130.2, 47.13579, 17.24680, true);
    for (BrzoVozilo trazenoVozilo : pracenjeVozila) {
      if (trazenoVozilo.id() == postojeceVozilo.id() && trazenoVozilo.status() == true) {
        BrzoVozilo brzoVozilo = trazenoVozilo.postaviStatus(false);
        pracenjeVozila.remove(trazenoVozilo);
        pracenjeVozila.add(brzoVozilo);
        if(brzoVozilo.status() == false) {
          postavljenStatus = true;
        }
        
      }
    }
    assertTrue(postavljenStatus);
  }

  @Test
  void testVratiVozilo() {
    int id = 1 ; 
    boolean pronadenoVozilo = false;
    BrzoVozilo postojeceVozilo = new BrzoVozilo (3, 789, 1711349009, 130.2, 47.13579, 17.24680, true);
    for (BrzoVozilo trazenoVozilo : pracenjeVozila) {
      if (trazenoVozilo.equals(postojeceVozilo)) {
        pronadenoVozilo = true;
       
      }
    }
  assertTrue(pronadenoVozilo);
}
}

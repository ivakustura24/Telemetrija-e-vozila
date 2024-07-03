package edu.unizg.foi.nwtis.ikustura.vjezba_04_dz_1.posluzitelji;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PosluziteljKazniTest {
  
  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}
  private PosluziteljKazni posluziteljKazni;

  @BeforeEach
  void setUp() throws Exception {
    posluziteljKazni = new PosluziteljKazni();
  }

  @AfterEach
  void tearDown() throws Exception {
    posluziteljKazni = null;
  }
  
  @Test
  @Order(5)
  void testMain() {
    var status = false;
    var mreznaVrata = 8020;
    this.posluziteljKazni.mreznaVrata = mreznaVrata;
    try {
      InetSocketAddress isa = new InetSocketAddress("localhost", this.posluziteljKazni.mreznaVrata);
      Socket s = new Socket();
      s.connect(isa, 70);
      s.close();
    } catch (Exception e) {
      status = true;
    }
    assertTrue(status);

    var nazivDatoteke = "PosluziteljKazni.txt";
    String[] argumenti = {nazivDatoteke};
    this.posluziteljKazni.mreznaVrata = mreznaVrata;
    try {
      Konfiguracija k = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(nazivDatoteke);
      k.spremiPostavku("mreznaVrataKazne", Integer.toString(this.posluziteljKazni.mreznaVrata));
      k.spremiKonfiguraciju();
    } catch (NeispravnaKonfiguracija | NumberFormatException e) {
      e.printStackTrace();
    }

    var dretva = Thread.ofVirtual().factory().newThread(() -> PosluziteljKazni.main(argumenti));
    dretva.start();

    status = true;
    this.posluziteljKazni.mreznaVrata = mreznaVrata;
    try {
      Thread.sleep(100);
      InetSocketAddress isa = new InetSocketAddress("localhost", this.posluziteljKazni.mreznaVrata);
      Socket s = new Socket();
      s.connect(isa, 70);
      s.close();
    } catch (Exception e) {
      status = false;
    }
    assertTrue(status);
    dretva.interrupt();
    this.obrisiDatoteku(nazivDatoteke);
  }
  @Test
  @Order(1)
  void testPreuzmiPostavke() {
    var nazivDatoteke = "PosluziteljKazni.txt";
    try {
      Konfiguracija k = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(nazivDatoteke);
      k.spremiPostavku("mreznaVrataKazne", "8020");
      k.spremiKonfiguraciju();
      String[] argumenti = {nazivDatoteke};
      this.posluziteljKazni.preuzmiPostavke(argumenti);
      assertEquals(Integer.valueOf(k.dajPostavku("mreznaVrataKazne")).intValue(),
          this.posluziteljKazni.mreznaVrata);
    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      e.printStackTrace();
    }
    this.obrisiDatoteku(nazivDatoteke);
  }
  
  @Test
  void testPokreniPosluzitelja() {
    var pokrenutPosluzitelj = true;
    this.posluziteljKazni.mreznaVrata = 8020;
    try {
      Thread.sleep(100);
      InetSocketAddress isa = new InetSocketAddress("localhost", this.posluziteljKazni.mreznaVrata);
      Socket s = new Socket();
      s.connect(isa, 70);
      pokrenutPosluzitelj = s.isConnected();
      assertTrue(pokrenutPosluzitelj);
      s.close();
    } catch (Exception e) {
      pokrenutPosluzitelj = false;
    }
  }

  @Test
  void testObradaZahtjeva() {
    var zahtjev = "VOZILO 1 1711348009 1711368009 21.767 46.286608 16.353131 "
        + "46.286602 16.353136";
    PosluziteljKazni pk = new PosluziteljKazni();
    String o = pk.obradaZahtjeva(zahtjev);
    assertEquals("OK\n", o);
  }

  @Test
  void testObradaZahtjevaKazna() {
    var zahtjev = "VOZILO 1 1711348009 1711368009 21.767 46.286608 16.353131 "
        + "46.286602 16.353136";
    String odgovor = this.posluziteljKazni.obradaZahtjevaKazna(zahtjev);
    assertEquals("OK\n", odgovor);
    zahtjev = "VOZILO 1 1711348009 1711368009 21.767 46.286608 16.353131 ";
    odgovor = this.posluziteljKazni.obradaZahtjevaKazna(zahtjev);
    assertEquals(null, odgovor);
  }
  
  private boolean obrisiDatoteku(String nazivDatoteke) {
    File f = new File(nazivDatoteke);

    if (!f.exists()) {
      return true;
    } else if (f.exists() && f.isFile()) {
      f.delete();
      if (!f.exists()) {
        return true;
      }
    }
    return false;
  }

}







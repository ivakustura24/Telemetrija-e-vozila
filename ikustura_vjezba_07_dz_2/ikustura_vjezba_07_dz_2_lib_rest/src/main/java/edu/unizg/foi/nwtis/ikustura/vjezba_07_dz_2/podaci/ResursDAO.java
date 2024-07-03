package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci;

import java.util.ArrayList;
import java.util.List;

public class ResursDAO {

  public List<PodaciRadara> dohvatiSveRadare(String odgovor) {
    List<PodaciRadara> radari = new ArrayList<>();
    if (odgovor != null) {
      String podaci = odgovor.replace("OK", "").replace("{", "").replace("}", "");
      if (podaci.length() < 6) {
        return null;
      }
      String[] podaciRadara = podaci.split("\\], \\[");
      for (int i = 0; i < podaciRadara.length; i++) {
        podaciRadara[i] = podaciRadara[i].replaceAll("[\\[\\]]", "").trim();
      }
      for (int i = 0; i < podaciRadara.length; i++) {
        String[] radar = podaciRadara[i].split("\\s+");
        int id = Integer.parseInt(radar[0]);
        String adresa = radar[1];
        int vrata = Integer.parseInt(radar[2]);
        double gpsSirina = Double.parseDouble(radar[3]);
        double gpsDuzina = Double.parseDouble(radar[4]);
        int udaljenost = Integer.parseInt(radar[5]);
        PodaciRadara noviRadar = new PodaciRadara(id, adresa, vrata, 0, 0, udaljenost, null, 0,
            null, 0, null, gpsSirina, gpsDuzina);
        radari.add(noviRadar);
      }
      return radari;
    }
    return null;
  }

  public List<PodaciRadara> dohvatiRadar(int id, List<PodaciRadara> radari) {
    List<PodaciRadara> radariSvi = new ArrayList<PodaciRadara>();
    for(PodaciRadara radar : radari) {
      if(radar.id() == id) {
        radariSvi.add(radar);
      }
    }
    return radariSvi;
  }

}

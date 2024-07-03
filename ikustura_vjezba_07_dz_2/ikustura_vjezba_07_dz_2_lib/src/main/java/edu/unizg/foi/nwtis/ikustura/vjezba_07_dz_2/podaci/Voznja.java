package edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci;

public class Voznja {
  private int id;
  private int broj;
  private long vrijeme; 
  private double brzina; 
  private double snaga;
  private double struja;
  private double visina;
  private double gpsBrzina; 
  private int tempVozila;
  private int postotakBaterija;
  private double naponBaterija;
  private int kapacitetBaterija; 
  private int tempBaterija;
  private double preostaloKm;
  private double ukupnoKm;
  private double gpsSirina; 
  private double gpsDuzina;

  public Voznja() {
    super();
  }
  public Voznja(int id, int broj, long vrijeme, double brzina, double snaga,
      double struja, double visina, double gpsBrzina, int tempVozila, int postotakBaterija,
      double naponBaterija, int kapacitetBaterija, int tempBaterija, double preostaloKm,
      double ukupnoKm, double gpsSirina, double gpsDuzina) {
    super();
    this.setId(id);
    this.setBroj(broj);
    this.setVrijeme(vrijeme);
    this.setBrzina(brzina);
    this.setSnaga(snaga);
    this.setStruja(struja);
    this.setVisina(visina);
    this.setGpsBrzina(gpsBrzina);
    this.setTempVozila(tempVozila);
    this.setPostotakBaterija(postotakBaterija);
    this.setNaponBaterija(naponBaterija);
    this.setKapacitetBaterija(kapacitetBaterija);
    this.setTempBaterija(tempBaterija);
    this.setPreostaloKm(preostaloKm);
    this.setUkupnoKm(ukupnoKm);
    this.setGpsSirina(gpsSirina);
    this.setGpsDuzina(gpsDuzina);
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public int getBroj() {
    return broj;
  }
  public void setBroj(int broj) {
    this.broj = broj;
  }
  public long getVrijeme() {
    return vrijeme;
  }
  public void setVrijeme(long vrijeme) {
    this.vrijeme = vrijeme;
  }
  public double getBrzina() {
    return brzina;
  }
  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }
  public double getSnaga() {
    return snaga;
  }
  public void setSnaga(double snaga) {
    this.snaga = snaga;
  }
  public double getStruja() {
    return struja;
  }
  public void setStruja(double struja) {
    this.struja = struja;
  }
  public double getVisina() {
    return visina;
  }
  public void setVisina(double visina) {
    this.visina = visina;
  }
  public double getGpsBrzina() {
    return gpsBrzina;
  }
  public void setGpsBrzina(double gpsBrzina) {
    this.gpsBrzina = gpsBrzina;
  }
  public int getTempVozila() {
    return tempVozila;
  }
  public void setTempVozila(int tempVozila) {
    this.tempVozila = tempVozila;
  }
  public int getPostotakBaterija() {
    return postotakBaterija;
  }
  public void setPostotakBaterija(int postotakBaterija) {
    this.postotakBaterija = postotakBaterija;
  }
  public double getNaponBaterija() {
    return naponBaterija;
  }
  public void setNaponBaterija(double naponBaterija) {
    this.naponBaterija = naponBaterija;
  }
  public int getKapacitetBaterija() {
    return kapacitetBaterija;
  }
  public void setKapacitetBaterija(int kapacitetBaterija) {
    this.kapacitetBaterija = kapacitetBaterija;
  }
  public int getTempBaterija() {
    return tempBaterija;
  }
  public void setTempBaterija(int tempBaterija) {
    this.tempBaterija = tempBaterija;
  }
  public double getPreostaloKm() {
    return preostaloKm;
  }
  public void setPreostaloKm(double preostaloKm) {
    this.preostaloKm = preostaloKm;
  }
  public double getUkupnoKm() {
    return ukupnoKm;
  }
  public void setUkupnoKm(double ukupnoKm) {
    this.ukupnoKm = ukupnoKm;
  }
  public double getGpsSirina() {
    return gpsSirina;
  }
  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }
  public double getGpsDuzina() {
    return gpsDuzina;
  }
  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }
}


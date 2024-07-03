package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.pomocnici;


import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Korisnici;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Korisnici_;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Named
@Stateless
public class KorisniciFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;
  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }
  public void create(Korisnici korisnik) {
    em.persist(korisnik);
  }
  public List<Korisnici> vratiSveKorisnike(){
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    cq.select(cq.from(Korisnici.class));
    return em.createQuery(cq).getResultList();
  }
  public List<Korisnici> vratiKorisnika(String email, String lozinka) {
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    Root<Korisnici> korisnici = cq.from(Korisnici.class);
    cq.where(cb.and(cb.equal(korisnici.get(Korisnici_.email), email),
        (cb.equal(korisnici.get(Korisnici_.email), email))));
    TypedQuery<Korisnici> q = em.createQuery(cq);
    return q.getResultList();
  }
  public boolean dodajKorisnika(Korisnici korisnik) {
    try {
      create(korisnik);
      return true;
    }
    catch(Exception e) {
      return false;
    }
  }
}

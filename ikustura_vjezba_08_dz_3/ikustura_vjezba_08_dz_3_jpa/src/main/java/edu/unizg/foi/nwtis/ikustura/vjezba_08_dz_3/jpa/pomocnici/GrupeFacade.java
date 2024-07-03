package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.pomocnici;

import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Grupe;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

@Named
@Stateless
public class GrupeFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;
  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }
  public void create(Grupe grupa) {
    em.persist(grupa);
  }
  public void remove(Grupe grupe) {
    em.remove(em.merge(grupe));
  }

  public Grupe find(String naziv) {
    return em.find(Grupe.class, naziv);
  }
  public List<Grupe> vratiSveGrupe(){
    CriteriaQuery<Grupe> cq = cb.createQuery(Grupe.class);
    cq.select(cq.from(Grupe.class));
    return em.createQuery(cq).getResultList();
  }
  public boolean dodajNovuGrupu(Grupe grupa) {
    try {
      create(grupa);
      return true;
    }
    catch(Exception e) {
      return false;
    }
  }
}

package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.pomocnici;

import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.DnevnikRada;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

@Named
@Stateless
public class DnevnikRadaFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;
  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }
  public void create(DnevnikRada dnevnikRada) {
    em.persist(dnevnikRada);
  }
  public void remove(DnevnikRada dnevnikRada) {
    em.remove(em.merge(dnevnikRada));
  }

  public DnevnikRada find(Object id) {
    return em.find(DnevnikRada.class, id);
  }
  public List<DnevnikRada> vratiSveZapise(){
    CriteriaQuery<DnevnikRada> cq = cb.createQuery(DnevnikRada.class);
    cq.select(cq.from(DnevnikRada.class));
    return em.createQuery(cq).getResultList();
  }
  public boolean dodajNoviZapis(DnevnikRada dnevnikRada) {
    try {
      create(dnevnikRada);
      return true;
    }
    catch(Exception e) {
      return false;
    }
  }
}

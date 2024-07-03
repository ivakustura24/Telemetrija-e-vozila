package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.pomocnici;

import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Pracenevoznje;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Pracenevoznje_;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
@RequestScoped
public class PraceneVoznjeFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;
  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }
  
  public void create(Pracenevoznje praceneVoznje) {
    em.persist(praceneVoznje);
  }
  public List<Pracenevoznje> vratiSveVoznje(){
    CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
    cq.select(cq.from(Pracenevoznje.class));
    return em.createQuery(cq).getResultList();
  }
  public List<Pracenevoznje> vratiPraceneVoznjeInterval(long odVremena, long doVremena){
    CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
    Root<Pracenevoznje> voznje  = cq.from(Pracenevoznje.class); 
    cq.where(cb.between(voznje.get(Pracenevoznje_.vrijeme), odVremena, doVremena));
    TypedQuery<Pracenevoznje> q = em.createQuery(cq);
    return q.getResultList();
  }
  public List<Pracenevoznje> vratiPraceneVoznjeVozilaInterval(int id, long odVremena, long doVremena) {
    CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
    Root<Pracenevoznje> voznje = cq.from(Pracenevoznje.class);
    cq.where(cb.and(cb.equal(voznje.get(Pracenevoznje_.id), id),
        cb.between(voznje.get(Pracenevoznje_.vrijeme), odVremena, doVremena)));
    TypedQuery<Pracenevoznje> q = em.createQuery(cq);
    return q.getResultList();
  }

  public List<Pracenevoznje> vratiPraceneVoznjeVozila(int id) {
    CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
    Root<Pracenevoznje> voznje = cq.from(Pracenevoznje.class);
    cq.where(cb.equal(voznje.get(Pracenevoznje_.id), id));
    TypedQuery<Pracenevoznje> q = em.createQuery(cq);
    return q.getResultList();
  }
  public boolean dodajPracenuVoznju(Pracenevoznje praceneVoznje) {
    try {
      create(praceneVoznje);
      return true;
    }
    catch(Exception e) {
      return false;
    }
  }
}

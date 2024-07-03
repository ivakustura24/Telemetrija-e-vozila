package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.pomocnici;


import java.util.List;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Voznje;
import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti.Voznje_;
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
public class VoznjeFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;
  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }
  
  public void create(Voznje voznje) {
    em.persist(voznje);
  }
  
  public List<Voznje> vratiSveVoznje(){
    CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
    cq.select(cq.from(Voznje.class));
    return em.createQuery(cq).getResultList();
  }
  public List<Voznje> vratiVoznjeInterval(long odVremena, long doVremena){
    CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
    Root<Voznje> voznje  = cq.from(Voznje.class); 
    cq.where(cb.between(voznje.get(Voznje_.vrijeme), odVremena, doVremena));
    TypedQuery<Voznje> q = em.createQuery(cq);
    return q.getResultList();
  }
  

  public List<Voznje> vratiVoznjeVozilaInterval(int id, long odVremena, long doVremena) {
    CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
    Root<Voznje> voznje = cq.from(Voznje.class);
    cq.where(cb.equal(voznje.get(Voznje_.vozila), id),
        cb.between(voznje.get(Voznje_.vrijeme), odVremena, doVremena));
    TypedQuery<Voznje> q = em.createQuery(cq);
    return q.getResultList();
  }

  public List<Voznje> vratiVoznjeVozila(int id) {
    CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
    Root<Voznje> voznje = cq.from(Voznje.class);
    cq.where(cb.equal(voznje.get(Voznje_.vrijeme), id));
    TypedQuery<Voznje> q = em.createQuery(cq);
    return q.getResultList();
  }
  public boolean dodajVoznju(Voznje voznje) {
    try {
      create(voznje);
      return true;
    }
    catch(Exception e) {
      return false;
    }
  }

}

package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jpa.entiteti;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author NWTiS_1
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Vozila.findAll", query = "SELECT v FROM Vozila v")})
public class Vozila implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer vozilo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    private String proizvodac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String model;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER)
    private List<Kazne> kazneList;
    @JoinColumn(name = "VLASNIK", referencedColumnName = "KORISNIK")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Korisnici vlasnik;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER)
    private List<Voznje> voznjeList;

    public Vozila() {
    }

    public Vozila(Integer vozilo) {
        this.vozilo = vozilo;
    }

    public Vozila(Integer vozilo, String proizvodac, String model) {
        this.vozilo = vozilo;
        this.proizvodac = proizvodac;
        this.model = model;
    }

    public Integer getVozilo() {
        return vozilo;
    }

    public void setVozilo(Integer vozilo) {
        this.vozilo = vozilo;
    }

    public String getProizvodac() {
        return proizvodac;
    }

    public void setProizvodac(String proizvodac) {
        this.proizvodac = proizvodac;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Kazne> getKazneList() {
        return kazneList;
    }

    public void setKazneList(List<Kazne> kazneList) {
        this.kazneList = kazneList;
    }

    public Korisnici getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(Korisnici vlasnik) {
        this.vlasnik = vlasnik;
    }

    public List<Voznje> getVoznjeList() {
        return voznjeList;
    }

    public void setVoznjeList(List<Voznje> voznjeList) {
        this.voznjeList = voznjeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vozilo != null ? vozilo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vozila)) {
            return false;
        }
        Vozila other = (Vozila) object;
        if ((this.vozilo == null && other.vozilo != null) || (this.vozilo != null && !this.vozilo.equals(other.vozilo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unizg.foi.nwtis.dkermek.vjezba_08_dz_3.jpa.entiteti.Vozila[ vozilo=" + vozilo + " ]";
    }
    
}

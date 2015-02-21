/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package just4youjpa.model.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import just4youjpa.listener.EventController;

/**
 *
 * @author Arles Mathieu
 */
@Entity
@Table(name = "Produit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produit.findAll", query = "SELECT p FROM Produit p"),
    @NamedQuery(name = "Produit.findByIdProduit", query = "SELECT p FROM Produit p WHERE p.idProduit = :idProduit"),
    @NamedQuery(name = "Produit.findByLibelle", query = "SELECT p FROM Produit p WHERE p.libelle = :libelle"),
    @NamedQuery(name = "Produit.findByFournisseur", query = "SELECT p FROM Produit p WHERE p.fournisseur = :fournisseur"),
    @NamedQuery(name = "Produit.findByPrix", query = "SELECT p FROM Produit p WHERE p.prix = :prix"),
    @NamedQuery(name = "Produit.findByQtte", query = "SELECT p FROM Produit p WHERE p.qtte = :qtte"),
    @NamedQuery(name = "Produit.findByQtteMax", query = "SELECT p FROM Produit p WHERE p.qtteMax = :qtteMax"),
    @NamedQuery(name = "Produit.findByQtteMin", query = "SELECT p FROM Produit p WHERE p.qtteMin = :qtteMin")})
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProduit")
    private Integer idProduit;
    @Basic(optional = false)
    @Column(name = "libelle")
    private String libelle;
    @Basic(optional = false)
    @Column(name = "fournisseur")
    private String fournisseur;
    @Basic(optional = false)
    @Column(name = "prix")
    private float prix;
    @Basic(optional = false)
    @Column(name = "qtte")
    private int qtte;
    @Basic(optional = false)
    @Column(name = "qtteMax")
    private int qtteMax;
    @Basic(optional = false)
    @Column(name = "qtteMin")
    private int qtteMin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produit")
    private Collection<CommandeFournisseur> commandeFournisseurCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produit")
    private Collection<Commande> commandeCollection;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener p) {
        this.pcs.addPropertyChangeListener(p);
    }

    public void removePropertyChangeListener(PropertyChangeListener p) {
        this.pcs.removePropertyChangeListener(p);
    }

    public Produit() {
        pcs.addPropertyChangeListener(new EventController());

    }

    public Produit(Integer idProduit) {
        this.idProduit = idProduit;
        pcs.addPropertyChangeListener(new EventController());

    }

    public Produit(Integer idProduit, String libelle, String fournisseur, float prix, int qtte, int qtteMax, int qtteMin) {
        this.idProduit = idProduit;
        this.libelle = libelle;
        this.fournisseur = fournisseur;
        this.prix = prix;
        this.qtte = qtte;
        this.qtteMax = qtteMax;
        this.qtteMin = qtteMin;
    }

    public Integer getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Integer idProduit) {
        this.idProduit = idProduit;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getQtte() {
        return qtte;
    }

    public void setQtte(int qtte) {
        this.addPropertyChangeListener(new EventController());
        Integer old_value=this.qtte;
        this.qtte = qtte;
        if (old_value!=null) {
            this.pcs.firePropertyChange("qtte", old_value, this);
        }
    }

    public int getQtteMax() {
        return qtteMax;
    }

    public void setQtteMax(int qtteMax) {
        this.qtteMax = qtteMax;
    }

    public int getQtteMin() {
        return qtteMin;
    }

    public void setQtteMin(int qtteMin) {
        this.qtteMin = qtteMin;
    }

    @XmlTransient
    public Collection<CommandeFournisseur> getCommandeFournisseurCollection() {
        return commandeFournisseurCollection;
    }

    public void setCommandeFournisseurCollection(Collection<CommandeFournisseur> commandeFournisseurCollection) {
        this.commandeFournisseurCollection = commandeFournisseurCollection;
    }

    @XmlTransient
    public Collection<Commande> getCommandeCollection() {
        return commandeCollection;
    }

    public void setCommandeCollection(Collection<Commande> commandeCollection) {
        this.commandeCollection = commandeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProduit != null ? idProduit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produit)) {
            return false;
        }
        Produit other = (Produit) object;
        if ((this.idProduit == null && other.idProduit != null) || (this.idProduit != null && !this.idProduit.equals(other.idProduit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "just4youjpa.model.entities.Produit[ idProduit=" + idProduit + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package just4youjpa.model.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Arles Mathieu
 */
@Entity
@Table(name = "CommandeFournisseur")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommandeFournisseur.findAll", query = "SELECT c FROM CommandeFournisseur c"),
    @NamedQuery(name = "CommandeFournisseur.findByIdCommandeFournisseur", query = "SELECT c FROM CommandeFournisseur c WHERE c.idCommandeFournisseur = :idCommandeFournisseur"),
    @NamedQuery(name = "CommandeFournisseur.findByQtte", query = "SELECT c FROM CommandeFournisseur c WHERE c.qtte = :qtte"),
    @NamedQuery(name = "CommandeFournisseur.findByDateLivraison", query = "SELECT c FROM CommandeFournisseur c WHERE c.dateLivraison = :dateLivraison")})
public class CommandeFournisseur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCommandeFournisseur")
    private Integer idCommandeFournisseur;
    @Basic(optional = false)
    @Column(name = "qtte")
    private int qtte;
    @Column(name = "dateLivraison")
    @Temporal(TemporalType.DATE)
    private Date dateLivraison;
    @JoinColumn(name = "Produit_idProduit", referencedColumnName = "idProduit")
    @ManyToOne(optional = false)
    private Produit produit;

    public CommandeFournisseur() {
    }

    public CommandeFournisseur(Integer idCommandeFournisseur) {
        this.idCommandeFournisseur = idCommandeFournisseur;
    }

    public CommandeFournisseur(Integer idCommandeFournisseur, int qtte) {
        this.idCommandeFournisseur = idCommandeFournisseur;
        this.qtte = qtte;
    }



    public Integer getInteger() {
        return idCommandeFournisseur;
    }

    public void setInteger(Integer idCommandeFournisseur) {
        this.idCommandeFournisseur = idCommandeFournisseur;
    }

    public int getQtte() {
        return qtte;
    }

    public void setQtte(int qtte) {
        this.qtte = qtte;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCommandeFournisseur != null ? idCommandeFournisseur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommandeFournisseur)) {
            return false;
        }
        CommandeFournisseur other = (CommandeFournisseur) object;
        if ((this.idCommandeFournisseur == null && other.idCommandeFournisseur != null) || (this.idCommandeFournisseur != null && !this.idCommandeFournisseur.equals(other.idCommandeFournisseur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "just4youjpa.model.entities.CommandeFournisseur[ idCommandeFournisseur=" + idCommandeFournisseur + " ]";
    }
    
}

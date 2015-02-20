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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
    @NamedQuery(name = "CommandeFournisseur.findByIdCommandeFournisseur", query = "SELECT c FROM CommandeFournisseur c WHERE c.commandeFournisseurPK.idCommandeFournisseur = :idCommandeFournisseur"),
    @NamedQuery(name = "CommandeFournisseur.findByQtte", query = "SELECT c FROM CommandeFournisseur c WHERE c.qtte = :qtte"),
    @NamedQuery(name = "CommandeFournisseur.findByDateLivraison", query = "SELECT c FROM CommandeFournisseur c WHERE c.dateLivraison = :dateLivraison"),
    @NamedQuery(name = "CommandeFournisseur.findByProduitidProduit", query = "SELECT c FROM CommandeFournisseur c WHERE c.commandeFournisseurPK.produitidProduit = :produitidProduit")})
public class CommandeFournisseur implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CommandeFournisseurPK commandeFournisseurPK;
    @Basic(optional = false)
    @Column(name = "qtte")
    private int qtte;
    @Column(name = "dateLivraison")
    @Temporal(TemporalType.DATE)
    private Date dateLivraison;
    @JoinColumn(name = "Produit_idProduit", referencedColumnName = "idProduit", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Produit produit;

    public CommandeFournisseur() {
    }

    public CommandeFournisseur(CommandeFournisseurPK commandeFournisseurPK) {
        this.commandeFournisseurPK = commandeFournisseurPK;
    }

    public CommandeFournisseur(CommandeFournisseurPK commandeFournisseurPK, int qtte) {
        this.commandeFournisseurPK = commandeFournisseurPK;
        this.qtte = qtte;
    }

    public CommandeFournisseur(int idCommandeFournisseur, int produitidProduit) {
        this.commandeFournisseurPK = new CommandeFournisseurPK(idCommandeFournisseur, produitidProduit);
    }

    public CommandeFournisseurPK getCommandeFournisseurPK() {
        return commandeFournisseurPK;
    }

    public void setCommandeFournisseurPK(CommandeFournisseurPK commandeFournisseurPK) {
        this.commandeFournisseurPK = commandeFournisseurPK;
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
        hash += (commandeFournisseurPK != null ? commandeFournisseurPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommandeFournisseur)) {
            return false;
        }
        CommandeFournisseur other = (CommandeFournisseur) object;
        if ((this.commandeFournisseurPK == null && other.commandeFournisseurPK != null) || (this.commandeFournisseurPK != null && !this.commandeFournisseurPK.equals(other.commandeFournisseurPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "just4youjpa.model.entities.CommandeFournisseur[ commandeFournisseurPK=" + commandeFournisseurPK + " ]";
    }
    
}

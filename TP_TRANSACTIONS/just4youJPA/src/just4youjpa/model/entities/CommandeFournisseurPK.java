/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package just4youjpa.model.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Arles Mathieu
 */
@Embeddable
public class CommandeFournisseurPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idCommandeFournisseur")
    private int idCommandeFournisseur;
    @Basic(optional = false)
    @Column(name = "Produit_idProduit")
    private int produitidProduit;

    public CommandeFournisseurPK() {
    }

    public CommandeFournisseurPK(int idCommandeFournisseur, int produitidProduit) {
        this.idCommandeFournisseur = idCommandeFournisseur;
        this.produitidProduit = produitidProduit;
    }

    public int getIdCommandeFournisseur() {
        return idCommandeFournisseur;
    }

    public void setIdCommandeFournisseur(int idCommandeFournisseur) {
        this.idCommandeFournisseur = idCommandeFournisseur;
    }

    public int getProduitidProduit() {
        return produitidProduit;
    }

    public void setProduitidProduit(int produitidProduit) {
        this.produitidProduit = produitidProduit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCommandeFournisseur;
        hash += (int) produitidProduit;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommandeFournisseurPK)) {
            return false;
        }
        CommandeFournisseurPK other = (CommandeFournisseurPK) object;
        if (this.idCommandeFournisseur != other.idCommandeFournisseur) {
            return false;
        }
        if (this.produitidProduit != other.produitidProduit) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "just4youjpa.model.entities.CommandeFournisseurPK[ idCommandeFournisseur=" + idCommandeFournisseur + ", produitidProduit=" + produitidProduit + " ]";
    }
    
}

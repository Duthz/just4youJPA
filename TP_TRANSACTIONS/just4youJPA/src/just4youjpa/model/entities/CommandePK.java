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
public class CommandePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idCommande")
    private int idCommande;
    @Basic(optional = false)
    @Column(name = "Produit_idProduit")
    private int produitidProduit;
    @Basic(optional = false)
    @Column(name = "Client_idClient")
    private int clientidClient;

    public CommandePK() {
    }

    public CommandePK(int idCommande, int produitidProduit, int clientidClient) {
        this.idCommande = idCommande;
        this.produitidProduit = produitidProduit;
        this.clientidClient = clientidClient;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getProduitidProduit() {
        return produitidProduit;
    }

    public void setProduitidProduit(int produitidProduit) {
        this.produitidProduit = produitidProduit;
    }

    public int getClientidClient() {
        return clientidClient;
    }

    public void setClientidClient(int clientidClient) {
        this.clientidClient = clientidClient;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCommande;
        hash += (int) produitidProduit;
        hash += (int) clientidClient;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommandePK)) {
            return false;
        }
        CommandePK other = (CommandePK) object;
        if (this.idCommande != other.idCommande) {
            return false;
        }
        if (this.produitidProduit != other.produitidProduit) {
            return false;
        }
        if (this.clientidClient != other.clientidClient) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "just4youjpa.model.entities.CommandePK[ idCommande=" + idCommande + ", produitidProduit=" + produitidProduit + ", clientidClient=" + clientidClient + " ]";
    }
    
}

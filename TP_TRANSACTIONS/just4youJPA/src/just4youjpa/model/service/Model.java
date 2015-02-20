/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package just4youjpa.model.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import just4youjpa.model.entities.Client;
import just4youjpa.model.entities.Commande;
import just4youjpa.model.entities.CommandeFournisseur;
import just4youjpa.model.entities.Produit;

/**
 *
 * @author Arles Mathieu
 */
public class Model {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("just4youJPAPU");

    public void createProduit(String libelle, String fournisseur, int qtte, int seuilMin, int seuilMax,
            int prix) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Produit p = new Produit();
        p.setCommandeCollection(new ArrayList<Commande>());
        p.setCommandeFournisseurCollection(new ArrayList<CommandeFournisseur>());
        p.setFournisseur(fournisseur);
        p.setLibelle(libelle);
        p.setPrix(prix);
        p.setQtte(qtte);
        p.setQtteMax(qtte);
        p.setQtteMin(qtte);
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }

    public void livrer(int idCommandeF) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        CommandeFournisseur cf = em.find(CommandeFournisseur.class, idCommandeF);
        Produit p = cf.getProduit();
        p.setQtte(p.getQtte() + cf.getQtte()); //on incrémente
        cf.setDateLivraison(new Date()); // On cloture la commande Fournisseur
        em.merge(p);
        em.merge(cf);
        em.getTransaction().commit();
        em.close();
    }
    
    public void creerCommandeClient(String nom, String prenom,
            int qtte, int idProduit) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery("SELECT * FROM Client Where nom='"+nom +"' AND prenom='"+prenom+"'");
        List<Client> clients = q.getResultList();
        Client c;
        if(clients.isEmpty()) {
           c = new Client();
           c.setCommandeCollection(new ArrayList<Commande>());
           c.setNom(nom);
           c.setPrenom(prenom);
           em.persist(c);
        } else
            c = clients.get(0);
        
        Produit p = em.find(Produit.class, idProduit);
        
        Commande commande  = new Commande();
        commande.setClient(c);
        commande.setDate(new Date());
        commande.setMontant(p.getPrix() * qtte);
        commande.setProduit(p);
        em.persist(commande);
        c.getCommandeCollection().add(commande);
        em.merge(commande);
        em.merge(c);
        em.getTransaction().commit();
        em.close();
    }
    
    public void payerCommande(int idCommande, String modePaiement) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Commande commande = em.find(Commande.class, idCommande);
        commande.setTypePaiement(modePaiement);
        em.merge(commande);
        em.getTransaction().commit();
        em.close();
    }

}

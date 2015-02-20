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

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ex-Struts-Spring-JPAPU");

    public void createProduit(Produit produit) {
        if (produit.getCommandeFournisseurCollection() == null) {
            produit.setCommandeFournisseurCollection(new ArrayList<CommandeFournisseur>());
        }
        if (produit.getCommandeCollection() == null) {
            produit.setCommandeCollection(new ArrayList<Commande>());
        }
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Collection<CommandeFournisseur> attachedCommandeFournisseurCollection = new ArrayList<CommandeFournisseur>();
            for (CommandeFournisseur commandeFournisseurCollectionCommandeFournisseurToAttach : produit.getCommandeFournisseurCollection()) {
                commandeFournisseurCollectionCommandeFournisseurToAttach = em.getReference(commandeFournisseurCollectionCommandeFournisseurToAttach.getClass(), commandeFournisseurCollectionCommandeFournisseurToAttach.getCommandeFournisseurPK());
                attachedCommandeFournisseurCollection.add(commandeFournisseurCollectionCommandeFournisseurToAttach);
            }
            produit.setCommandeFournisseurCollection(attachedCommandeFournisseurCollection);
            Collection<Commande> attachedCommandeCollection = new ArrayList<Commande>();
            for (Commande commandeCollectionCommandeToAttach : produit.getCommandeCollection()) {
                commandeCollectionCommandeToAttach = em.getReference(commandeCollectionCommandeToAttach.getClass(), commandeCollectionCommandeToAttach.getCommandePK());
                attachedCommandeCollection.add(commandeCollectionCommandeToAttach);
            }
            produit.setCommandeCollection(attachedCommandeCollection);
            em.persist(produit);
            for (CommandeFournisseur commandeFournisseurCollectionCommandeFournisseur : produit.getCommandeFournisseurCollection()) {
                Produit oldProduitOfCommandeFournisseurCollectionCommandeFournisseur = commandeFournisseurCollectionCommandeFournisseur.getProduit();
                commandeFournisseurCollectionCommandeFournisseur.setProduit(produit);
                commandeFournisseurCollectionCommandeFournisseur = em.merge(commandeFournisseurCollectionCommandeFournisseur);
                if (oldProduitOfCommandeFournisseurCollectionCommandeFournisseur != null) {
                    oldProduitOfCommandeFournisseurCollectionCommandeFournisseur.getCommandeFournisseurCollection().remove(commandeFournisseurCollectionCommandeFournisseur);
                    oldProduitOfCommandeFournisseurCollectionCommandeFournisseur = em.merge(oldProduitOfCommandeFournisseurCollectionCommandeFournisseur);
                }
            }
            for (Commande commandeCollectionCommande : produit.getCommandeCollection()) {
                Produit oldProduitOfCommandeCollectionCommande = commandeCollectionCommande.getProduit();
                commandeCollectionCommande.setProduit(produit);
                commandeCollectionCommande = em.merge(commandeCollectionCommande);
                if (oldProduitOfCommandeCollectionCommande != null) {
                    oldProduitOfCommandeCollectionCommande.getCommandeCollection().remove(commandeCollectionCommande);
                    oldProduitOfCommandeCollectionCommande = em.merge(oldProduitOfCommandeCollectionCommande);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void livrer(int idCommandeF) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        CommandeFournisseur cf = em.find(CommandeFournisseur.class, idCommandeF);
        Produit p = cf.getProduit();
        p.setQtte(p.getQtte() + cf.getQtte()); //on incrémente
        cf.setDateLivraison(new Date()); // On cloture la commande Fournisseur
        em.getTransaction().commit();
        em.close();
    }
    
    public void creerCommandeClient(Integer idClient, String nom, String prenom,
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
        em.getTransaction().commit();
        em.close();
    }
    
    public void payerCommande(int idCommande, String modePaiement) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Commande commande = em.find(Commande.class, idCommande);
        commande.setTypePaiement(modePaiement);
        em.getTransaction().commit();
        em.close();
    }

}

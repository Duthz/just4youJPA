/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package just4youjpa.model.service;

import java.util.ArrayList;
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

    /**
     * Création d'un produit
     *
     * @param libelle
     * @param fournisseur
     * @param qtte
     * @param seuilMin
     * @param seuilMax
     * @param prix
     */
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
        p.setQtteMax(seuilMax);
        p.setQtteMin(seuilMin);
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Livraison d'une commande fournisseur. Si la date de livraison est null,
     * on cloture la commande et incrémente le stock
     *
     * @param idCommandeF
     */
    public void livrer(int idCommandeF) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        CommandeFournisseur cf = em.find(CommandeFournisseur.class, idCommandeF);
        Produit p = cf.getProduit();

        if (cf.getDateLivraison() == null) {
            p.setQtte(p.getQtte() + cf.getQtte()); //on incrémente
            cf.setDateLivraison(new Date()); // On cloture la commande Fournisseur
            em.merge(p);
            em.merge(cf);
            em.getTransaction().commit();
        }
        em.close();
    }

    /**
     * Création d'une commande client.
     *
     * @param nom
     * @param prenom
     * @param qtte
     * @param idProduit
     */
    public void creerCommandeClient(String nom, String prenom,
            int qtte, int idProduit) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Produit p = em.find(Produit.class, idProduit);

        if (p != null && p.getQtte() >= qtte) { //On regarde si le produit existe et s'il y a assez de qtte

            Query q = em.createQuery("FROM Client c Where c.nom='" + nom + "' AND c.prenom='" + prenom + "'");

            List<Client> clients = q.getResultList();
            Client c;
            if (clients.isEmpty()) {
                c = new Client();
                c.setCommandeCollection(new ArrayList<Commande>());
                c.setNom(nom);
                c.setPrenom(prenom);
                c.setIdClient(1);
                em.persist(c);
            } else {
                c = clients.get(0);
            }

            Commande commande = new Commande(); //On créer la commande
            commande.setClient(c);
            commande.setDate(new Date());
            commande.setMontant(p.getPrix() * qtte);
            commande.setProduit(p);
            commande.setQtte(qtte);

            em.persist(commande);
            c.getCommandeCollection().add(commande); // On ajoute la commande au client
            p.setQtte(p.getQtte() - qtte); // On Décremente la qtte du produit

            em.merge(c);
            em.merge(p);
            em.merge(commande);
            em.getTransaction().commit();
            em.close();
        }
    }

    /**
     * Paiement d'une commande
     *
     * @param idCommande
     * @param modePaiement
     */
    public void payerCommande(int idCommande, String modePaiement) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Commande commande = em.find(Commande.class, idCommande);
        commande.setTypePaiement(modePaiement);
        em.merge(commande);
        em.getTransaction().commit();
        em.close();
    }

    public void creerCommandeFournisseur(int idProduit, int newQtte) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Produit p = em.find(Produit.class, idProduit);
        CommandeFournisseur cf = new CommandeFournisseur();
        cf.setProduit(p);
        cf.setQtte(p.getQtteMax() - newQtte);
        em.persist(cf);
        em.getTransaction().commit();
        em.close();
    }

}

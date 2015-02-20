/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package just4youjpa.model.service;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import just4youjpa.model.entities.Client;
import just4youjpa.model.entities.Commande;
import just4youjpa.model.entities.CommandePK;
import just4youjpa.model.entities.Produit;
import just4youjpa.model.service.exceptions.NonexistentEntityException;
import just4youjpa.model.service.exceptions.PreexistingEntityException;

/**
 *
 * @author Arles Mathieu
 */
public class CommandeJpaController implements Serializable {

    public CommandeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Commande commande) throws PreexistingEntityException, Exception {
        if (commande.getCommandePK() == null) {
            commande.setCommandePK(new CommandePK());
        }
        commande.getCommandePK().setProduitidProduit(commande.getProduit().getIdProduit());
        commande.getCommandePK().setClientidClient(commande.getClient().getIdClient());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client client = commande.getClient();
            if (client != null) {
                client = em.getReference(client.getClass(), client.getIdClient());
                commande.setClient(client);
            }
            Produit produit = commande.getProduit();
            if (produit != null) {
                produit = em.getReference(produit.getClass(), produit.getIdProduit());
                commande.setProduit(produit);
            }
            em.persist(commande);
            if (client != null) {
                client.getCommandeCollection().add(commande);
                client = em.merge(client);
            }
            if (produit != null) {
                produit.getCommandeCollection().add(commande);
                produit = em.merge(produit);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCommande(commande.getCommandePK()) != null) {
                throw new PreexistingEntityException("Commande " + commande + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Commande commande) throws NonexistentEntityException, Exception {
        commande.getCommandePK().setProduitidProduit(commande.getProduit().getIdProduit());
        commande.getCommandePK().setClientidClient(commande.getClient().getIdClient());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Commande persistentCommande = em.find(Commande.class, commande.getCommandePK());
            Client clientOld = persistentCommande.getClient();
            Client clientNew = commande.getClient();
            Produit produitOld = persistentCommande.getProduit();
            Produit produitNew = commande.getProduit();
            if (clientNew != null) {
                clientNew = em.getReference(clientNew.getClass(), clientNew.getIdClient());
                commande.setClient(clientNew);
            }
            if (produitNew != null) {
                produitNew = em.getReference(produitNew.getClass(), produitNew.getIdProduit());
                commande.setProduit(produitNew);
            }
            commande = em.merge(commande);
            if (clientOld != null && !clientOld.equals(clientNew)) {
                clientOld.getCommandeCollection().remove(commande);
                clientOld = em.merge(clientOld);
            }
            if (clientNew != null && !clientNew.equals(clientOld)) {
                clientNew.getCommandeCollection().add(commande);
                clientNew = em.merge(clientNew);
            }
            if (produitOld != null && !produitOld.equals(produitNew)) {
                produitOld.getCommandeCollection().remove(commande);
                produitOld = em.merge(produitOld);
            }
            if (produitNew != null && !produitNew.equals(produitOld)) {
                produitNew.getCommandeCollection().add(commande);
                produitNew = em.merge(produitNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CommandePK id = commande.getCommandePK();
                if (findCommande(id) == null) {
                    throw new NonexistentEntityException("The commande with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CommandePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Commande commande;
            try {
                commande = em.getReference(Commande.class, id);
                commande.getCommandePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commande with id " + id + " no longer exists.", enfe);
            }
            Client client = commande.getClient();
            if (client != null) {
                client.getCommandeCollection().remove(commande);
                client = em.merge(client);
            }
            Produit produit = commande.getProduit();
            if (produit != null) {
                produit.getCommandeCollection().remove(commande);
                produit = em.merge(produit);
            }
            em.remove(commande);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Commande> findCommandeEntities() {
        return findCommandeEntities(true, -1, -1);
    }

    public List<Commande> findCommandeEntities(int maxResults, int firstResult) {
        return findCommandeEntities(false, maxResults, firstResult);
    }

    private List<Commande> findCommandeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Commande.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Commande findCommande(CommandePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Commande.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommandeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Commande> rt = cq.from(Commande.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

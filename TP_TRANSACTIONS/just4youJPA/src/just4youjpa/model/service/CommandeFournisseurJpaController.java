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
import just4youjpa.model.entities.CommandeFournisseur;
import just4youjpa.model.entities.CommandeFournisseurPK;
import just4youjpa.model.entities.Produit;
import just4youjpa.model.service.exceptions.NonexistentEntityException;
import just4youjpa.model.service.exceptions.PreexistingEntityException;

/**
 *
 * @author Arles Mathieu
 */
public class CommandeFournisseurJpaController implements Serializable {

    public CommandeFournisseurJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CommandeFournisseur commandeFournisseur) throws PreexistingEntityException, Exception {
        if (commandeFournisseur.getCommandeFournisseurPK() == null) {
            commandeFournisseur.setCommandeFournisseurPK(new CommandeFournisseurPK());
        }
        commandeFournisseur.getCommandeFournisseurPK().setProduitidProduit(commandeFournisseur.getProduit().getIdProduit());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produit produit = commandeFournisseur.getProduit();
            if (produit != null) {
                produit = em.getReference(produit.getClass(), produit.getIdProduit());
                commandeFournisseur.setProduit(produit);
            }
            em.persist(commandeFournisseur);
            if (produit != null) {
                produit.getCommandeFournisseurCollection().add(commandeFournisseur);
                produit = em.merge(produit);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCommandeFournisseur(commandeFournisseur.getCommandeFournisseurPK()) != null) {
                throw new PreexistingEntityException("CommandeFournisseur " + commandeFournisseur + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CommandeFournisseur commandeFournisseur) throws NonexistentEntityException, Exception {
        commandeFournisseur.getCommandeFournisseurPK().setProduitidProduit(commandeFournisseur.getProduit().getIdProduit());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CommandeFournisseur persistentCommandeFournisseur = em.find(CommandeFournisseur.class, commandeFournisseur.getCommandeFournisseurPK());
            Produit produitOld = persistentCommandeFournisseur.getProduit();
            Produit produitNew = commandeFournisseur.getProduit();
            if (produitNew != null) {
                produitNew = em.getReference(produitNew.getClass(), produitNew.getIdProduit());
                commandeFournisseur.setProduit(produitNew);
            }
            commandeFournisseur = em.merge(commandeFournisseur);
            if (produitOld != null && !produitOld.equals(produitNew)) {
                produitOld.getCommandeFournisseurCollection().remove(commandeFournisseur);
                produitOld = em.merge(produitOld);
            }
            if (produitNew != null && !produitNew.equals(produitOld)) {
                produitNew.getCommandeFournisseurCollection().add(commandeFournisseur);
                produitNew = em.merge(produitNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CommandeFournisseurPK id = commandeFournisseur.getCommandeFournisseurPK();
                if (findCommandeFournisseur(id) == null) {
                    throw new NonexistentEntityException("The commandeFournisseur with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CommandeFournisseurPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CommandeFournisseur commandeFournisseur;
            try {
                commandeFournisseur = em.getReference(CommandeFournisseur.class, id);
                commandeFournisseur.getCommandeFournisseurPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commandeFournisseur with id " + id + " no longer exists.", enfe);
            }
            Produit produit = commandeFournisseur.getProduit();
            if (produit != null) {
                produit.getCommandeFournisseurCollection().remove(commandeFournisseur);
                produit = em.merge(produit);
            }
            em.remove(commandeFournisseur);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CommandeFournisseur> findCommandeFournisseurEntities() {
        return findCommandeFournisseurEntities(true, -1, -1);
    }

    public List<CommandeFournisseur> findCommandeFournisseurEntities(int maxResults, int firstResult) {
        return findCommandeFournisseurEntities(false, maxResults, firstResult);
    }

    private List<CommandeFournisseur> findCommandeFournisseurEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CommandeFournisseur.class));
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

    public CommandeFournisseur findCommandeFournisseur(CommandeFournisseurPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CommandeFournisseur.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommandeFournisseurCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CommandeFournisseur> rt = cq.from(CommandeFournisseur.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package just4youjpa.model.service;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import just4youjpa.model.entities.CommandeFournisseur;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import just4youjpa.model.entities.Commande;
import just4youjpa.model.entities.Produit;
import just4youjpa.model.service.exceptions.IllegalOrphanException;
import just4youjpa.model.service.exceptions.NonexistentEntityException;

/**
 *
 * @author Arles Mathieu
 */
public class ProduitJpaController implements Serializable {

    public ProduitJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produit produit) {
        if (produit.getCommandeFournisseurCollection() == null) {
            produit.setCommandeFournisseurCollection(new ArrayList<CommandeFournisseur>());
        }
        if (produit.getCommandeCollection() == null) {
            produit.setCommandeCollection(new ArrayList<Commande>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
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

    public void edit(Produit produit) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produit persistentProduit = em.find(Produit.class, produit.getIdProduit());
            Collection<CommandeFournisseur> commandeFournisseurCollectionOld = persistentProduit.getCommandeFournisseurCollection();
            Collection<CommandeFournisseur> commandeFournisseurCollectionNew = produit.getCommandeFournisseurCollection();
            Collection<Commande> commandeCollectionOld = persistentProduit.getCommandeCollection();
            Collection<Commande> commandeCollectionNew = produit.getCommandeCollection();
            List<String> illegalOrphanMessages = null;
            for (CommandeFournisseur commandeFournisseurCollectionOldCommandeFournisseur : commandeFournisseurCollectionOld) {
                if (!commandeFournisseurCollectionNew.contains(commandeFournisseurCollectionOldCommandeFournisseur)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CommandeFournisseur " + commandeFournisseurCollectionOldCommandeFournisseur + " since its produit field is not nullable.");
                }
            }
            for (Commande commandeCollectionOldCommande : commandeCollectionOld) {
                if (!commandeCollectionNew.contains(commandeCollectionOldCommande)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Commande " + commandeCollectionOldCommande + " since its produit field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<CommandeFournisseur> attachedCommandeFournisseurCollectionNew = new ArrayList<CommandeFournisseur>();
            for (CommandeFournisseur commandeFournisseurCollectionNewCommandeFournisseurToAttach : commandeFournisseurCollectionNew) {
                commandeFournisseurCollectionNewCommandeFournisseurToAttach = em.getReference(commandeFournisseurCollectionNewCommandeFournisseurToAttach.getClass(), commandeFournisseurCollectionNewCommandeFournisseurToAttach.getCommandeFournisseurPK());
                attachedCommandeFournisseurCollectionNew.add(commandeFournisseurCollectionNewCommandeFournisseurToAttach);
            }
            commandeFournisseurCollectionNew = attachedCommandeFournisseurCollectionNew;
            produit.setCommandeFournisseurCollection(commandeFournisseurCollectionNew);
            Collection<Commande> attachedCommandeCollectionNew = new ArrayList<Commande>();
            for (Commande commandeCollectionNewCommandeToAttach : commandeCollectionNew) {
                commandeCollectionNewCommandeToAttach = em.getReference(commandeCollectionNewCommandeToAttach.getClass(), commandeCollectionNewCommandeToAttach.getCommandePK());
                attachedCommandeCollectionNew.add(commandeCollectionNewCommandeToAttach);
            }
            commandeCollectionNew = attachedCommandeCollectionNew;
            produit.setCommandeCollection(commandeCollectionNew);
            produit = em.merge(produit);
            for (CommandeFournisseur commandeFournisseurCollectionNewCommandeFournisseur : commandeFournisseurCollectionNew) {
                if (!commandeFournisseurCollectionOld.contains(commandeFournisseurCollectionNewCommandeFournisseur)) {
                    Produit oldProduitOfCommandeFournisseurCollectionNewCommandeFournisseur = commandeFournisseurCollectionNewCommandeFournisseur.getProduit();
                    commandeFournisseurCollectionNewCommandeFournisseur.setProduit(produit);
                    commandeFournisseurCollectionNewCommandeFournisseur = em.merge(commandeFournisseurCollectionNewCommandeFournisseur);
                    if (oldProduitOfCommandeFournisseurCollectionNewCommandeFournisseur != null && !oldProduitOfCommandeFournisseurCollectionNewCommandeFournisseur.equals(produit)) {
                        oldProduitOfCommandeFournisseurCollectionNewCommandeFournisseur.getCommandeFournisseurCollection().remove(commandeFournisseurCollectionNewCommandeFournisseur);
                        oldProduitOfCommandeFournisseurCollectionNewCommandeFournisseur = em.merge(oldProduitOfCommandeFournisseurCollectionNewCommandeFournisseur);
                    }
                }
            }
            for (Commande commandeCollectionNewCommande : commandeCollectionNew) {
                if (!commandeCollectionOld.contains(commandeCollectionNewCommande)) {
                    Produit oldProduitOfCommandeCollectionNewCommande = commandeCollectionNewCommande.getProduit();
                    commandeCollectionNewCommande.setProduit(produit);
                    commandeCollectionNewCommande = em.merge(commandeCollectionNewCommande);
                    if (oldProduitOfCommandeCollectionNewCommande != null && !oldProduitOfCommandeCollectionNewCommande.equals(produit)) {
                        oldProduitOfCommandeCollectionNewCommande.getCommandeCollection().remove(commandeCollectionNewCommande);
                        oldProduitOfCommandeCollectionNewCommande = em.merge(oldProduitOfCommandeCollectionNewCommande);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produit.getIdProduit();
                if (findProduit(id) == null) {
                    throw new NonexistentEntityException("The produit with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produit produit;
            try {
                produit = em.getReference(Produit.class, id);
                produit.getIdProduit();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produit with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CommandeFournisseur> commandeFournisseurCollectionOrphanCheck = produit.getCommandeFournisseurCollection();
            for (CommandeFournisseur commandeFournisseurCollectionOrphanCheckCommandeFournisseur : commandeFournisseurCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Produit (" + produit + ") cannot be destroyed since the CommandeFournisseur " + commandeFournisseurCollectionOrphanCheckCommandeFournisseur + " in its commandeFournisseurCollection field has a non-nullable produit field.");
            }
            Collection<Commande> commandeCollectionOrphanCheck = produit.getCommandeCollection();
            for (Commande commandeCollectionOrphanCheckCommande : commandeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Produit (" + produit + ") cannot be destroyed since the Commande " + commandeCollectionOrphanCheckCommande + " in its commandeCollection field has a non-nullable produit field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(produit);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produit> findProduitEntities() {
        return findProduitEntities(true, -1, -1);
    }

    public List<Produit> findProduitEntities(int maxResults, int firstResult) {
        return findProduitEntities(false, maxResults, firstResult);
    }

    private List<Produit> findProduitEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produit.class));
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

    public Produit findProduit(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produit.class, id);
        } finally {
            em.close();
        }
    }

    public int getProduitCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produit> rt = cq.from(Produit.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

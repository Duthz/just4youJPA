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
import just4youjpa.model.entities.Commande;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import just4youjpa.model.entities.Client;
import just4youjpa.model.service.exceptions.IllegalOrphanException;
import just4youjpa.model.service.exceptions.NonexistentEntityException;
import just4youjpa.model.service.exceptions.PreexistingEntityException;

/**
 *
 * @author Arles Mathieu
 */
public class ClientJpaController implements Serializable {

    public ClientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Client client) throws PreexistingEntityException, Exception {
        if (client.getCommandeCollection() == null) {
            client.setCommandeCollection(new ArrayList<Commande>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Commande> attachedCommandeCollection = new ArrayList<Commande>();
            for (Commande commandeCollectionCommandeToAttach : client.getCommandeCollection()) {
                commandeCollectionCommandeToAttach = em.getReference(commandeCollectionCommandeToAttach.getClass(), commandeCollectionCommandeToAttach.getCommandePK());
                attachedCommandeCollection.add(commandeCollectionCommandeToAttach);
            }
            client.setCommandeCollection(attachedCommandeCollection);
            em.persist(client);
            for (Commande commandeCollectionCommande : client.getCommandeCollection()) {
                Client oldClientOfCommandeCollectionCommande = commandeCollectionCommande.getClient();
                commandeCollectionCommande.setClient(client);
                commandeCollectionCommande = em.merge(commandeCollectionCommande);
                if (oldClientOfCommandeCollectionCommande != null) {
                    oldClientOfCommandeCollectionCommande.getCommandeCollection().remove(commandeCollectionCommande);
                    oldClientOfCommandeCollectionCommande = em.merge(oldClientOfCommandeCollectionCommande);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClient(client.getIdClient()) != null) {
                throw new PreexistingEntityException("Client " + client + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Client client) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client persistentClient = em.find(Client.class, client.getIdClient());
            Collection<Commande> commandeCollectionOld = persistentClient.getCommandeCollection();
            Collection<Commande> commandeCollectionNew = client.getCommandeCollection();
            List<String> illegalOrphanMessages = null;
            for (Commande commandeCollectionOldCommande : commandeCollectionOld) {
                if (!commandeCollectionNew.contains(commandeCollectionOldCommande)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Commande " + commandeCollectionOldCommande + " since its client field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Commande> attachedCommandeCollectionNew = new ArrayList<Commande>();
            for (Commande commandeCollectionNewCommandeToAttach : commandeCollectionNew) {
                commandeCollectionNewCommandeToAttach = em.getReference(commandeCollectionNewCommandeToAttach.getClass(), commandeCollectionNewCommandeToAttach.getCommandePK());
                attachedCommandeCollectionNew.add(commandeCollectionNewCommandeToAttach);
            }
            commandeCollectionNew = attachedCommandeCollectionNew;
            client.setCommandeCollection(commandeCollectionNew);
            client = em.merge(client);
            for (Commande commandeCollectionNewCommande : commandeCollectionNew) {
                if (!commandeCollectionOld.contains(commandeCollectionNewCommande)) {
                    Client oldClientOfCommandeCollectionNewCommande = commandeCollectionNewCommande.getClient();
                    commandeCollectionNewCommande.setClient(client);
                    commandeCollectionNewCommande = em.merge(commandeCollectionNewCommande);
                    if (oldClientOfCommandeCollectionNewCommande != null && !oldClientOfCommandeCollectionNewCommande.equals(client)) {
                        oldClientOfCommandeCollectionNewCommande.getCommandeCollection().remove(commandeCollectionNewCommande);
                        oldClientOfCommandeCollectionNewCommande = em.merge(oldClientOfCommandeCollectionNewCommande);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = client.getIdClient();
                if (findClient(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
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
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getIdClient();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Commande> commandeCollectionOrphanCheck = client.getCommandeCollection();
            for (Commande commandeCollectionOrphanCheckCommande : commandeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the Commande " + commandeCollectionOrphanCheckCommande + " in its commandeCollection field has a non-nullable client field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(client);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Client.class));
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

    public Client findClient(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Client> rt = cq.from(Client.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

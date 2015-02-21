/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package just4youjpa.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import just4youjpa.model.entities.Produit;
import just4youjpa.model.service.Model;

/**
 *
 * @author Arles Mathieu
 */
public class EventController implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {


        String property = evt.getPropertyName();
        if (property.equals("qtte")) {
            int oldValue = (int) evt.getOldValue();
            Produit p = (Produit) evt.getNewValue();
            System.out.println("Old value: " + oldValue + " NewValue:" + p.getQtte());

            if (p.getQtte() < p.getQtteMin() && oldValue >= p.getQtteMin()) {
                System.out.println("La quantité est en dessous du seuil minimum");
                Model m = new Model();
                m.creerCommandeFournisseur(p.getIdProduit(), p.getQtte());
            }
        } else {
            System.out.println("pas de traitement");
        }
    }

}

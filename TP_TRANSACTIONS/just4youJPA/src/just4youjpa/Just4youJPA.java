/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package just4youjpa;

import just4youjpa.model.service.Model;

/**
 *
 * @author pierregaillard
 */
public class Just4youJPA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Model m = new Model();
        
        m.createProduit("Patate", "Patatier", 5, 2, 10, 3);
        m.creerCommandeClient(1,"Arles", "Mathieu", 4, 1);
    }
    
}

package com.elodie.calculatrice.model;

import com.elodie.calculatrice.oberserver.Observateur;
import com.elodie.calculatrice.vue.Bouton;
import java.util.ArrayList;

public class Calcultatrice {
    private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
    private String userInput = "";

    public void run(){
        //this.cal = Calendar.getInstance();
        this.userInput = "";
        //On avertit les observateurs que l'affichage doit être mis à jour
        this.updateObservateur();

    }

    //Ajoute un observateur à la liste
    public void addObservateur(Observateur obs) {
        this.listObservateur.add(obs);
    }
    //Retire tous les observateurs de la liste
    public void delObservateur() {
        this.listObservateur = new ArrayList<Observateur>();
    }
    //Avertit les observateurs que l'objet observable a changé
    //et invoque la méthode update() de chaque observateur
    public void updateObservateur() {
        for(Observateur obs : this.listObservateur )
            obs.update(this.userInput);
    }
}

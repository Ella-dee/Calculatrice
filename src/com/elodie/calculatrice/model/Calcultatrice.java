package com.elodie.calculatrice.model;

import java.util.ArrayList;
import java.util.Objects;

public class Calcultatrice {
    private double result;
    private double nbr;
    private String btn = "";
    private String output = "";
    //ajout d'un tableau pour stocker les entrées utilisateur
    ArrayList inputs = new ArrayList();
    int [] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    String [] operators = {".", "=", "C", "+", "-", "*", "/"};

    public void run(){

                if (Objects.equals( btn, "C" )){
                    output="";
                    inputs.removeAll(inputs);
                }
                else if(Objects.equals( btn, "=" )){
                    output=Double.toString(result);
                }
                else{

                }
    }
}

package com.elodie.calculatrice.interactions;

import com.elodie.calculatrice.vue.Bouton;
import com.elodie.calculatrice.vue.Fenetre;
import static com.elodie.calculatrice.interactions.Calculs.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

//TODO ajouter un keylistener pour pouvoir utiliser les entrées clavier

/**
 *<b>Chaque bouton lorsqu'il est cliqué déclenche une "écoute"</b>
 *<p>On défini trois grandes actions:
 *<ul>
 *     <li>appuyer sur "C" soit Cancel</li>
 *     <li>appuyer sur "=" soit égal</li>
 *     <li>appuyer sur toute autre touche</li>
 *</ul>
 **/
public class BoutonListener implements ActionListener {

    //Ajout d'un tableau pour stocker les entrées utilisateur
    public static CopyOnWriteArrayList inputs = new CopyOnWriteArrayList();
    public static String clickedOperator;
    public static int clicked = 0;
        /**<p>Le détail des différentes actions:</p>
         * <ul>
         * <li>Lorsqu'on appuie sur "C":
         *     <p>
         *     L'affichage de la Calculs passe à "0";
         *     La liste des entrées utilisateurs se vide.
         *     </p>
         * </li>
         * <li>Lorsqu'on appuie sur "=":
         *     <p>
         *         La fonction equalOp() se déclenche:
         *      <p>L'opération est effectuée et l'écran d'affichage renvoi le résultat de celle-ci
         *         </p>
         *      @see Calculs#calcul()
         *      @see Calculs#equalOp()
         * </li>
         * <li>Lorsqu'on appuie sur toute autre touche de la calcultatrice:
         *     <p>Les entrées utlisateurs sont affichées à l'écran.
         *     <p>Si un opérateur est entré à la suite d'un autre, le dernier le remplace.</p>
         * @see Calculs#operatorCheck()
         *      <p>Lorsqu'une opération est faisable suite au déclenchement d'un opérateur, le résultat de celle-ci s'affiche à l'écran,
         *         la liste des entrées utilisateurs est remplacée par ce résultat.</p>
         *      @see Calculs#calcul()
         *      @see Calculs#plusOp()
         *      @see Calculs#minusOp()
         *      @see Calculs#multiplyOp()
         *      @see Calculs#divideOp()
         *      <p>Après une opération effectuée suite à "=", on vérifie selon la saisie suivante s'il s'agit d'une nouvelle opération,
         *      ou s'il s'agit de la même opération qui continue.</p>
         *      @see Calculs#newOpOrNot()
         * </li>
         * </ul>
         * <p>Pour la cosmétique d'affichage:
         * <ul>
         *
         *     <li>
         *          Si un zéro est en première position, ne pas l'afficher (exemple: 034, doit apparaîre 34)
         *          @see Calculs#firstZero()
         *     </li>
         *     <li>
         *          Si un .0 est affiché lorsqu'il s'agit d'un nombre entier, ne pas l'afficher (exemple: 34.0, doit apparaîre 34)
         *          @see Calculs#doubleEntier(String str)
         *     </li>
         * </ul>
         * @param e bouton cliqué
         */
        public void actionPerformed(ActionEvent e) {
            Bouton bouton = (Bouton) e.getSource();
            String userInput = bouton.getText();
            // si on appuie sur Cancel "C" on vide l'affichage et la liste des entrées utilisateurs
            if (Objects.equals( userInput, "C" )) {
                inputs.clear();
                inputs.add("0");
            }
            // si on appuie sur égal "=" on effectue l'opération affichée
            else if(Objects.equals( userInput, "=" )){
                clicked = 0;
                equalOp();
            }
            else{
                inputs.add( userInput );
                firstZero();
                newOpOrNot();
                if (Arrays.asList( Fenetre.ops ).contains( userInput )) {
                    clickedOperator = userInput;
                    operatorCheck();
                    for (Object o : inputs) {
                        if ((Arrays.asList( Fenetre.ops ).contains( o ))&& inputs.indexOf( o )!= 0 ) {
                            switch (o.toString()) {
                                case "+":
                                    plusOp();
                                    break;
                                case "-":
                                    minusOp();
                                    break;
                                case "*":
                                    multiplyOp();
                                    break;
                                case "/":
                                    divideOp();
                                    break;
                            }
                        }
                    }
                }

            }
            StringBuilder txt = new StringBuilder();
            for (Object input : inputs) {
                txt.append( input );
            }
            Fenetre.screened.setText( txt.toString() );
        }
    }



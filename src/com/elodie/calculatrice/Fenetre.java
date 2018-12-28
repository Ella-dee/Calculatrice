package com.elodie.calculatrice;

import com.elodie.calculatrice.vue.Bouton;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static java.awt.Color.red;

/**
 * <b>Fenetre est la classe pour l'affichage de la calculatrice.</b>
 * <p>
 * Les initialisations d'affichage:
 * <ul>
 * <li>le JFrame</li>
 * <li>le corps principal externe de la calculatrice, puis le corps interne:
 * <ol>
 *     <li>l'écran d'affichage</li>
 *     <li>les chiffres</li>
 *     <li>les opérateurs</li>
 *  </li>
 * </ol></li>
 * </ul>
 * <p>
 *     On lie ensuite tout ces différents morceaux d'affichage densemble, regroupé dans le corps principal de la calculatrice.
 * <p>
 *     On initialise ensuilte l'objet calculatrice depuis lequel seront effectué les calculs
 * <p>
 *     On place un écouteur sur notre objet calculatrice, afin de gérer l'affichage en temps réel.
 * </p>
 * @version 1.0
 * @author elojito
 */

class Fenetre extends JFrame {

    //Les Layouts
    private final GridLayout gridWest = new GridLayout(4, 3, 5, 5); // pour le conteneur numbers
    private final GridLayout gridEast = new GridLayout(5, 1, 5, 5); // pour le conteneur operators

    // l'écran d'affichage
    private final JLabel screened;
    private final String [] ops = {"C", "+", "-", "*", "/"};
    private String clickedOperator;
    //Ajout d'un tableau pour stocker les entrées utilisateur
    private final ArrayList inputs = new ArrayList();

    private Fenetre() {
        //On initialise la JFrame
        this.setTitle("Calculatrice");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setSize(300, 320);
        // Les Panels
        // conteneur Fenetre
        JPanel wrapper = new JPanel();
        wrapper.setBackground( Color.white );

        //Init la calculatrice
        Dimension calcSize = new Dimension(270,270);
        // conteneur calculatrice
        JPanel container = new JPanel();
        container.setPreferredSize(calcSize);
        container.setMaximumSize(calcSize);
        container.setPreferredSize(calcSize);
        container.setMaximumSize(calcSize);
        container.setBackground(Color.white);
        container.setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
        Border border = container.getBorder();
        Border margin = new EmptyBorder(10,10,10,10);
        container.setBorder(new CompoundBorder(border, margin));
        container.setLayout(new BorderLayout());

        //Ecran d'affichage
        screened = new JLabel( "0" );
        Font police = new Font("Arial", Font.PLAIN, 16);
        screened.setPreferredSize(new Dimension(240, 25) );
        screened.setFont(police);
        screened.setForeground( Color.black );
        // conteneur écran d'affichage
        JPanel screen = new JPanel();
        screen.setBackground(Color.white );
        screened.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        Border screenBorder = screened.getBorder();
        Border screenMargin = new EmptyBorder(10,10,10,10);
        screened.setBorder( new CompoundBorder( screenBorder, screenMargin) );
        screened.setHorizontalAlignment(JLabel.RIGHT);
        screen.add( screened );
        container.add( screen, BorderLayout.NORTH);

        //Affichage des chiffres
        // conteneur des boutons chiffres
        JPanel numbers = new JPanel();
        numbers.setLayout(gridWest);
        //Les boutons numbers
        String[] nbr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "="};
        for(String s : nbr){
            Bouton bouton = new Bouton(s);
            bouton.addActionListener(new BoutonListener());
            numbers.add(bouton);
        }
        container.add( numbers );

        //Affichage des opérateurs sur la droite
        // conteneur des opérateurs contenu dans operators
        JPanel operatorsInside = new JPanel();
        operatorsInside.setLayout(gridEast);
        for(String s : ops){
            Bouton bouton = new Bouton(s);
            bouton.addActionListener(new BoutonListener());
            Dimension opSize = new Dimension( 50, 35 );
            bouton.setPreferredSize( opSize );
            operatorsInside.add(bouton);
            if(Objects.equals( s, "C" )){
                bouton.setForeground( red );
            }
        }
        // conteneur colonne des opérateurs
        JPanel operators = new JPanel();
        operators.add( operatorsInside );

        container.add( operators, BorderLayout.EAST);
        wrapper.add( container );
        this.setContentPane( wrapper );
        this.setVisible(true);

    }

    class BoutonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            Bouton bouton = (Bouton) e.getSource();
            String userInput = bouton.getText();
            if (userInput == "C") {
                screened.setText( "0" );
                inputs.clear();
            }
            else if(userInput == "="){
                equalOp();
            }
            else{
                inputs.add( userInput );
                if (Arrays.asList( ops ).contains( userInput )) {
                    clickedOperator = userInput;
                    /**
                     * lire le tableau inputs
                     * trouver l'opérateur et switcher dessus
                     **/
                    for (Object o : inputs) {
                        if (Arrays.asList( ops ).contains( o )) {
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

            screened.setText( txt.toString() );
        }
    }

    private static String myTrimString(String str, String delimiter){
        StringBuilder sb = new StringBuilder();
        sb.append(str.replaceAll("[\\[\\],]", "").replace( " ", "" )).append(delimiter);
        str.trim();
        return sb.substring( 0, sb.length());
    }
    private void equalOp() {
        String lastTrim = myTrimString( inputs.toString(), "" );
        String[] findLastOperator = lastTrim.split( "[0-9.]" );
        String lastOperator ="";
        for(String s : findLastOperator){
            if(Arrays.asList( ops ).contains( s )){
                lastOperator = s;
                System.out.println( s );
            }
        }
        String [] nbrACalculer = lastTrim.split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0], "");
        String secondString = myTrimString(nbrACalculer[1], "");
        double firstNbr = Double.parseDouble( firstString );
        double secondNbr = Double.parseDouble( secondString );
        double res = 0;
        if (Arrays.asList( ops ).contains( lastOperator )) {
            switch (lastOperator) {
                case "+":
                    res = firstNbr + secondNbr;
                    break;
                case "-":
                    res = firstNbr - secondNbr;
                    break;
                case "*":
                    res = firstNbr * secondNbr;
                    break;
                case "/":
                    res = firstNbr / secondNbr;
                    break;
            }
            String resString = Double.toString(res);
            inputs.clear();
            inputs.add(resString);
        }
    }
    private void plusOp(){
        String [] nbrACalculer = inputs.toString().split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0], "");
        String secondString = myTrimString(nbrACalculer[1], "");
        if(secondString.length()>=1){
            double firstNbr = Double.parseDouble( firstString );
            double secondNbr = Double.parseDouble( secondString );
            double res = firstNbr+secondNbr;
            String resString = Double.toString(res);
            inputs.clear();
            inputs.add(resString);
            inputs.add( clickedOperator );

        }
    }
    private void minusOp(){
        String [] nbrACalculer = inputs.toString().split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0], "");
        String secondString = myTrimString(nbrACalculer[1], "");
        if(secondString.length()>=1){
            double firstNbr = Double.parseDouble( firstString );
            double secondNbr = Double.parseDouble( secondString );
            double res = firstNbr-secondNbr;
            String resString = Double.toString(res);
            inputs.clear();
            inputs.add(resString);
            inputs.add( clickedOperator );
        }
    }
    private void multiplyOp(){
        String [] nbrACalculer = inputs.toString().split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0], "");
        String secondString = myTrimString(nbrACalculer[1], "");
        if(secondString.length()>=1){
            double firstNbr = Double.parseDouble( firstString );
            double secondNbr = Double.parseDouble( secondString );
            double res = firstNbr*secondNbr;
            String resString = Double.toString(res);
            inputs.clear();
            inputs.add(resString);
            inputs.add( clickedOperator );
        }
    }    private void divideOp(){
        String [] nbrACalculer = inputs.toString().split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0], "");
        String secondString = myTrimString(nbrACalculer[1], "");
        if(secondString.length()>=1){
            double firstNbr = Double.parseDouble( firstString );
            double secondNbr = Double.parseDouble( secondString );
            double res = firstNbr/secondNbr;
            String resString = Double.toString(res);
            System.out.println( resString );
            inputs.clear();
            inputs.add(resString);
            inputs.add( clickedOperator );
        }
    }

    public static void main(String[] args) {
        Fenetre fen = new Fenetre();
    }
}

package com.elodie.calculatrice;

import com.elodie.calculatrice.model.Calcultatrice;
import com.elodie.calculatrice.vue.Bouton;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;
import static java.awt.Color.red;
import static java.lang.Double.parseDouble;
import static java.util.Date.parse;

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

public class Fenetre extends JFrame {
    private Calcultatrice calculatrice;

    // Les Panels
    private JPanel wrapper = new JPanel();           // conteneur Fenetre
    private JPanel container = new JPanel();         // conteneur calculatrice
    private JPanel screen = new JPanel();            // conteneur écran d'affichage
    private JPanel operators = new JPanel();         // conteneur colonne des opérateurs
    private JPanel operatorsInside = new JPanel();   // conteneur des opérateurs contenu dans operators
    private JPanel numbers = new JPanel();           // conteneur des boutons chiffres

    //Les Layouts
    GridLayout gridWest = new GridLayout(4, 3, 5, 5); // pour le conteneur numbers
    GridLayout gridEast = new GridLayout(5, 1, 5, 5); // pour le conteneur operators

    // l'écran d'affichage
    private JLabel screened;
    //Les boutons numbers
    private String [] nbr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "="};
    private String [] ops = {"C", "+", "-", "*", "/"};
    private boolean operatorIsClicked;
    private String clickedOperator;
    //Ajout d'un tableau pour stocker les entrées utilisateur
    public ArrayList inputs = new ArrayList();

    public Fenetre() {
        //On initialise la JFrame
        this.setTitle("Calculatrice");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setSize(300, 320);
        wrapper.setBackground( Color.white );

        //Init la calculatrice
        Dimension calcSize = new Dimension(270,270);
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
        screen.setBackground(Color.white );
        screened.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        Border screenBorder = screened.getBorder();
        Border screenMargin = new EmptyBorder(10,10,10,10);
        screened.setBorder( new CompoundBorder( screenBorder, screenMargin) );
        screened.setHorizontalAlignment(JLabel.RIGHT);
        screen.add( screened );
        container.add(screen, BorderLayout.NORTH);

        //Affichage des chiffres
        numbers.setLayout(gridWest);
        for(String s : nbr){
            Bouton bouton = new Bouton(s);
            bouton.addActionListener(new BoutonListener());
            numbers.add(bouton);
        }
        container.add(numbers);

        //Affichage des opérateurs sur la droite
        operatorsInside.setLayout(gridEast);
        for(String s : ops){
            Bouton bouton = new Bouton(s);
            bouton.addActionListener(new BoutonListener());
            Dimension opSize = new Dimension( 50, 35 );
            bouton.setPreferredSize( opSize );
            operatorsInside.add(bouton);
            if(s == "C"){
                bouton.setForeground( red );
            }
        }
        operators.add(operatorsInside);

        container.add(operators, BorderLayout.EAST);
        wrapper.add( container );
        this.setContentPane(wrapper);
        this.setVisible(true);

        //On initialise la calculatrice
        this.calculatrice = new Calcultatrice();
        this.calculatrice.run();
    }

    class BoutonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            Bouton bouton = (Bouton) e.getSource();
            String userInput = bouton.getText();
            switch (userInput) {
                case "C":
                    screened.setText( "0" );
                    inputs.removeAll( inputs );
                    break;
                case "=":
                    String result = "";
                    screened.setText( result );
                    break;
                    default:
                        inputs.add( userInput );
                        if (Arrays.asList( ops ).contains( userInput )) {
                            operatorIsClicked = true;
                            clickedOperator = userInput;
                            /**
                             * lire le tableau inputs
                             * trouver l'opérateur et switcher dessus
                             */
                            Iterator iterator = inputs.iterator();
                            while (iterator.hasNext()) {
                                Object o = iterator.next();
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

//            }
//                if (userInput == "C") {
//                    screened.setText( "0" );
//                    inputs.removeAll( inputs );
//                } else if (bouton.getText() == "=") {
//                    String result = "";
//                    screened.setText( result );
//                } else if (userInput != "=") {
//                    inputs.add( userInput );
//                    if (Arrays.asList( ops ).contains( userInput )) {
//                        operatorIsClicked = true;
//                        clickedOperator = userInput;
//                        /**
//                         * lire le tableau inputs
//                         * trouver l'opérateur et switcher dessus
//                         */
//                        Iterator iterator = inputs.iterator();
//                        while (iterator.hasNext()) {
//                            Object o = iterator.next();
//                            if (Arrays.asList( ops ).contains( o )) {
//                                switch (o.toString()) {
//                                    case "+":
//                                        plusOp();
//                                        break;
//                                    case "-":
//                                        minusOp();
//                                        break;
//                                    case "*":
//                                        multiplyOp();
//                                        break;
//                                    case "/":
//                                        divideOp();
//                                        break;
//                                    case "C":
//                                        screened.setText( "" );
//                                        inputs.clear();
//                                        break;
//                                }
//                            }
//                        }
//                    }
//
//                }

            String txt = "";
            for (int i = 0; i < inputs.size(); i++) {
                txt += inputs.get( i );
            }

            screened.setText( txt );
        }
        }



    public static String myTrimString(String str, String delimiter){
        StringBuilder sb = new StringBuilder();
        sb.append(str.replaceAll("[\\[\\],]", "").replace( " ", "" )).append(delimiter);
        str.trim();
        return sb.substring( 0, sb.length());
    }
    public  void plusOp(){
        String [] nbrACalculer = inputs.toString().split("[+]");
        String firstString = myTrimString(nbrACalculer[0], "");
        String secondString = myTrimString(nbrACalculer[1], "");
        if(secondString.length()>=1){
            System.out.println( nbrACalculer );
            System.out.println( firstString );
            System.out.println( secondString );
            double firstNbr = Double.parseDouble( firstString );
            System.out.println( firstNbr );
            System.out.println( "vous avez choisi l'addition:" );
            System.out.println( firstString +" + "+secondString);
            double secondNbr = Double.parseDouble( secondString );
            double res = firstNbr+secondNbr;
            String resString = Double.toString(res);
            System.out.println( resString );
            inputs.clear();
            inputs.add(resString);
            inputs.add( clickedOperator );
            System.out.println( inputs.toString() );
        }
    }
    public  void minusOp(){
        String [] nbrACalculer = inputs.toString().split("[-]");
        String firstString = myTrimString(nbrACalculer[0], "");
        String secondString = myTrimString(nbrACalculer[1], "");


        if(secondString.length()>=1){

            System.out.println( nbrACalculer );
            System.out.println( firstString );
            System.out.println( secondString );

            double firstNbr = Double.parseDouble( firstString );

            System.out.println( firstNbr );
            System.out.println( "vous avez choisi la soustraction" );
            System.out.println( firstString +" - "+secondString);
            double secondNbr = Double.parseDouble( secondString );
            double res = firstNbr-secondNbr;
            String resString = Double.toString(res);
            System.out.println( resString );
            inputs.clear();
            inputs.add(resString);
            inputs.add( clickedOperator );
            System.out.println( inputs.toString() );
        }
    }
    public  void multiplyOp(){
        String [] nbrACalculer = inputs.toString().split("[*]");
        String firstString = myTrimString(nbrACalculer[0], "");
        String secondString = myTrimString(nbrACalculer[1], "");


        if(secondString.length()>=1){

            System.out.println( nbrACalculer );
            System.out.println( firstString );
            System.out.println( secondString );

            double firstNbr = Double.parseDouble( firstString );

            System.out.println( firstNbr );
            System.out.println( "vous avez choisi la multiplication" );
            System.out.println( firstString +" * "+secondString);
            double secondNbr = Double.parseDouble( secondString );
            double res = firstNbr*secondNbr;
            String resString = Double.toString(res);
            System.out.println( resString );
            inputs.clear();
            inputs.add(resString);
            inputs.add( clickedOperator );
            System.out.println( inputs.toString() );
        }
    }    public  void divideOp(){
        String [] nbrACalculer = inputs.toString().split("[/]");
        String firstString = myTrimString(nbrACalculer[0], "");
        String secondString = myTrimString(nbrACalculer[1], "");


        if(secondString.length()>=1){

            System.out.println( nbrACalculer );
            System.out.println( firstString );
            System.out.println( secondString );

            double firstNbr = Double.parseDouble( firstString );

            System.out.println( firstNbr );
            System.out.println( "vous avez choisi la division: " );
            System.out.println( firstString +" / "+secondString);
            double secondNbr = Double.parseDouble( secondString );
            double res = firstNbr/secondNbr;
            String resString = Double.toString(res);
            System.out.println( resString );
            inputs.clear();
            inputs.add(resString);
            inputs.add( clickedOperator );
            System.out.println( inputs.toString() );
        }
    }

    public static void main(String[] args) {
        Fenetre fen = new Fenetre();
    }
}

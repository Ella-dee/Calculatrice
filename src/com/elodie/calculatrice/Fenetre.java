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
 *     On lit ensuite tout ces différents morceaux d'affichage densemble, regroupé dans le corps principal de la calculatrice.
 * <p>
 *     On effectue les calculs selon les différents boutons activés de la calculatrice.
 * </p>
 * @version 1.2
 * @version 1.0
 * @author elojito
 */

class Fenetre extends JFrame {
    /**
     *Fenetre possède les attributs:
     *écran d'affichage
     *liste d'opérateurs
     *liste de nombres, virgule ou signe égal
     *le nom du bouton activé par l'utilisateur
     * une liste contenant les entrées utilisateurs
     */
    private final JLabel screened;
    private final String [] ops = {"C", "+", "-", "*", "/"};
    private final String[] nbr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "="};
    private String clickedOperator;
    //Ajout d'un tableau pour stocker les entrées utilisateur
    private final ArrayList inputs = new ArrayList();
    /**
     * On forme l'affichage de la calculatrice:
     * On initialise la fenêtre, le panneau d'affichage, les divers composants de la calculatrice
     * On met en place l'affichage avec les différents layouts
     */
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
        //Les Layouts
        // pour le conteneur numbers
        GridLayout gridWest = new GridLayout( 4, 3, 5, 5 );
        numbers.setLayout( gridWest );
        //Les boutons numbers
        for(String s : nbr){
            Bouton bouton = new Bouton(s);
            bouton.addActionListener(new BoutonListener());
            numbers.add(bouton);
        }
        container.add( numbers );

        //Affichage des opérateurs sur la droite
        // conteneur des opérateurs contenu dans operators
        JPanel operatorsInside = new JPanel();
        // pour le conteneur operators
        GridLayout gridEast = new GridLayout( 5, 1, 5, 5 );
        operatorsInside.setLayout( gridEast );
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

    /**
     *<b>Chaque bouton lorsqu'il est cliqué déclenche une "écoute"</b>
     *<p>On défini trois grandes actions:
     *<ul>
     *     <li>appuyer sur "C" soit Cancel</li>
     *     <li>appuyer sur "=" soit égal</li>
     *     <li>appuyer sur toute autre touche</li>
     *</ul>
    **/
    class BoutonListener implements ActionListener {

        /**<p>Le détail des différentes actions:</p>
         * <ul>
         * <li>Lorsqu'on appuie sur "C":
         *     <p>
         *     L'affichage de la calculatrice passe à "0";
         *     La liste des entrées utilisateurs se vide.
         *     </p>
         * </li>
         * <li>Lorsqu'on appuie sur "=":
         *     <p>
         *         La fonction equalOp() se déclenche:
         *         L'opération est effectuée et l'écran d'affichage renvoi le résultat de celle-ci
         *         </p>
         *@see Fenetre#equalOp()
         * </li>
         * <li>Lorsqu'on appuie sur toute autre touche de la calcultatrice:
         *     <p>
         *         Les entrées utlisateurs sont affichées à l'écran.
         *         Lorsqu'une opération est faisable suite au déclenchement d'un opérateur, le résultat de celle-ci s'affiche à l'écran,
         *         la liste des entrées utilisateurs est remplacée par ce résultat.
         *     </p>
         * @see Fenetre#clickedOperator
         * @see Fenetre#plusOp()
         * @see Fenetre#minusOp()
         * @see Fenetre#multiplyOp()
         * @see Fenetre#divideOp()
         * </li>
         * </ul>
         * @param e bouton cliqué
         */
        public void actionPerformed(ActionEvent e) {
            Bouton bouton = (Bouton) e.getSource();
            String userInput = bouton.getText();
            if (Objects.equals( userInput, "C" )) {
                inputs.clear();
                inputs.add("0");
            }
            else if(Objects.equals( userInput, "=" )){
                equalOp();
            }
            else{
                inputs.add( userInput );
                if (Arrays.asList( ops ).contains( userInput )) {
                    clickedOperator = userInput;
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

    /**
     * Permet de transformer la liste des entrées utilisateurs en chaîne de caractères,
     * et d'enlever la mise en forme "liste"
     * @param str chaîne de caractères donnée
     * @return la chaine de caractères donnée reformatée sans espace et sans caractère spéciaux hormis l'opérateur
     */
    private static String myTrimString(String str){
        StringBuilder sb = new StringBuilder();
        sb.append(str.replaceAll("[\\[\\],]", "").replace( " ", "" ));
        str.trim();
        return sb.substring( 0, sb.length());
    }

    /**
     * Opération suite à l'enclenchement du bouton "=" égal
     * On récupère la liste des entrées utilisateurs
     * On cherche quel est l'opérateur utilisé dans l'opération:
     * <ul>
     *     <li>Avec la chaîne de caractères récupérée, on utilise la fonction split() en spécifiant qu'on ne cherche ni un chiffre ni un point</li>
     *     <li>On cherche si cette chaîne de caractères récupérée correspond à une chaîne de caractères contenue dans le tableau des opérateurs</li>
     *     <li>on assigne ce retour à une chaîne de caractère "lastOperator" qui contient maintenant l'opérateur</li>
     *     <li>Nous reprenons notre chaîne de caractères des entrées utilisateurs initiale:
     *     <ul>
     *         <li>on la divise en deux en utilisant l'opérateur comme délimiteur</li>
     *         <li>on stocke ces deux parties à deux différentes chaînes de caractères</li>
     *         <li>on parse ces chaines en "double"</li>
     *         <li>on effectue l'opération selon l'opérateur défini</li>
     *         <li>le résultat de celle-ci s'affiche à l'écran, la liste des entrées utilisateurs est remplacée par ce résultat.</li>
     *     </ul>
     *     </li>
     * </ul>
     */
    private void equalOp() {
        String lastTrim = myTrimString( inputs.toString());
        String[] findLastOperator = lastTrim.split( "[0-9.]" );
        String lastOperator ="";
        for(String s : findLastOperator){
            if(Arrays.asList( ops ).contains( s )){
                lastOperator = s;
                System.out.println( s );
            }
        }
        String [] nbrACalculer = lastTrim.split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0]);
        String secondString = myTrimString(nbrACalculer[1]);
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

    /**
     * Opération suite à l'enclenchement du bouton "+" plus
     * On récupère la liste des entrées utilisateurs qu'on divise en deux en utilisant un opérateur comme délimiteur
     *     <ul>
     *         <li>on stocke ces deux parties à deux différentes chaînes de caractères</li>
     *         <li>on parse ces chaines en "double"</li>
     *         <li>on effectue l'addition</li>
     *         <li>le résultat de celle-ci s'affiche à l'écran, la liste des entrées utilisateurs est remplacée par ce résultat.</li>
     *         <li>on ajoute à la liste des entrées utilisateurs le dernier opérateur saisi</li>
     *     </ul>
     */
    private void plusOp(){
        String [] nbrACalculer = inputs.toString().split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0]);
        String secondString = myTrimString(nbrACalculer[1]);
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

    /**
     * Opération suite à l'enclenchement du bouton "-" moins
     * On récupère la liste des entrées utilisateurs qu'on divise en deux en utilisant un opérateur comme délimiteur
     *     <ul>
     *         <li>on stocke ces deux parties à deux différentes chaînes de caractères</li>
     *         <li>on parse ces chaines en "double"</li>
     *         <li>on effectue la souscription</li>
     *         <li>le résultat de celle-ci s'affiche à l'écran, la liste des entrées utilisateurs est remplacée par ce résultat.</li>
     *         <li>on ajoute à la liste des entrées utilisateurs le dernier opérateur saisi</li>
     *     </ul>
     */
    private void minusOp(){
        String [] nbrACalculer = inputs.toString().split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0]);
        String secondString = myTrimString(nbrACalculer[1]);
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

    /**
     * Opération suite à l'enclenchement du bouton "*" multiplier
     * On récupère la liste des entrées utilisateurs qu'on divise en deux en utilisant un opérateur comme délimiteur
     *     <ul>
     *         <li>on stocke ces deux parties à deux différentes chaînes de caractères</li>
     *         <li>on parse ces chaines en "double"</li>
     *         <li>on effectue la multiplication</li>
     *         <li>le résultat de celle-ci s'affiche à l'écran, la liste des entrées utilisateurs est remplacée par ce résultat.</li>
     *         <li>on ajoute à la liste des entrées utilisateurs le dernier opérateur saisi</li>
     *     </ul>
     */
    private void multiplyOp(){
        String [] nbrACalculer = inputs.toString().split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0]);
        String secondString = myTrimString(nbrACalculer[1]);
        if(secondString.length()>=1){
            double firstNbr = Double.parseDouble( firstString );
            double secondNbr = Double.parseDouble( secondString );
            double res = firstNbr*secondNbr;
            String resString = Double.toString(res);
            inputs.clear();
            inputs.add(resString);
            inputs.add( clickedOperator );
        }
    }
    /**
     * Opération suite à l'enclenchement du bouton "/" diviser
     * On récupère la liste des entrées utilisateurs qu'on divise en deux en utilisant un opérateur comme délimiteur
     *     <ul>
     *         <li>on stocke ces deux parties à deux différentes chaînes de caractères</li>
     *         <li>on parse ces chaines en "double"</li>
     *         <li>on effectue la division</li>
     *         <li>le résultat de celle-ci s'affiche à l'écran, la liste des entrées utilisateurs est remplacée par ce résultat.</li>
     *         <li>on ajoute à la liste des entrées utilisateurs le dernier opérateur saisi</li>
     *     </ul>
     */
    private void divideOp(){
        String [] nbrACalculer = inputs.toString().split("[+]|-|[*]|/");
        String firstString = myTrimString(nbrACalculer[0]);
        String secondString = myTrimString(nbrACalculer[1]);
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

package com.elodie.calculatrice;

import com.elodie.calculatrice.vue.Bouton;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

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
 * </ol>
 *  </li>
 * </ul>
 * <p>
 *     On lit ensuite tout ces différents morceaux d'affichage densemble, regroupé dans le corps principal de la calculatrice.
 * <p>
 *     On effectue les calculs selon les différents boutons activés de la calculatrice.
 * </p>
 * @version 2.0
 * @version 1.0
 * @author elojito
 */

public class Fenetre extends JFrame {
    /**
     *<b>Fenetre possède les attributs:</b>
     * <ul>
     *<li>écran d'affichage</li>
     *<li>liste d'opérateurs</li>
     *<li>liste de nombres, virgule ou signe égal</li>
     *<li>le nom du bouton activé par l'utilisateur</li>
     * <li>une liste contenant les entrées utilisateurs</li>
     * <li>un chiffre représentant le nombre de fois qu'un opérateur est cliqué à la suite</li>
     * </ul>
     */
    private final JLabel screened;
    private final String [] ops = {"C", "+", "-", "*", "/"};
    private final String[] nbr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "="};
    private String clickedOperator;
    //Ajout d'un tableau pour stocker les entrées utilisateur
    private  CopyOnWriteArrayList inputs = new CopyOnWriteArrayList();
    private  int clicked = 0;
    /**
     * <p>On forme l'affichage de la calculatrice:
     * <ul>
     *     <li>On initialise la fenêtre</li>
     *     <li>On initialise le panneau d'affichage</li>
     *     <li>On initialise les divers composants de la calculatrice</li>
     *     <li>On met en place l'affichage avec les différents layouts</li>
     *</ul>
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
         *      <p>L'opération est effectuée et l'écran d'affichage renvoi le résultat de celle-ci
         *         </p>
         *      @see Fenetre#calcul()
         *      @see Fenetre#equalOp()
         * </li>
         * <li>Lorsqu'on appuie sur toute autre touche de la calcultatrice:
         *     <p>Les entrées utlisateurs sont affichées à l'écran.
         *     <p>Si un opérateur est entré à la suite d'un autre, le dernier le remplace.</p>
         * @see Fenetre#operatorCheck()
         *      <p>Lorsqu'une opération est faisable suite au déclenchement d'un opérateur, le résultat de celle-ci s'affiche à l'écran,
         *         la liste des entrées utilisateurs est remplacée par ce résultat.</p>
         *      @see Fenetre#calcul()
         *      @see Fenetre#plusOp()
         *      @see Fenetre#minusOp()
         *      @see Fenetre#multiplyOp()
         *      @see Fenetre#divideOp()
         *      <p>Après une opération effectuée suite à "=", on vérifie selon la saisie suivante s'il s'agit d'une nouvelle opération,
         *      ou s'il s'agit de la même opération qui continue.</p>
         *      @see Fenetre#newOpOrNot()
         * </li>
         * </ul>
         * <p>Pour la cosmétique d'affichage:
         * <ul>
         *
         *     <li>
         *          Si un zéro est en première position, ne pas l'afficher (exemple: 034, doit apparaîre 34)
         *          @see Fenetre#firstZero()
         *     </li>
         *     <li>
         *          Si un .0 est affiché lorsqu'il s'agit d'un nombre entier, ne pas l'afficher (exemple: 34.0, doit apparaîre 34)
         *          @see Fenetre#doubleEntier(String str)
         *     </li>
         * </ul></p>
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
                if (Arrays.asList( ops ).contains( userInput )) {
                    clickedOperator = userInput;
                    operatorCheck();
                    for (Object o : inputs) {
                        if ((Arrays.asList( ops ).contains( o ))&& inputs.indexOf( o )!= 0 ) {
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
     * <b>Méthode de formatage du texte</b>
     * <p>Permet de transformer la liste des entrées utilisateurs en chaîne de caractères,
     * et d'enlever la mise en forme "liste"</p>
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
     * <b>Méthode cosmétique d'affichage</b>
     * <p>Si un zéro est en première position, ne pas l'afficher ( 034 = 34)</p>
     */
    public void firstZero(){
        StringBuilder trimmed = new StringBuilder();
        trimmed.append(myTrimString(inputs.toString()));
        if(trimmed.charAt( 0 ) == '0'){
            trimmed = trimmed.deleteCharAt( 0 );
            inputs.clear();
            inputs.add( trimmed );
        }
    }

    /**
     * <b>Méthode cosmétique d'affichage</b>
     * <p>Si un .0 est affiché lorsqu'il s'agit d'un nombre entier, le supprimer (34.0 = 34)</p>
     * @param str le résultat d'une opération parsé en String
     * @return le double vérifié, coupé ou non de sa virgule selon le cas, sous forme de chaine de carcatères
     */
    public String doubleEntier(String str){
        String trimmed = myTrimString(str);
        String [] nbrAVerifier = trimmed.split("[.]");
        String beforeDot = myTrimString( nbrAVerifier[0]);
        String afterDot = myTrimString( nbrAVerifier[1]);
        double afterDotDouble = Double.parseDouble( afterDot );
        if(afterDotDouble == 0.0){
            str = beforeDot;
        }
        return str;
    }

    /**
     * <b>Vérifier si le premier nombre apparant esr un négatif.</b>
     * <p>Si c'est le cas il ne faut pas prendre le "-" du négatif comme un "-" opérateur,
     * et ne pas splitter la chaîne de caractère</p>
     * @param str chaîne de caractère donnée à vérifier
     * @param minus chaine servant de vérificateur, ici sera le signe "-"
     * @return true si le nombre est négatif, false s'il es positif
     */
    public static boolean negativeNbrCheck(String str, String minus){
        boolean negative;
        if(str.indexOf(minus) == 0){
            negative = true;
        }
        else negative = false;
        return negative;
    }

    /**
     * <b>Méthode vérifie qu'il n'y a pas double saisie d'opérateur.</b>
     * <p>Si on clique sur un opérateur à la suite d'un autre,
     * le dernier saisi est pris en compte.</p>
     */
    public void operatorCheck() {
        String trimmed = myTrimString( inputs.toString() ); //On récupère les entrées utilisateurs sous forme de chaîne de caractère
        String[] findOperator = trimmed.split( "[0-9.]" ); //Sur la chaîne initiale, on enlève les chiffres et le point situés avant le premier opérateur trouvé
        int findOperatorLenght = findOperator.length; //On récupère sa longueur
        String lastOperator = findOperator[findOperatorLenght-1]; //length-1 est égal au dernier caractère soit l'opérateur recherché

        if (lastOperator.length() > 1) {  //Si length>1, il y a plusieurs opérateurs de saisis à la suite.
            int sLenght = lastOperator.length();
            String lastOp = lastOperator.substring( sLenght - 1 ); //On trouve le dernier opérateur saisi
            clickedOperator = lastOp; //On le défini comme nouvel opérateur à prendre en compte
            double firstNumber = findFirstNumber();
            inputs.clear();
            inputs.add(doubleEntier(String.valueOf( firstNumber)));
            inputs.add( clickedOperator );
        }
    }

    /**
     * <b>Méthode pour effectuer les différentes opérations</b>
     * <p>On récupère la liste des entrées utilisateurs
     * <p>On cherche quel est l'opérateur utilisé dans l'opération:
     * <ul>
     *     <li>On cherche le premier nombre à calculer</li>
     *      @see Fenetre#findFirstNumber()
     *     <li>On cherche le deuxième nombre à calculer</li>
     *      @see Fenetre#findSecondNumber()
     *     <li>On cherche l'opérateur à prendre en compte pour effectuer l'opération</li>
     *     <li>on assigne ce retour à une chaîne de caractère "lastOperator" qui contient maintenant l'opérateur</li>
     *     <li>on effectue l'opération selon l'opérateur défini</li>
     *     <li>le résultat de celle-ci s'affiche à l'écran, la liste des entrées utilisateurs est remplacée par ce résultat.</li>
     * </ul>
     */
    private void calcul(){
        String trimmed = myTrimString( inputs.toString());
        Double firstNbr = findFirstNumber();
        Double secondNbr = findSecondNumber();
        double result = 0;
        String[] findOperator = trimmed.split( "[0-9.]" );
        String operator = "";
        for(int i =0;i<findOperator.length;i++){
            if ((Arrays.asList( ops ).contains( findOperator[i] ))&& i>0){
                operator = findOperator[i];
                break;
            }
        }
        switch (operator) {
            case "+":
                result = firstNbr + secondNbr;
                break;
            case "-":
                result = firstNbr - secondNbr;
                break;
            case "*":
                result = firstNbr * secondNbr;
                break;
            case "/":
                if(secondNbr == 0){
                    secondNbr = 1.0;
                }
                result = firstNbr / secondNbr;
                break;
        }
        String str = String.valueOf( result );
        inputs.clear();
        inputs.add(doubleEntier(str));
    }

    /**
     * <b>Opération suite à l'enclenchement du bouton "=" égal</b>
     * <p>On défini l'attribut clicked comme étant cliqué: la fonction newOpOrNot en sera affectée;</p>
     * @see Fenetre#newOpOrNot()
     * @see Fenetre#calcul()
     */
    private void equalOp() {
        clicked = 1;
        calcul();
    }

    /**
     * <b>Méthode déclenchée si une opération "=" a été effectuée</b>
     * <p>Vérifie si le bouton déclenché suivant est un chiffre.
     * <p>Si c'est un chiffre, on commence alors une nouvelle opération:
     * L'écran d'affichage se met à jour et affiche le nouveau chiffre à calculer.</p>
     */
    public void newOpOrNot() {
        if (clicked == 1) {
            String trimmed = myTrimString( inputs.toString());
            int trimmedL = trimmed.length();
            char carAt = trimmed.charAt(trimmedL-1);
            for (int i = 0; i<nbr.length;i++) {
                if (nbr[i].equals( String.valueOf( carAt ))) {//Si c'est un chiffre, on commence alors une nouvelle opération:
                    inputs.clear();
                    inputs.add(carAt);
                    clicked = 0;
                }
            }
        }
        clicked = 0;
    }

    /**
     * <b>Méthode pour trouver le premier nombre à calculer</b>
     * <p>On récupère sous chaîne de caractère la liste "inputs" contenant les entrées utilisateurs.</p>
     * <p>On stocke ces données dans une chaîne de caractères tableau, qu'on divise avec un opérateur pour séparateur.</p>
     * <p>La première partie de ce tableau (avant le séparateur), est donc notre premier nombre.</p>
     * <p>On vérifie si le nombre est négatif ou positif.</p>
     * @see Fenetre#negativeNbrCheck(String, String)
     * @return le premier nombre à calculer sous forme de double
     */
    private Double findFirstNumber(){
        String trimmed = myTrimString( inputs.toString());
        String [] nbrACalculer = trimmed.split("[+]|-|[*]|/");
        String firstString = "";
        if(negativeNbrCheck( trimmed, "-" )== false) {
            firstString = nbrACalculer[0];
        }
        else if(negativeNbrCheck( trimmed, "-")== true ) {
            String negString = nbrACalculer[1];
            String minusS = "-";
            firstString = minusS.concat( negString );
        }
        return Double.parseDouble(firstString);
    }

    /**
     * <b>Méthode pour trouver le deuxième nombre à calculer</b>
     * <p>On récupère sous chaîne de caractère la liste "inputs" contenant les entrées utilisateurs.</p>
     * <p>On stocke ces données dans une chaîne de caractères tableau, qu'on divise avec un opérateur pour séparateur.</p>
     * <p>La deuxième partie de ce tableau (après le séparateur), est donc notre deuxième nombre.</p>
     * <p>On vérifie si le nombre est négatif ou positif.</p>
     * @see Fenetre#negativeNbrCheck(String, String)
     * @return le deuxième nombre à calculer sous forme de double
     */
    private Double findSecondNumber(){
        String trimmed = myTrimString(inputs.toString());
        String [] nbrACalculer = trimmed.split("[+]|-|[*]|/");
        String secondString = "";
        if(negativeNbrCheck( trimmed, "-" )== false) {
            secondString = nbrACalculer[1];
        }
        else if(negativeNbrCheck( trimmed, "-")==true ) {
            secondString = nbrACalculer[2];
        }
        return Double.parseDouble(secondString);
    }
    /**
     * <b>Opération suite à l'enclenchement du bouton "+" plus</b>
     * @see Fenetre#newOpOrNot()
     * @see Fenetre#calcul()
     * @see Fenetre#negativeNbrCheck(String, String)
     */
    private void plusOp() {
        String trimmed = myTrimString( inputs.toString() );
        String[] nbrACalculer = trimmed.split( "[+]|-|[*]|/" );
        if (negativeNbrCheck( trimmed, "-" ) == false) {
            if (nbrACalculer.length > 1) {
                calcul();
                inputs.add( clickedOperator );
            }
        } else if (negativeNbrCheck( trimmed, "-" ) == true) {
            if (nbrACalculer.length > 2) {
                calcul();
                inputs.add( clickedOperator );
            }
        }
    }

    /**
     * <b>Opération suite à l'enclenchement du bouton "-" moins</b>
     * @see Fenetre#newOpOrNot()
     * @see Fenetre#calcul()
     * @see Fenetre#negativeNbrCheck(String, String)
     */
    private void minusOp() {
        String trimmed = myTrimString( inputs.toString() );
        String[] nbrACalculer = trimmed.split( "[+]|-|[*]|/" );
        if (negativeNbrCheck( trimmed, "-" ) == false) {
            if (nbrACalculer.length > 1) {
                calcul();
                inputs.add( clickedOperator );
            }
        } else if (negativeNbrCheck( trimmed, "-" ) == true) {
            if (nbrACalculer.length > 2) {
                calcul();
                inputs.add( clickedOperator );
            }
        }
    }

    /**
     * <b>Opération suite à l'enclenchement du bouton "*" multiplier</b>
     * @see Fenetre#newOpOrNot()
     * @see Fenetre#calcul()
     * @see Fenetre#negativeNbrCheck(String, String)
     */
    private void multiplyOp() {
        String trimmed = myTrimString( inputs.toString() );
        String[] nbrACalculer = trimmed.split( "[+]|-|[*]|/" );
        if (negativeNbrCheck( trimmed, "-" ) == false) {
            if (nbrACalculer.length > 1) {
                calcul();
                inputs.add( clickedOperator );
            }
        } else if (negativeNbrCheck( trimmed, "-" ) == true) {
            if (nbrACalculer.length > 2) {
                calcul();
                inputs.add( clickedOperator );
            }
        }
    }
    /**
     * <b>Opération suite à l'enclenchement du bouton "/" diviser</b>
     * @see Fenetre#newOpOrNot()
     * @see Fenetre#calcul()
     * @see Fenetre#negativeNbrCheck(String, String)
     */
    private void divideOp(){
        String trimmed = myTrimString( inputs.toString() );
        String[] nbrACalculer = trimmed.split( "[+]|-|[*]|/" );
        if (negativeNbrCheck( trimmed, "-" ) == false) {
            if (nbrACalculer.length > 1) {
                calcul();
                inputs.add( clickedOperator );
            }
        } else if (negativeNbrCheck( trimmed, "-" ) == true) {
            if (nbrACalculer.length > 2) {
                calcul();
                inputs.add( clickedOperator );
            }
        }
    }


    public static void main(String[] args) {
        Fenetre fen = new Fenetre();
    }
}

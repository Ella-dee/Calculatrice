package com.elodie.calculatrice.interactions;

import com.elodie.calculatrice.vue.Fenetre;

import java.util.Arrays;

import static com.elodie.calculatrice.interactions.BoutonListener.*;
import static com.elodie.calculatrice.vue.Fenetre.nbr;

public class Calculatrice {

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
    public static void firstZero(){
        StringBuilder trimmed = new StringBuilder();
        trimmed.append(myTrimString( inputs.toString()));
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
    public static String doubleEntier(String str){
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
    public static void operatorCheck() {
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
     *      @see Calculatrice#findFirstNumber()
     *     <li>On cherche le deuxième nombre à calculer</li>
     *      @see Calculatrice#findSecondNumber()
     *     <li>On cherche l'opérateur à prendre en compte pour effectuer l'opération</li>
     *     <li>on assigne ce retour à une chaîne de caractère "lastOperator" qui contient maintenant l'opérateur</li>
     *     <li>on effectue l'opération selon l'opérateur défini</li>
     *     <li>le résultat de celle-ci s'affiche à l'écran, la liste des entrées utilisateurs est remplacée par ce résultat.</li>
     * </ul>
     */
    public static void calcul(){
        String trimmed = myTrimString( inputs.toString());
        Double firstNbr = findFirstNumber();
        Double secondNbr = findSecondNumber();
        double result = 0;
        String[] findOperator = trimmed.split( "[0-9.]" );
        String operator = "";
        for(int i =0;i<findOperator.length;i++){
            if ((Arrays.asList( Fenetre.ops ).contains( findOperator[i] ))&& i>0){
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
     * @see Calculatrice#newOpOrNot()
     * @see Calculatrice#calcul()
     */
    public static void equalOp() {
        clicked = 1;
        calcul();
    }

    /**
     * <b>Méthode déclenchée si une opération "=" a été effectuée</b>
     * <p>Vérifie si le bouton déclenché suivant est un chiffre.
     * <p>Si c'est un chiffre, on commence alors une nouvelle opération:
     * L'écran d'affichage se met à jour et affiche le nouveau chiffre à calculer.</p>
     */
    public static void newOpOrNot() {
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
     * @see Calculatrice#negativeNbrCheck(String, String)
     * @return le premier nombre à calculer sous forme de double
     */
    public static Double findFirstNumber(){
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
     * @see Calculatrice#negativeNbrCheck(String, String)
     * @return le deuxième nombre à calculer sous forme de double
     */
    public static Double findSecondNumber(){
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
     * @see Calculatrice#newOpOrNot()
     * @see Calculatrice#calcul()
     * @see Calculatrice#negativeNbrCheck(String, String)
     */
    public static void plusOp() {
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
     * @see Calculatrice#newOpOrNot()
     * @see Calculatrice#calcul()
     * @see Calculatrice#negativeNbrCheck(String, String)
     */
    public static void minusOp() {
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
     * @see Calculatrice#newOpOrNot()
     * @see Calculatrice#calcul()
     * @see Calculatrice#negativeNbrCheck(String, String)
     */
    public static void multiplyOp() {
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
     * @see Calculatrice#newOpOrNot()
     * @see Calculatrice#calcul()
     * @see Calculatrice#negativeNbrCheck(String, String)
     */
    public static void divideOp(){
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


}

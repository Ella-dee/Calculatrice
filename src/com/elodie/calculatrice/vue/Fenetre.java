package com.elodie.calculatrice.vue;
import com.elodie.calculatrice.interactions.BoutonListener;
import com.elodie.calculatrice.interactions.Calculatrice;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
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
 *     On effectue les interactions selon les différents boutons activés de la calculatrice.
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
    public static JLabel screened;
    public static final String [] ops = {"C", "+", "-", "*", "/"};
    public static final String[] nbr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "="};

    /**
     * <p>On forme l'affichage de la calculatrice:
     * <ul>
     *     <li>On initialise la fenêtre</li>
     *     <li>On initialise le panneau d'affichage</li>
     *     <li>On initialise les divers composants de la calculatrice</li>
     *     <li>On met en place l'affichage avec les différents layouts</li>
     *</ul>
     */
    public Fenetre() {
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
}

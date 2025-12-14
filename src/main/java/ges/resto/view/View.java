package ges.resto.view;

import java.util.Scanner;

import ges.resto.entity.ComplementType;
import ges.resto.entity.Etat;
import ges.resto.service.Test;


public class View {
    
    protected static String saisieChaine(String message) {

        String mot;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print(message);
            mot = scanner.nextLine();
        } while (!mot.matches("[a-zA-Z ]+"));  

        return mot.trim();  
    }

    protected static double saisieDouble(String message) {
        
        String mot;
        double reel;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print(message);
            mot = scanner.nextLine();
        } while (!Test.isDouble(mot));
        reel = Double.parseDouble(mot);
        return reel;
    }

    protected static int saisieEntier(String message) {
        
        String mot;
        int entier;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print(message);
            mot = scanner.nextLine();
        } while (!Test.isNumeric(mot));
        entier = Integer.parseInt(mot);
        return entier;
    }

    protected static Etat saisieEtat() {
        Scanner sc = new Scanner(System.in);
        String choix;

        while (true) {
            System.out.println("Choisissez l'état :");
            System.out.println("1 - Disponible");
            System.out.println("2 - Archived");
            System.out.print("Votre choix : ");

            choix = sc.nextLine().trim();

            switch (choix) {
                case "1":
                    return Etat.Disponible;
                case "2":
                    return Etat.Archived;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.\n");
            }
        }
    }

    protected static ComplementType saisieType() {
        Scanner sc = new Scanner(System.in);
        String choix;

        while (true) {
            System.out.println("Choisissez le type de complément :");
            System.out.println("1 - FRITE");
            System.out.println("2 - BOISSON");
            System.out.print("Votre choix : ");

            choix = sc.nextLine().trim();

            switch (choix) {
                case "1":
                    return ComplementType.Frites;
                case "2":
                    return ComplementType.Boisson;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.\n");
            }
        }
    }

    protected static Etat saisieEtatOuVide(Etat etatActuel) {
        Scanner sc = new Scanner(System.in);
        String choix;

        while (true) {
            System.out.println("Choisissez l'état :");
            System.out.println("1 - Disponible");
            System.out.println("2 - Archived");
            System.out.println("Entrée - Conserver (" + etatActuel + ")");
            System.out.print("Votre choix : ");

            choix = sc.nextLine().trim();

            if (choix.isEmpty()) {
                return etatActuel;
            }

            switch (choix) {
                case "1":
                    return Etat.Disponible;
                case "2":
                    return Etat.Archived;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.\n");
            }
        }
    }

    protected static String saisieChaineOuVide(String message) {
        Scanner scanner = new Scanner(System.in);
        String saisie;

        while (true) {
            System.out.print(message);
            saisie = scanner.nextLine().trim();

            if (saisie.isEmpty()) {
                return "";
            }

            if (saisie.matches("[a-zA-Z ]+")) {
                return saisie;
            }

            System.out.println("Saisie invalide (lettres et espaces uniquement).");
        }
    }

    protected static Double saisieDoubleOuVide(String message) {
        Scanner scanner = new Scanner(System.in);
        String saisie;

        while (true) {
            System.out.print(message);
            saisie = scanner.nextLine().trim();

            if (saisie.isEmpty()) {
                return null;
            }

            if (Test.isDouble(saisie)) {
                return Double.parseDouble(saisie);
            }

            System.out.println("Saisie invalide (entrez un nombre ou vide pour conserver).");
        }
    }


    protected static ComplementType saisieTypeOuVide(ComplementType typeActuel) {
        Scanner sc = new Scanner(System.in);
        String choix;

        while (true) {
            System.out.println("Choisissez le type de complément :");
            System.out.println("1 - FRITE");
            System.out.println("2 - BOISSON");
            System.out.println("Entrée - Conserver (" + typeActuel + ")");
            System.out.print("Votre choix : ");

            choix = sc.nextLine().trim();

            if (choix.isEmpty()) {
                return typeActuel;
            }

            switch (choix) {
                case "1":
                    return ComplementType.Frites;
                case "2":
                    return ComplementType.Boisson;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.\n");
            }
        }
    }

    
}
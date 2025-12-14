package ges.resto.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import ges.resto.entity.Complement;
import ges.resto.entity.ComplementType;
import ges.resto.entity.Etat;
import ges.resto.service.ComplementService;
import ges.resto.service.Sys;

public class ComplementView extends View {

    private static ComplementView instance = null;

    public static ComplementView getInstance(ComplementService complementService) {
        if (instance == null) {
            instance = new ComplementView(complementService);
        }
        return instance;
    }

    private ComplementService complementService;

    private ComplementView(ComplementService complementService) {
        this.complementService = complementService;
    }

    public void showMenu() {
        int choix;
        Scanner sc = new Scanner(System.in);

        do {
            while(true) {
                Sys.cls();
                System.out.println("\n===== COMPLEMENT VIEW =====\n");
                System.out.println("1 - Ajouter un complément");
                System.out.println("2 - Lister les compléments");
                System.out.println("3 - Rechercher par ID");
                System.out.println("4 - Modifier un complément");
                System.out.println("5 - Supprimer un complément");
                System.out.println("6 - Archiver un complément");
                System.out.println("0 - Quitter");
                System.out.print("\nVotre choix : ");

                try {
                    choix = Integer.parseInt(sc.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.print("\nVeuillez saisir un entier !!!\n");
                    Sys.pause();
                }
            }

            switch (choix) {
                case 1 -> ajouterComplement();
                case 2 -> listerComplements();
                case 3 -> rechercherComplement();
                case 4 -> modifierComplement();
                case 5 -> supprimerComplement();
                case 6 -> archiverComplement();
            }

        } while (choix != 0);
    }

    private void ajouterComplement() {
        System.out.println("\n--- AJOUT COMPLEMENT ---\n");

        Complement c = new Complement();

        c.setNom(Sys.capitalize(saisieChaine("Nom : ")));
        c.setPrix(saisieDouble("Prix : "));
        c.setComplementType(saisieType());
        c.setEtat(Etat.Disponible);

        int result = complementService.addComplement(c);
        System.out.println(result > 0 ? "Complément ajouté !" : "Erreur d'ajout.");

        Sys.pause();
    }

    private void listerComplements() {
        System.out.println("\n--- LISTE DES COMPLEMENTS ---");

        List<Complement> complements = complementService.findAllComplement();
        if (complements.isEmpty()) {
            System.out.println("Aucun complément trouvé.");
            Sys.pause();
            return;
        }

        for (Complement c : complements) {
            System.out.println(c);
        }

        Sys.pause();
    }

    private void rechercherComplement() {
        System.out.println("\n--- RECHERCHE COMPLEMENT ---\n");

        int id = saisieEntier("ID du complément : ");

        Optional<Complement> opt = complementService.findById(id);

        if (opt.isPresent()) {
            Complement c = opt.get();
            System.out.println("Trouvé : \n" + c);
        } else {
            System.out.println("Aucun complément avec cet ID.");
        }

        Sys.pause();
    }

    private void supprimerComplement() {
        System.out.println("\n--- SUPPRESSION COMPLEMENT ---\n");

        int id = saisieEntier("ID du complément à supprimer : ");

        Optional<Complement> opt = complementService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Aucun complément avec cet ID.");
            Sys.pause();
            return;
        }

        int result = complementService.deleteComplement(id);

        if (result > 0) {
            System.out.println("Complément supprimé !");
        } else {
            System.out.println("Erreur lors de la suppression.");
        }

        Sys.pause();
    }

    private void archiverComplement() {
        System.out.println("\n--- ARCHIVAGE COMPLÉMENT ---\n");

        int id = saisieEntier("ID du complément à archiver : ");

        Optional<Complement> opt = complementService.findById(id);

        if (opt.isPresent()) {
            Complement c = opt.get();

            if (c.getEtat() == Etat.Archived) {
                System.out.println("Ce complément est déjà archivé.");
                Sys.pause();
                return;
            }

            c.setEtat(Etat.Archived);

            int result = complementService.updateComplement(c);

            if (result > 0) {
                System.out.println("Complément archivé !");
            } else {
                System.out.println("Erreur : impossible d’archiver ce complément.");
            }
        } else {
            System.out.println("Aucun complément avec cet ID.");
        }

        Sys.pause();
    }


    public void modifierComplement() {
        int id = saisieEntier("ID du complément à modifier : ");

        Optional<Complement> opt = complementService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Complément introuvable.");
            Sys.pause();
            return;
        }

        Complement c = opt.get();
        System.out.println("=== Modification du complément " + c.getNom() + " ===");
        System.out.println("(Appuyez sur Entrée pour conserver la valeur actuelle)\n");

        String nom = saisieChaineOuVide("Nom (" + c.getNom() + ") : ");
        if (!nom.isBlank()) {
            c.setNom(Sys.capitalize(nom));
        }

        Double prixInput = saisieDoubleOuVide("Prix (" + c.getPrix() + ") : ");
        if (prixInput != null) {
            c.setPrix(prixInput);
        }

        ComplementType type = saisieTypeOuVide(c.getComplementType());
        c.setComplementType(type);

        complementService.updateComplement(c);

        System.out.println("Complément modifié avec succès !");
        Sys.pause();
    }
}

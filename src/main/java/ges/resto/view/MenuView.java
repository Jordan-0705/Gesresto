package ges.resto.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import ges.resto.entity.Menu;
import ges.resto.entity.Burger;
import ges.resto.entity.Complement;
import ges.resto.entity.ComplementType;
import ges.resto.entity.Etat;
import ges.resto.service.BurgerService;
import ges.resto.service.ComplementService;
import ges.resto.service.MenuService;
import ges.resto.service.Sys;

public class MenuView extends View {

    private static MenuView instance = null;

    public static MenuView getInstance(BurgerService burgerService, ComplementService complementService, MenuService menuService) {
        if (instance == null) {
            instance = new MenuView(burgerService, complementService, menuService);
        }
        return instance;
}

    private MenuService menuService;
    private ComplementService complementService;
    private BurgerService burgerService;

    private MenuView(MenuService menuService) {
        this.menuService = menuService;
    }

    private MenuView(BurgerService burgerService, ComplementService complementService, MenuService menuService) {
        this.burgerService = burgerService;
        this.complementService = complementService;
        this.menuService = menuService;
    }

    public void showMenu() {
        int choix;
        Scanner sc = new Scanner(System.in);

        do {
            while(true) {
                Sys.cls();
                System.out.println("\n===== MENU VIEW =====\n");
                System.out.println("1 - Ajouter un menu");
                System.out.println("2 - Lister les menus");
                System.out.println("3 - Rechercher par ID");
                System.out.println("4 - Modifier un menu");
                System.out.println("5 - Supprimer un menu");
                System.out.println("6 - Archiver un menu");
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
                case 1 -> ajouterMenu();
                case 2 -> listerMenus();
                case 3 -> rechercherMenu();
                case 4 -> modifierMenu();
                case 5 -> supprimerMenu();
                case 6 -> archiverMenu();
            }

        } while (choix != 0);
    }

    private void ajouterMenu() {
        System.out.println("\n--- AJOUT MENU ---\n");

        Menu m = new Menu();

        m.setNom(Sys.capitalize(saisieChaine("Nom du menu : ")));

        m.setBurger(selectionnerBurger());

        m.setFrites(selectionnerComplement(ComplementType.Frites));
        m.setBoisson(selectionnerComplement(ComplementType.Boisson));

        m.setPrix(calculerPrix(m));

        m.setEtat(saisieEtat());

        int result = menuService.addMenu(m);
        System.out.println(result > 0 ? "Menu ajouté !" : "Erreur d'ajout.");

        Sys.pause();
    }

    private Burger selectionnerBurger() {
        Burger burger = null;;
        do {
            int burgerId = saisieEntier("ID du burger : ");
            burger = burgerService.findById(burgerId).orElse(null);
            if (burger == null) {
                System.out.println("Burger introuvable. Veuillez saisir un ID valide.");
            }
        } while (burger == null);
        return burger;
    }

    private Complement selectionnerComplement(ComplementType type) {
        Complement complement = null;
        do {
            int compId = saisieEntier("ID de " + (type.equals(ComplementType.Frites) ? "la frite" : "la boisson") + " : ");
            complement = complementService.findById(compId).orElse(null);
            if (complement == null || !complement.getComplementType().equals(type)) {
                System.out.println(type + " introuvable. Veuillez saisir un ID valide.");
                complement = null;
            }
        } while (complement == null);
        return complement;
    }

    private double calculerPrix(Menu m) {
        return m.getBurger().getPrix() + m.getFrites().getPrix() + m.getBoisson().getPrix();
    }

    private void listerMenus() {
        System.out.println("\n--- LISTE DES MENUS ---\n");

        List<Menu> menus = menuService.findAllMenu();
        if (menus.isEmpty()) {
            System.out.println("Aucun menu trouvé.");
            Sys.pause();
            return;
        }

        for (Menu m : menus) {
            System.out.println(m);
        }

        Sys.pause();
    }

    private void rechercherMenu() {
        System.out.println("\n--- RECHERCHE MENU ---\n");

        int id = saisieEntier("ID du menu : ");

        Optional<Menu> opt = menuService.findById(id);

        if (opt.isPresent()) {
            Menu m = opt.get();
            System.out.println("Trouvé : \n" + m);
        } else {
            System.out.println("Aucun menu avec cet ID.");
        }

        Sys.pause();
    }

    private void supprimerMenu() {
        System.out.println("\n--- SUPPRESSION MENU ---\n");

        int id = saisieEntier("ID du menu à supprimer : ");

        Optional<Menu> opt = menuService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Aucun menu avec cet ID.");
            Sys.pause();
            return;
        }

        int result = menuService.deleteMenu(id);

        if (result > 0) {
            System.out.println("Menu supprimé !");
        } else {
            System.out.println("Erreur lors de la suppression.");
        }

        Sys.pause();
    }

    private void archiverMenu() {
        System.out.println("\n--- ARCHIVAGE MENU ---\n");

        int id = saisieEntier("ID du menu à archiver : ");

        Optional<Menu> opt = menuService.findById(id);

        if (opt.isPresent()) {
            Menu m = opt.get();

            if (m.getEtat() == Etat.Archived) {
                System.out.println("Ce menu est déjà archivé.");
                Sys.pause();
                return;
            }

            m.setEtat(Etat.Archived);
            int result = menuService.updateMenu(m);

            if (result > 0) {
                System.out.println("Menu archivé !");
            } else {
                System.out.println("Erreur lors de l'archivage.");
            }
        } else {
            System.out.println("Aucun menu avec cet ID.");
        }

        Sys.pause();
    }

    private void modifierMenu() {
        System.out.println("\n--- MODIFICATION MENU ---\n");

        int id = saisieEntier("ID du menu à modifier : ");
        Optional<Menu> opt = menuService.findById(id);

        if (opt.isEmpty()) {
            System.out.println("Menu introuvable.");
            Sys.pause();
            return;
        }

        Menu m = opt.get();
        System.out.println("Menu actuel :\n" + m + "\n");
        System.out.println("(Appuyez sur Entrée pour conserver la valeur actuelle)\n");

        
        String nom = saisieChaineOuVide("Nom (" + m.getNom() + ") : ");
        if (!nom.isBlank()) {
            m.setNom(Sys.capitalize(nom));
        }

        
        Burger burger = selectionnerBurgerOuConserver();
        if (burger != null) {
            m.setBurger(burger);
        }

        
        Complement frites = selectionnerComplementOuConserver(ComplementType.Frites);
        if (frites != null) {
            m.setFrites(frites);
        }

        
        Complement boisson = selectionnerComplementOuConserver(ComplementType.Boisson);
        if (boisson != null) {
            m.setBoisson(boisson);
        }

        
        m.setPrix(calculerPrix(m));

        

        m.setEtat(saisieEtatOuVide(m.getEtat()));
       
        int result = menuService.updateMenu(m);

        System.out.println(
            result > 0
                ? "\nMenu modifié avec succès !"
                : "\nErreur lors de la modification."
        );

        Sys.pause();
    }

    
    private Burger selectionnerBurgerOuConserver() {
        System.out.print("ID du burger (Entrée = conserver) : ");
        String input = new Scanner(System.in).nextLine().trim();
        if (input.isEmpty()) return null; // conserver l'existant

        try {
            int burgerId = Integer.parseInt(input);
            Burger burger = burgerService.findById(burgerId).orElse(null);
            if (burger == null) {
                System.out.println("Burger introuvable.");
                return selectionnerBurgerOuConserver();
            }
            return burger;
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide.");
            return selectionnerBurgerOuConserver();
        }
    }

    private Complement selectionnerComplementOuConserver(ComplementType type) {
        System.out.print("ID de " + (type.equals(ComplementType.Frites) ? "la frite" : "la boisson") 
                        + " (Entrée = conserver) : ");
        String input = new Scanner(System.in).nextLine().trim();
        if (input.isEmpty()) return null; // conserver l'existant

        try {
            int compId = Integer.parseInt(input);
            Complement complement = complementService.findById(compId).orElse(null);
            if (complement == null || !complement.getComplementType().equals(type)) {
                System.out.println(type + " introuvable ou type incorrect.");
                return selectionnerComplementOuConserver(type);
            }
            return complement;
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide.");
            return selectionnerComplementOuConserver(type);
        }
    }
}

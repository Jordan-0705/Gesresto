package ges.resto;

import java.util.Scanner;

import ges.resto.factory.repository.EntityName;
import ges.resto.factory.service.ServiceFactory;
import ges.resto.service.BurgerService;
import ges.resto.service.ComplementService;
import ges.resto.service.GestionnaireService;
import ges.resto.service.MenuService;
import ges.resto.service.Sys;
import ges.resto.view.BurgerView;
import ges.resto.view.ComplementView;
import ges.resto.view.GestionnaireView;
import ges.resto.view.MenuView;

public class Main {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        GestionnaireService gestionnaireService = (GestionnaireService) ServiceFactory.getInstance(EntityName.Gestionnaire);
        GestionnaireView gestionnaireView = GestionnaireView.getInstance(gestionnaireService);

        
        if (!gestionnaireView.login()) {
            System.out.println("Échec de la connexion. Fin du programme.");
            return;
        }

        
        BurgerService burgerService = (BurgerService) ServiceFactory.getInstance(EntityName.Burger);
        ComplementService complementService = (ComplementService) ServiceFactory.getInstance(EntityName.Complement);
        MenuService menuService = (MenuService) ServiceFactory.getInstance(EntityName.Menu);

        BurgerView bView = BurgerView.getInstance(burgerService);
        ComplementView cView = ComplementView.getInstance(complementService);
        MenuView mView = MenuView.getInstance(burgerService, complementService, menuService);

        
        int choix;
        do {
            while (true) {
                Sys.cls();
                System.out.println("\n=== MENU PRINCIPAL ===\n");
                System.out.println("1 - Gérer les Burgers");
                System.out.println("2 - Gérer les Compléments");
                System.out.println("3 - Gérer les Menus");
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
                case 1 -> bView.showMenu();
                case 2 -> cView.showMenu();
                case 3 -> mView.showMenu();
                case 0 -> System.out.println("Au revoir !");
                default -> {
                    System.out.println("Choix invalide.");
                    Sys.pause();
                }
            }

        } while (choix != 0);
    }
}

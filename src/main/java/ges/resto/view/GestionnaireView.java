package ges.resto.view;

import java.util.Scanner;

import ges.resto.service.GestionnaireService;
import ges.resto.service.Sys;

public class GestionnaireView {

    private GestionnaireService service;

    private static GestionnaireView instance = null;

    public static GestionnaireView getInstance(GestionnaireService service) {
        if (instance == null) {
            instance = new GestionnaireView(service);
        }
        return instance;
    }
    private Scanner sc;

    private GestionnaireView(GestionnaireService service) {
        this.service = service;
        this.sc = new Scanner(System.in);
    }

    /**
     * Affiche l'écran de connexion et vérifie si le gestionnaire existe dans la BD
     * @return true si login réussi, false sinon
     */
    public boolean login() {
        System.out.println("=== CONNEXION GESTIONNAIRE ===");

        System.out.print("Login : ");
        String login = sc.nextLine();

        System.out.print("Mot de passe : ");
        String password = sc.nextLine(); // Pour plus de sécurité, utiliser System.console().readPassword()

        // Vérification via le service
        if (service.login(login, password)) {
            System.out.println("Connexion réussie !");
            Sys.pause();
            return true;
        } else {
            System.out.println("Login ou mot de passe incorrect.");
            Sys.pause();
            return false;
        }
    }
}


package ges.resto.service.impl;

import ges.resto.repository.GestionnaireRepository;
import ges.resto.service.GestionnaireService;

public class GestionnaireServiceImpl implements GestionnaireService {

    private static GestionnaireServiceImpl instance = null;
    private GestionnaireRepository repository;

    // Singleton
    public static GestionnaireServiceImpl getInstance(GestionnaireRepository repository) {
        if (instance == null) {
            instance = new GestionnaireServiceImpl(repository);
        }
        return instance;
    }

    private GestionnaireServiceImpl(GestionnaireRepository repository) {
        this.repository = repository;
    }

    /**
     * Vérifie si un gestionnaire existe avec le login et mot de passe donnés
     */
    @Override
    public boolean login(String login, String password) {
        return repository.exists(login, password);
    }
}

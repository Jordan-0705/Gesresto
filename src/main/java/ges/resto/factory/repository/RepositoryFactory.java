package ges.resto.factory.repository;

import ges.resto.database.Database;
import ges.resto.factory.database.DatabaseFactory;
import ges.resto.repository.impl.BurgerRepositoryImpl;
import ges.resto.repository.impl.ComplementRepositoryImpl;
import ges.resto.repository.impl.GestionnaireRepositoryImpl;
import ges.resto.repository.impl.MenuRepositoryImpl;

public final class RepositoryFactory {

    private static final PersistanceName PERSISTENCE_NAME = PersistanceName.Database;

    private RepositoryFactory() {
    }

    public static Object getInstance(EntityName entityName) {
        switch (PERSISTENCE_NAME) {
            case Database:
                return getRepositoryDatabase(entityName);
            default:
                throw new IllegalArgumentException("Unknown Persistence: " + PERSISTENCE_NAME);
        }
    }

    private static Object getRepositoryDatabase(EntityName entityName) {
        Database db = DatabaseFactory.getInstance();
        switch (entityName) {
            case Burger:
                return BurgerRepositoryImpl.getInstance(db);
            case Complement:
                return ComplementRepositoryImpl.getInstance(db);
            case Menu:
                return MenuRepositoryImpl.getInstance(db);
            case Gestionnaire:
                return GestionnaireRepositoryImpl.getInstance(db);
            default:
                throw new IllegalArgumentException("Unknown Entity: " + entityName);
        }
    }
}


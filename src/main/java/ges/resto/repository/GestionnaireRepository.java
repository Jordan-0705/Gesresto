package ges.resto.repository;

public interface GestionnaireRepository {
    public boolean exists(String login, String password);
}

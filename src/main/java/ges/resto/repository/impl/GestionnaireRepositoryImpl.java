package ges.resto.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import ges.resto.database.Database;
import ges.resto.repository.GestionnaireRepository;

public class GestionnaireRepositoryImpl implements GestionnaireRepository {

    private static GestionnaireRepositoryImpl instance = null;

    public static GestionnaireRepositoryImpl getInstance(Database database) {
        if (instance == null) {
            instance = new GestionnaireRepositoryImpl(database);
        }
        return instance;
    }

    private Database database;

    private GestionnaireRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public boolean exists(String login, String password) {
        String sql = "SELECT 1 FROM gestionnaire WHERE login=? AND password=?";
        try {
            Connection conn = database.getConnection(); 
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();

            rs.close();
            ps.close();

            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

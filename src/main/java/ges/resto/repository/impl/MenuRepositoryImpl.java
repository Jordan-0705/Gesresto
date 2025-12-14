package ges.resto.repository.impl;

import ges.resto.database.Database;
import ges.resto.entity.Burger;
import ges.resto.entity.Complement;
import ges.resto.entity.ComplementType;
import ges.resto.entity.Etat;
import ges.resto.entity.Menu;
import ges.resto.repository.MenuRepository;

import java.sql.*;
import java.util.*;

public class MenuRepositoryImpl implements MenuRepository {

    private static MenuRepositoryImpl instance = null;

    public static MenuRepositoryImpl getInstance(Database database) {
        if (instance == null) {
            instance = new MenuRepositoryImpl(database);
        }
        return instance;
    }

    private Database database;

    private MenuRepositoryImpl(Database db) {
        this.database = db;
    }

    @Override
    public int insert(Menu menu) {
        try {
            Connection conn = database.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO menu(code, nom, burger_id, frites_id, boisson_id, prix, etat) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setString(1, menu.getCode());
            ps.setString(2, menu.getNom());
            ps.setInt(3, menu.getBurger().getId());
            ps.setInt(4, menu.getFrites().getId());
            ps.setInt(5, menu.getBoisson().getId());
            ps.setDouble(6, menu.getPrix());
            ps.setString(7, menu.getEtat().name());

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Menu menu) {
        try {
            Connection conn = database.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE menu SET nom=?, burger_id=?, frites_id=?, boisson_id=?, prix=?, etat=? WHERE id=?"
            );

            ps.setString(1, menu.getNom());
            ps.setInt(2, menu.getBurger().getId());
            ps.setInt(3, menu.getFrites().getId());
            ps.setInt(4, menu.getBoisson().getId());
            ps.setDouble(5, menu.getPrix());
            ps.setString(6, menu.getEtat().name());
            ps.setInt(7, menu.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(int id) {
        try {
            Connection conn = database.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM menu WHERE id=?"
            );
            ps.setInt(1, id);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Menu> selectAll() {
        try {
            Connection conn = database.getConnection();

            String sql = """
                SELECT 
                    m.*,

                    -- BURGER
                    b.id     AS burger_id,
                    b.code   AS burger_code,
                    b.nom    AS burger_nom,
                    b.prix   AS burger_prix,
                    b.etat   AS burger_etat,

                    -- FRITE
                    f.id     AS frite_id,
                    f.code   AS frite_code,
                    f.nom    AS frite_nom,
                    f.prix   AS frite_prix,
                    f.complement_type   AS frite_type,
                    f.etat   AS frite_etat,

                    -- BOISSON
                    bo.id    AS boisson_id,
                    bo.code  AS boisson_code,
                    bo.nom   AS boisson_nom,
                    bo.prix  AS boisson_prix,
                    bo.complement_type  AS boisson_type,
                    bo.etat  AS boisson_etat

                FROM menu m
                JOIN burger b   ON m.burger_id  = b.id
                JOIN complement f  ON m.frites_id  = f.id
                JOIN complement bo ON m.boisson_id = bo.id
                ORDER BY m.id ASC
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            return database.fetchAll(ps, this::toEntity);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    @Override
    public Optional<Menu> selectById(int id) {
        try {
            Connection conn = database.getConnection();

            String sql = """
                SELECT 
                    m.*,

                    -- BURGER
                    b.id     AS burger_id,
                    b.code   AS burger_code,
                    b.nom    AS burger_nom,
                    b.prix   AS burger_prix,
                    b.etat   AS burger_etat,

                    -- FRITE
                    f.id     AS frite_id,
                    f.code   AS frite_code,
                    f.nom    AS frite_nom,
                    f.prix   AS frite_prix,
                    f.complement_type AS frite_type,
                    f.etat   AS frite_etat,

                    -- BOISSON
                    bo.id    AS boisson_id,
                    bo.code  AS boisson_code,
                    bo.nom   AS boisson_nom,
                    bo.prix  AS boisson_prix,
                    bo.complement_type AS boisson_type,
                    bo.etat  AS boisson_etat

                FROM menu m
                JOIN burger b   ON m.burger_id  = b.id
                JOIN complement f  ON m.frites_id  = f.id
                JOIN complement bo ON m.boisson_id = bo.id

                WHERE m.id = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return database.fetch(ps, this::toEntity);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Menu toEntity(ResultSet rs) throws SQLException {
        return mapMenu(rs);
    }

     public static Menu mapMenu(ResultSet rs) throws SQLException {
        Menu m = new Menu();

        m.setId(rs.getInt("id"));
        m.setCode(rs.getString("code"));
        m.setNom(rs.getString("nom"));
        m.setPrix(rs.getDouble("prix"));
        m.setEtat(
            "Disponible".equals(rs.getString("etat")) ? Etat.Disponible : Etat.Archived
        );

        m.setBurger(mapBurger(rs));
        m.setFrites(mapComplement(rs, "frite_"));
        m.setBoisson(mapComplement(rs, "boisson_"));

        return m;
    }

    private static Burger mapBurger(ResultSet rs) throws SQLException {
        Burger b = new Burger();

        b.setId(rs.getInt("burger_id"));
        b.setCode(rs.getString("burger_code"));
        b.setNom(rs.getString("burger_nom"));
        b.setPrix(rs.getDouble("burger_prix"));
        b.setEtat(
            "Disponible".equals(rs.getString("burger_etat")) ? Etat.Disponible : Etat.Archived
        );

        return b;
    }

    private static Complement mapComplement(ResultSet rs, String prefix) throws SQLException {
        Complement c = new Complement();

        c.setId(rs.getInt(prefix + "id"));
        c.setCode(rs.getString(prefix + "code"));
        c.setNom(rs.getString(prefix + "nom"));
        c.setPrix(rs.getDouble(prefix + "prix"));

        String type = rs.getString(prefix + "type");
        c.setComplementType(ComplementType.valueOf(type));
        c.setEtat(rs.getString(prefix + "etat").equals("Disponible") ? Etat.Disponible : Etat.Archived);

        return c;
    }
}


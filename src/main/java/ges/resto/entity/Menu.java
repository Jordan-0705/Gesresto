package ges.resto.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Menu {
    private int id;
    private String code;
    private String nom;
    private Burger burger;
    private Complement frites;
    private Complement boisson;
    private double prix;
    private Etat etat;

    @Override
    public String toString() {
        return String.format(
                "%03d | %-6s | %-18s | BURGER: %-18s | FRITE: %-12s | BOISSON: %-12s | %9s FCFA | %s",
                id,
                code,
                nom,
                burger != null ? burger.getNom() : "Aucun",
                frites != null ? frites.getNom() : "Aucune",
                boisson != null ? boisson.getNom() : "Aucune",
                prix,
                etat
        );
    }
}

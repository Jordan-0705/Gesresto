package ges.resto.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Complement {
    private int id;
    private String code;
    private String nom;
    private double prix;
    private ComplementType ComplementType;
    private Etat etat;

    @Override
    public String toString() {
        return String.format("%03d | %-7s | %-20s | %8.2f FCFA | %-10s | %-7s",
                id, code, nom, prix, ComplementType, etat);
    }
}

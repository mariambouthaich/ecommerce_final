
package model;

import java.sql.Timestamp;

/**
 * Modèle : Commande passée par un utilisateur
 */
public class Commande {
    private int       id;
    private int       userId;
    private Timestamp dateCommande;
    private String    statut;       // EN_ATTENTE | VALIDEE | ANNULEE

    // ── Constructeurs ────────────────────────────────────────
    public Commande() {}

    public Commande(int userId) {
        this.userId = userId;
        this.statut = "EN_ATTENTE";
    }

    public Commande(int userId, String statut) {
        this.userId = userId;
        this.statut = statut;
    }

    // ── Getters ──────────────────────────────────────────────
    public int       getId()           { return id; }
    public int       getUserId()       { return userId; }
    public Timestamp getDateCommande() { return dateCommande; }
    public String    getStatut()       { return statut; }

    // ── Setters ──────────────────────────────────────────────
    public void setId(int id)                       { this.id = id; }
    public void setUserId(int userId)               { this.userId = userId; }
    public void setDateCommande(Timestamp date)     { this.dateCommande = date; }
    public void setStatut(String statut)            { this.statut = statut; }

    @Override
    public String toString() {
        return "Commande{id=" + id + ", userId=" + userId
                + ", date=" + dateCommande + ", statut='" + statut + "'}";
    }
}

package dao;

import database.DatabaseConnection;
import model.Commande;
import model.LigneCommande;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO : Opérations sur les tables commandes + lignes_commande
 */
public class CommandeDAO {

    // ─────────────────────────────────────────────
    // CREATE — Créer une nouvelle commande
    // ─────────────────────────────────────────────
    public int creerCommande(int userId) {
        String sql = "INSERT INTO commandes (user_id, statut) VALUES (?, 'EN_ATTENTE')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("✅ Commande créée, ID = " + id);
                return id;
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur creerCommande : " + e.getMessage());
        }
        return -1;
    }

    // ─────────────────────────────────────────────
    // CREATE — Ajouter une ligne à une commande
    // ─────────────────────────────────────────────
    public void ajouterLigneCommande(LigneCommande lc) {
        String sql = "INSERT INTO lignes_commande (commande_id, produit_id, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lc.getCommandeId());
            ps.setInt(2, lc.getProduitId());
            ps.setInt(3, lc.getQuantite());
            ps.setDouble(4, lc.getPrixUnitaire());
            ps.executeUpdate();
            System.out.println("✅ Ligne ajoutée : produit " + lc.getProduitId()
                    + " × " + lc.getQuantite());

        } catch (Exception e) {
            System.err.println("❌ Erreur ajouterLigneCommande : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // READ — Commandes d'un utilisateur
    // ─────────────────────────────────────────────
    public List<Commande> getCommandesByUser(int userId) {
        List<Commande> list = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Commande c = new Commande();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("user_id"));
                c.setDateCommande(rs.getTimestamp("date_commande"));
                c.setStatut(rs.getString("statut"));
                list.add(c);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur getCommandesByUser : " + e.getMessage());
        }
        return list;
    }

    // ─────────────────────────────────────────────
    // READ — Lignes d'une commande
    // ─────────────────────────────────────────────
    public List<LigneCommande> getLignesByCommande(int commandeId) {
        List<LigneCommande> list = new ArrayList<>();
        String sql = "SELECT * FROM lignes_commande WHERE commande_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, commandeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LigneCommande lc = new LigneCommande();
                lc.setId(rs.getInt("id"));
                lc.setCommandeId(rs.getInt("commande_id"));
                lc.setProduitId(rs.getInt("produit_id"));
                lc.setQuantite(rs.getInt("quantite"));
                lc.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                list.add(lc);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur getLignesByCommande : " + e.getMessage());
        }
        return list;
    }

    // ─────────────────────────────────────────────
    // UPDATE — Changer le statut d'une commande
    // ─────────────────────────────────────────────
    public void updateStatut(int commandeId, String statut) {
        String sql = "UPDATE commandes SET statut=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut);
            ps.setInt(2, commandeId);
            ps.executeUpdate();
            System.out.println("✅ Statut mis à jour → " + statut);

        } catch (Exception e) {
            System.err.println("❌ Erreur updateStatut : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // DELETE — Annuler/supprimer une commande
    // ─────────────────────────────────────────────
    public void supprimerCommande(int commandeId) {
        String sql = "DELETE FROM commandes WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, commandeId);
            ps.executeUpdate();
            System.out.println("🗑 Commande supprimée (id=" + commandeId + ")");

        } catch (Exception e) {
            System.err.println("❌ Erreur supprimerCommande : " + e.getMessage());
        }
    }
}

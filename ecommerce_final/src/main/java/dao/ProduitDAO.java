
package dao;

import database.DatabaseConnection;
import model.Produit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO : Opérations CRUD sur la table produits
 */
public class ProduitDAO {

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────
    public void ajouterProduit(Produit p) {
        String sql = "INSERT INTO produits (nom, description, prix, stock, categorie_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getCategorieId());
            ps.executeUpdate();
            System.out.println("✅ Produit ajouté : " + p.getNom());

        } catch (Exception e) {
            System.err.println("❌ Erreur ajouterProduit : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // READ — Tous les produits
    // ─────────────────────────────────────────────
    public List<Produit> getAllProduits() {
        List<Produit> list = new ArrayList<>();
        String sql = "SELECT * FROM produits";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Produit p = mapResultSet(rs);
                list.add(p);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur getAllProduits : " + e.getMessage());
        }
        return list;
    }

    // ─────────────────────────────────────────────
    // READ — Produits par catégorie
    // ─────────────────────────────────────────────
    public List<Produit> getProduitsByCategorie(int categorieId) {
        List<Produit> list = new ArrayList<>();
        String sql = "SELECT * FROM produits WHERE categorie_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categorieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur getProduitsByCategorie : " + e.getMessage());
        }
        return list;
    }

    // ─────────────────────────────────────────────
    // READ — Un produit par ID
    // ─────────────────────────────────────────────
    public Produit getProduitById(int id) {
        String sql = "SELECT * FROM produits WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);

        } catch (Exception e) {
            System.err.println("❌ Erreur getProduitById : " + e.getMessage());
        }
        return null;
    }

    // ─────────────────────────────────────────────
    // UPDATE — Mettre à jour le stock
    // ─────────────────────────────────────────────
    public void updateStock(int id, int newStock) {
        String sql = "UPDATE produits SET stock=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newStock);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("✅ Stock mis à jour (produit id=" + id + " → " + newStock + ")");

        } catch (Exception e) {
            System.err.println("❌ Erreur updateStock : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // UPDATE — Mettre à jour tout le produit
    // ─────────────────────────────────────────────
    public void updateProduit(Produit p) {
        String sql = "UPDATE produits SET nom=?, description=?, prix=?, stock=?, categorie_id=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getCategorieId());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
            System.out.println("✅ Produit modifié : " + p.getNom());

        } catch (Exception e) {
            System.err.println("❌ Erreur updateProduit : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────
    public void supprimerProduit(int id) {
        String sql = "DELETE FROM produits WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("🗑 Produit supprimé (id=" + id + ")");

        } catch (Exception e) {
            System.err.println("❌ Erreur supprimerProduit : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // Méthode utilitaire privée
    // ─────────────────────────────────────────────
    private Produit mapResultSet(ResultSet rs) throws SQLException {
        Produit p = new Produit();
        p.setId(rs.getInt("id"));
        p.setNom(rs.getString("nom"));
        p.setDescription(rs.getString("description"));
        p.setPrix(rs.getDouble("prix"));
        p.setStock(rs.getInt("stock"));
        p.setCategorieId(rs.getInt("categorie_id"));
        return p;
    }

    // ─────────────────────────────────────────────
    // Afficher dans la console (méthode héritée)
    // ─────────────────────────────────────────────
    public void afficherProduits() {
        getAllProduits().forEach(System.out::println);
    }
}

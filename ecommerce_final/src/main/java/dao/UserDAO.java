package dao;

import database.DatabaseConnection;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO : Opérations CRUD sur la table utilisateurs
 */
public class UserDAO {

    // ─────────────────────────────────────────────
    // CREATE — Ajouter un utilisateur
    // ─────────────────────────────────────────────
    public void ajouterUser(User user) {
        String sql = "INSERT INTO utilisateurs (nom, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getNom());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
            System.out.println("✅ Utilisateur ajouté : " + user.getNom());

        } catch (Exception e) {
            System.err.println("❌ Erreur ajouterUser : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // READ — Récupérer tous les utilisateurs
    // ─────────────────────────────────────────────
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                list.add(u);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur getAllUsers : " + e.getMessage());
        }
        return list;
    }

    // ─────────────────────────────────────────────
    // READ — Trouver un utilisateur par email
    // ─────────────────────────────────────────────
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                return u;
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur getUserByEmail : " + e.getMessage());
        }
        return null;
    }

    // ─────────────────────────────────────────────
    // UPDATE — Modifier un utilisateur
    // ─────────────────────────────────────────────
    public void updateUser(User user) {
        String sql = "UPDATE utilisateurs SET nom=?, email=?, password=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getNom());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.executeUpdate();
            System.out.println("✅ Utilisateur modifié : " + user.getNom());

        } catch (Exception e) {
            System.err.println("❌ Erreur updateUser : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // DELETE — Supprimer un utilisateur
    // ─────────────────────────────────────────────
    public void deleteUser(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("🗑 Utilisateur supprimé (id=" + id + ")");

        } catch (Exception e) {
            System.err.println("❌ Erreur deleteUser : " + e.getMessage());
        }
    }
}

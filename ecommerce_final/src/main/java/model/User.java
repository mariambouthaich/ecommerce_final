package model;
public class User {
    private int    id;
    private String nom;
    private String email;
    private String password;

    // ── Constructeurs ────────────────────────────────────────
    public User() {}

    public User(String nom, String email, String password) {
        this.nom      = nom;
        this.email    = email;
        this.password = password;
    }

    // ── Getters ──────────────────────────────────────────────
    public int    getId()       { return id; }
    public String getNom()      { return nom; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }

    // ── Setters ──────────────────────────────────────────────
    public void setId(int id)             { this.id = id; }
    public void setNom(String nom)        { this.nom = nom; }
    public void setEmail(String email)    { this.email = email; }
    public void setPassword(String p)     { this.password = p; }
    @Override
    public String toString() {
        return "User{id=" + id + ", nom='" + nom + "', email='" + email + "'}";
    }
}
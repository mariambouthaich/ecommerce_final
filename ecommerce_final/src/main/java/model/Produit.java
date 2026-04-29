
package model;

/**
 * Modèle : Produit disponible dans la boutique
 */
public class Produit {
    private int       id;
    private String    nom;
    private String    description;
    private double    prix;
    private int       stock;
    private int       categorieId;

    // ── Constructeurs ────────────────────────────────────────
    public Produit() {}

    /** Constructeur simple (sans description ni catégorie) */
    public Produit(String nom, double prix, int stock) {
        this.nom   = nom;
        this.prix  = prix;
        this.stock = stock;
    }

    /** Constructeur complet */
    public Produit(String nom, String description, double prix, int stock, int categorieId) {
        this.nom         = nom;
        this.description = description;
        this.prix        = prix;
        this.stock       = stock;
        this.categorieId = categorieId;
    }

    // ── Getters ──────────────────────────────────────────────
    public int    getId()          { return id; }
    public String getNom()         { return nom; }
    public String getDescription() { return description; }
    public double getPrix()        { return prix; }
    public int    getStock()       { return stock; }
    public int    getCategorieId() { return categorieId; }

    // ── Setters ──────────────────────────────────────────────
    public void setId(int id)                   { this.id = id; }
    public void setNom(String nom)              { this.nom = nom; }
    public void setDescription(String desc)     { this.description = desc; }
    public void setPrix(double prix)            { this.prix = prix; }
    public void setStock(int stock)             { this.stock = stock; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }

    @Override
    public String toString() {
        return "Produit{id=" + id + ", nom='" + nom + "', prix=" + prix
                + ", stock=" + stock + ", categorieId=" + categorieId + "}";
    }
}

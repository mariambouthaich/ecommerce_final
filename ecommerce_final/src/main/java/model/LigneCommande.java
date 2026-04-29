package model;
/**
 * Modèle : Ligne d'une commande (un produit + quantité + prix au moment de l'achat)
 * Une commande peut contenir plusieurs LigneCommande.
 */
public class LigneCommande {
    private int    id;
    private int    commandeId;
    private int    produitId;
    private int    quantite;
    private double prixUnitaire;  // prix enregistré au moment de l'achat

    // ── Constructeurs ────────────────────────────────────────
    public LigneCommande() {}

    public LigneCommande(int commandeId, int produitId, int quantite, double prixUnitaire) {
        this.commandeId   = commandeId;
        this.produitId    = produitId;
        this.quantite     = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    // ── Getters ──────────────────────────────────────────────
    public int    getId()           { return id; }
    public int    getCommandeId()   { return commandeId; }
    public int    getProduitId()    { return produitId; }
    public int    getQuantite()     { return quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }

    /** Total de cette ligne = quantite × prixUnitaire */
    public double getTotal() {
        return quantite * prixUnitaire;
    }

    // ── Setters ──────────────────────────────────────────────
    public void setId(int id)                     { this.id = id; }
    public void setCommandeId(int commandeId)     { this.commandeId = commandeId; }
    public void setProduitId(int produitId)       { this.produitId = produitId; }
    public void setQuantite(int quantite)         { this.quantite = quantite; }
    public void setPrixUnitaire(double prix)      { this.prixUnitaire = prix; }

    @Override
    public String toString() {
        return "LigneCommande{commandeId=" + commandeId + ", produitId=" + produitId
                + ", qte=" + quantite + ", prix=" + prixUnitaire
                + ", total=" + getTotal() + "}";
    }
}

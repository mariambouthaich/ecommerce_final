package controller;

import dao.CategorieDAO;
import dao.ProduitDAO;
import model.Categorie;
import model.Panier;
import model.Produit;
import model.User;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller : Catalogue produits
 * - Grille de cartes avec image, nom, prix, bouton Ajouter
 * - Recherche temps réel
 * - Filtre par catégorie
 * - Filtre par fourchette de prix (Slider)
 * - Animation hover JavaFX sur chaque carte
 * - Badge panier dynamique
 */
public class CatalogueController implements Initializable {

    // ── Navbar ───────────────────────────────────────────────
    @FXML private Label   welcomeLabel;
    @FXML private Label   badgePanier;
    @FXML private Button  panierButton;
    @FXML private Button  historiqueButton;
    @FXML private Button  profilButton;

    // ── Barre de filtres ─────────────────────────────────────
    @FXML private TextField        searchField;
    @FXML private ComboBox<String> categorieCombo;
    @FXML private Slider           prixMinSlider;
    @FXML private Slider           prixMaxSlider;
    @FXML private Label            prixMinLabel;
    @FXML private Label            prixMaxLabel;
    @FXML private Label            nbResultatsLabel;

    // ── Grille produits ──────────────────────────────────────
    @FXML private GridPane produitsGrid;

    // ── Données ──────────────────────────────────────────────
    private final ProduitDAO   produitDAO   = new ProduitDAO();
    private final CategorieDAO categorieDAO = new CategorieDAO();

    private User             currentUser;
    private Panier           panier;
    private List<Produit>    allProduits;
    private List<Categorie>  allCategories;
    private double           prixMax = 10000;

    // ────────────────────────────────────────────────────────
    // Initialisation
    // ────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allProduits   = produitDAO.getAllProduits();
        allCategories = categorieDAO.getAllCategories();

        // Prix max dynamique depuis la BD
        prixMax = allProduits.stream()
                .mapToDouble(Produit::getPrix)
                .max().orElse(10000);

        // ── ComboBox catégories ──────────────────────────────
        List<String> noms = allCategories.stream()
                .map(Categorie::getNom)
                .collect(Collectors.toList());
        noms.add(0, "Toutes les catégories");
        categorieCombo.setItems(FXCollections.observableArrayList(noms));
        categorieCombo.getSelectionModel().selectFirst();

        // ── Sliders prix ─────────────────────────────────────
        prixMinSlider.setMin(0);
        prixMinSlider.setMax(prixMax);
        prixMinSlider.setValue(0);

        prixMaxSlider.setMin(0);
        prixMaxSlider.setMax(prixMax);
        prixMaxSlider.setValue(prixMax);

        updatePrixLabels();

        // ── Listeners temps réel ─────────────────────────────
        searchField.textProperty().addListener((o, v, n) -> filtrer());
        categorieCombo.valueProperty().addListener((o, v, n) -> filtrer());
        prixMinSlider.valueProperty().addListener((o, v, n) -> {
            if (n.doubleValue() > prixMaxSlider.getValue())
                prixMinSlider.setValue(prixMaxSlider.getValue());
            updatePrixLabels();
            filtrer();
        });
        prixMaxSlider.valueProperty().addListener((o, v, n) -> {
            if (n.doubleValue() < prixMinSlider.getValue())
                prixMaxSlider.setValue(prixMinSlider.getValue());
            updatePrixLabels();
            filtrer();
        });

        afficherProduits(allProduits);
    }

    // ────────────────────────────────────────────────────────
    // Injecter l'utilisateur depuis Login
    // ────────────────────────────────────────────────────────
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.panier      = new Panier(user.getId());
        if (welcomeLabel != null)
            welcomeLabel.setText("Bonjour, " + user.getNom() + " 👋");
    }

    // ────────────────────────────────────────────────────────
    // Filtrage combiné : texte + catégorie + prix
    // ────────────────────────────────────────────────────────
    private void filtrer() {
        String  texte    = searchField.getText().toLowerCase().trim();
        String  categNom = categorieCombo.getValue();
        double  pMin     = prixMinSlider.getValue();
        double  pMax     = prixMaxSlider.getValue();

        List<Produit> filtres = allProduits.stream()
                // Filtre texte
                .filter(p -> p.getNom().toLowerCase().contains(texte)
                          || (p.getDescription() != null
                              && p.getDescription().toLowerCase().contains(texte)))
                // Filtre catégorie
                .filter(p -> {
                    if (categNom == null || categNom.equals("Toutes les catégories"))
                        return true;
                    return allCategories.stream()
                            .filter(c -> c.getNom().equals(categNom))
                            .anyMatch(c -> c.getId() == p.getCategorieId());
                })
                // Filtre prix
                .filter(p -> p.getPrix() >= pMin && p.getPrix() <= pMax)
                .collect(Collectors.toList());

        afficherProduits(filtres);
    }

    // ────────────────────────────────────────────────────────
    // Mettre à jour les labels des sliders
    // ────────────────────────────────────────────────────────
    private void updatePrixLabels() {
        prixMinLabel.setText(String.format("%.0f MAD", prixMinSlider.getValue()));
        prixMaxLabel.setText(String.format("%.0f MAD", prixMaxSlider.getValue()));
    }

    // ────────────────────────────────────────────────────────
    // Afficher les cartes dans la grille
    // ────────────────────────────────────────────────────────
    private void afficherProduits(List<Produit> produits) {
        produitsGrid.getChildren().clear();
        produitsGrid.getColumnConstraints().clear();

        int cols = 3;
        for (int i = 0; i < cols; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / cols);
            produitsGrid.getColumnConstraints().add(cc);
        }

        // Compteur résultats
        if (nbResultatsLabel != null)
            nbResultatsLabel.setText(produits.size() + " produit(s) trouvé(s)");

        int col = 0, row = 0;
        for (Produit p : produits) {
            VBox card = creerCarte(p);
            produitsGrid.add(card, col, row);
            GridPane.setMargin(card, new Insets(10));
            col++;
            if (col == cols) { col = 0; row++; }
        }
    }

    // ────────────────────────────────────────────────────────
    // Créer une carte produit avec image + animation hover
    // ────────────────────────────────────────────────────────
    private VBox creerCarte(Produit p) {
        VBox card = new VBox(10);
        card.getStyleClass().add("product-card");
        card.setPadding(new Insets(0, 0, 16, 0));
        card.setAlignment(Pos.TOP_CENTER);

        // ── Image du produit ─────────────────────────────────
        ImageView imageView = new ImageView();
        imageView.setFitWidth(240);
        imageView.setFitHeight(160);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        // Essaie de charger une image par catégorie, sinon image par défaut
        String imgPath = getImagePath(p.getCategorieId());
        try {
            Image img = new Image(
                    getClass().getResourceAsStream(imgPath), 240, 160, true, true);
            imageView.setImage(img);
        } catch (Exception e) {
            // Image de remplacement si fichier absent
            imageView.setStyle("-fx-background-color: #e0e7ff;");
        }

        StackPane imageBox = new StackPane(imageView);
        imageBox.getStyleClass().add("card-image-box");
        imageBox.setPrefHeight(160);

        // ── Badge stock ──────────────────────────────────────
        Label stockBadge = new Label(
                p.getStock() > 0 ? "✔ En stock" : "✘ Rupture");
        stockBadge.getStyleClass().add(
                p.getStock() > 0 ? "badge-stock-ok" : "badge-stock-empty");

        // ── Infos ────────────────────────────────────────────
        VBox infos = new VBox(6);
        infos.setPadding(new Insets(0, 14, 0, 14));

        Label nom = new Label(p.getNom());
        nom.getStyleClass().add("product-name");
        nom.setWrapText(true);

        Label desc = new Label(p.getDescription() != null
                ? p.getDescription() : "");
        desc.getStyleClass().add("product-desc");
        desc.setWrapText(true);
        desc.setMaxHeight(38);

        Label prix = new Label(String.format("%.2f MAD", p.getPrix()));
        prix.getStyleClass().add("product-price");

        // ── Bouton Ajouter ───────────────────────────────────
        Button addBtn = new Button("🛒  Ajouter au panier");
        addBtn.getStyleClass().add("btn-add");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setDisable(p.getStock() == 0);
        addBtn.setOnAction(e -> ajouterAuPanier(p, addBtn));

        infos.getChildren().addAll(nom, desc, prix);
        card.getChildren().addAll(imageBox, stockBadge, infos, addBtn);
        VBox.setMargin(addBtn, new Insets(0, 14, 0, 14));

        // ── Animation hover (ScaleTransition JavaFX) ─────────
        DropShadow shadowNormal = new DropShadow(10, Color.rgb(0, 0, 0, 0.08));
        DropShadow shadowHover  = new DropShadow(24, Color.rgb(99, 102, 241, 0.28));

        card.setEffect(shadowNormal);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(180), card);
        scaleUp.setToX(1.04);
        scaleUp.setToY(1.04);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(180), card);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        card.setOnMouseEntered(e -> {
            card.setEffect(shadowHover);
            scaleUp.playFromStart();
        });
        card.setOnMouseExited(e -> {
            card.setEffect(shadowNormal);
            scaleDown.playFromStart();
        });

        return card;
    }

    // ────────────────────────────────────────────────────────
    // Retourner le chemin image selon la catégorie
    // ────────────────────────────────────────────────────────
    private String getImagePath(int categorieId) {
        // Place tes images dans src/main/resources/images/
        return switch (categorieId) {
            case 1  -> "/images/electronique.png";
            case 2  -> "/images/informatique.png";
            case 3  -> "/images/accessoires.png";
            case 4  -> "/images/vetements.png";
            case 5  -> "/images/maison.png";
            default -> "/images/default.png";
        };
    }

    // ────────────────────────────────────────────────────────
    // Ajouter au panier + feedback visuel sur le bouton
    // ────────────────────────────────────────────────────────
    private void ajouterAuPanier(Produit p, Button btn) {
        if (panier == null) return;
        panier.ajouterItem(p, 1);

        // Mise à jour badge
        int total = panier.getItems().stream()
                .mapToInt(i -> i.getQuantite()).sum();
        badgePanier.setText(String.valueOf(total));
        badgePanier.setVisible(true);

        // Feedback bouton temporaire
        String originalText = btn.getText();
        btn.setText("✅ Ajouté !");
        btn.setDisable(true);
        new Thread(() -> {
            try { Thread.sleep(1200); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> {
                btn.setText(originalText);
                btn.setDisable(false);
            });
        }).start();
    }

    // ────────────────────────────────────────────────────────
    // Réinitialiser les filtres
    // ────────────────────────────────────────────────────────
    @FXML
    public void reinitialiserFiltres() {
        searchField.clear();
        categorieCombo.getSelectionModel().selectFirst();
        prixMinSlider.setValue(0);
        prixMaxSlider.setValue(prixMax);
    }

    // ────────────────────────────────────────────────────────
    // Navigation
    // ────────────────────────────────────────────────────────
    @FXML
    public void ouvrirPanier() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/panier.fxml"));
            Parent root = loader.load();
            PanierController ctrl = loader.getController();
            ctrl.setContext(currentUser, panier);
            Stage stage = (Stage) panierButton.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 620));
            stage.setTitle("🛒 Mon Panier");
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void ouvrirHistorique() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/historique.fxml"));
            Parent root = loader.load();
            HistoriqueController ctrl = loader.getController();
            ctrl.setCurrentUser(currentUser);
            Stage stage = (Stage) historiqueButton.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 620));
            stage.setTitle("📋 Mes Commandes");
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void ouvrirProfil() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/profil.fxml"));
            Parent root = loader.load();
            ProfilController ctrl = loader.getController();
            ctrl.setCurrentUser(currentUser);
            Stage stage = (Stage) profilButton.getScene().getWindow();
            stage.setScene(new Scene(root, 520, 540));
            stage.setTitle("👤 Mon Profil");
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void seDeconnecter() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/login-client.fxml"));
            Stage stage = (Stage) panierButton.getScene().getWindow();
            stage.setScene(new Scene(root, 480, 420));
            stage.setTitle("Connexion");
        } catch (IOException e) { e.printStackTrace(); }
    }
}

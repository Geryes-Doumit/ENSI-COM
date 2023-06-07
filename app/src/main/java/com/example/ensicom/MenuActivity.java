package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class MenuActivity extends AppCompatActivity {

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Référence au bouton du menu
        Button menuButton = findViewById(R.id.menu_button);

        // Référence à la vue du contenu de la fenêtre contextuelle
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_menu, null);

        // Création de la fenêtre contextuelle avec les dimensions souhaitées
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Configuration de la fenêtre contextuelle
        popupWindow.setOutsideTouchable(true); // Clique en dehors de la fenêtre pour la fermer
        popupWindow.setFocusable(true); // Permet d'interagir avec les éléments de la fenêtre

        // Gestionnaire d'événements du bouton du menu
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Affichage de la fenêtre contextuelle au-dessus du bouton du menu
                popupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, 0, 0);
            }
        });

        // Gestionnaire d'événements du bouton de fermeture de la fenêtre contextuelle
        Button closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fermeture de la fenêtre contextuelle
                popupWindow.dismiss();
            }
        });

        // Ajoutez ici le code pour gérer les événements de sélection des filtres (CheckBox)
    }
}

package ma.saifdine.hd.ui.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.Button;

import ma.saifdine.hd.R;

public class ConfirmationDialog {

    /**
     * Affiche un dialog de confirmation personnalisé.
     *
     * @param activity   L'activité actuelle.
     * @param title      Le titre du dialog.
     * @param message    Le message de confirmation.
     * @param icon       L'icône à afficher dans le dialog (peut être null).
     * @param onConfirm  Action à exécuter lorsque l'utilisateur confirme.
     * @param onCancel   Action à exécuter lorsque l'utilisateur annule (peut être null).
     */
    public static void show(Activity activity, String title, String message, Drawable icon,
                            Runnable onConfirm, Runnable onCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);

        if (icon != null) {
            builder.setIcon(icon);
        }

        builder.setPositiveButton("Oui", (dialog, which) -> {
            if (onConfirm != null) {
                onConfirm.run();
            }
        });

        builder.setNegativeButton("Non", (dialog, which) -> {
            if (onCancel != null) {
                onCancel.run();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Personnaliser les boutons
        customizeDialogButtons(activity, dialog);
    }

    /**
     * Personnalise les boutons d'un AlertDialog.
     *
     * @param activity L'activité actuelle.
     * @param dialog   L'instance de l'AlertDialog.
     */
    private static void customizeDialogButtons(Activity activity, AlertDialog dialog) {
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        positiveButton.setTextColor(activity.getResources().getColor(R.color.accent));
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        negativeButton.setTextColor(activity.getResources().getColor(R.color.error));
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }
}

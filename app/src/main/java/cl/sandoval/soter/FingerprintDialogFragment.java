package cl.sandoval.soter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class FingerprintDialogFragment extends DialogFragment {

    private OnFingerprintChoiceListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFingerprintChoiceListener) {
            listener = (OnFingerprintChoiceListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFingerprintChoiceListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("¿Usar huella dactilar?")
                .setMessage("¿Quieres usar tu huella dactilar para iniciar sesión la próxima vez?")
                .setPositiveButton("Sí", (dialog, id) -> {
                    if (listener != null) {
                        listener.onFingerprintEnabled(true);
                    }
                })
                .setNegativeButton("No", (dialog, id) -> {
                    if (listener != null) {
                        listener.onFingerprintEnabled(false);
                    }
                })
                .create();
    }

    public interface OnFingerprintChoiceListener {
        void onFingerprintEnabled(boolean enabled);
    }
}

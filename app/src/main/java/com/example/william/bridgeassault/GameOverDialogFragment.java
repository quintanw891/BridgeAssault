package com.example.william.bridgeassault;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by William on 9/5/2018.
 */

public class GameOverDialogFragment extends DialogFragment {

    public interface GameOverDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    GameOverDialogListener gListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            gListener = (GameOverDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GameOverDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title, content;
        if(getArguments().getBoolean("WON")){
            title = getResources().getString(R.string.game_over_dialog_title_win);
            content = getResources().getString(R.string.game_over_dialog_content_win);
        } else{
            title = getResources().getString(R.string.game_over_dialog_title_lose);
            if(getArguments().getBoolean("DIED"))
                content = getResources().getString(R.string.game_over_dialog_content_die);
            else
                content = getResources().getString(R.string.game_over_dialog_content_collapse);
        }
        builder.setMessage(content)
                .setPositiveButton(R.string.game_over_dialog_button_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        gListener.onDialogPositiveClick(GameOverDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.game_over_dialog_button_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        gListener.onDialogNegativeClick(GameOverDialogFragment.this);
                    }
                })
        .setTitle(title);

        return builder.create();
    }

}

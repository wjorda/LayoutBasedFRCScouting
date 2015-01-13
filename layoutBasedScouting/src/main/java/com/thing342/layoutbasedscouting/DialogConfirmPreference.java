package com.thing342.layoutbasedscouting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

/**
 * A <code>Preference</code> in which the user has to confirm the operation after selecting it.
 */
public class DialogConfirmPreference extends Preference
{

    private String dialogTitle = "Confirm";
    private String message = "Do ou want to continue?";
    private String okText = "OK";
    private String cancelText = "Cancel";
    private String toastText;
    private int toastLength = Toast.LENGTH_LONG;
    private int dialogIcon = android.R.drawable.ic_dialog_alert;
    private OnDialogListener listener;

    //////////////////--CONSTRUCTORS--/////////////////////

    public DialogConfirmPreference(Context context)
    {
        super(context);
        setListener(new DefaultOnDialogListener());
    }

    /**
     * Creates a <code>DialogConfirmPreference</code> with all fields set.
     *
     * @param context     Application context.
     * @param title       Name to appear on the preference.
     * @param summary     A description for the user as to what the preference with perform.
     * @param icon        A drawable id to appear next to the preference.
     * @param dialogTitle The title of the dialog to appear.
     * @param message     The message contained within the dialog.
     * @param okText      Message on the button used to confirm the operation.
     * @param cancelText  Message on the button to cancel the operation.
     * @param toastText   Text on the <code>Toast</code> to appear after the opertation is confirmed
     * @param toastLength Length the <code>Toast</code> is to appear on screen.
     * @param dialogIcon  Icon to appear on the dialog.
     */
    public DialogConfirmPreference(Context context, String title, String summary, int icon,
                                   String dialogTitle, String message, String okText,
                                   String cancelText, String toastText,
                                   int toastLength, int dialogIcon)
    {
        super(context);

        setTitle(title);
        setSummary(summary);
        //setIcon(icon);

        this.dialogTitle = dialogTitle;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.toastText = toastText;
        this.toastLength = toastLength;
        this.dialogIcon = dialogIcon;
    }

    /////////////////--OVERIDDEN METHODS--//////////////////

    @Override
    public void onClick()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(getContext(), R.style.Theme_AppCompat_Light_Dialog));
        builder
                .setTitle(dialogTitle)
                .setMessage(message)
                .setIcon(dialogIcon)
                .setPositiveButton(okText, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Yes button clicked, do something
                        Toast.makeText(getContext(), toastText, toastLength).show();
                        listener.onConfirm();
                    }
                })
                .setNegativeButton(cancelText, new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        listener.onDecline();

                    }
                })
                .show();
    }

    /////////////////--GETTERS & SETTERS--////////////////////////////

    /**
     * @param listener the listener to set
     */
    public void setListener(OnDialogListener listener)
    {
        this.listener = listener;
    }

    /**
     * Removes listener and replaces it with default
     */
    public void removeListener()
    {
        setListener(new DefaultOnDialogListener());
    }

    /**
     * @param title the title to set
     */
    public void setDialogTitle(String title)
    {
        this.dialogTitle = title;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * @param okText the text on the confirm button
     */
    public void setOkText(String okText)
    {
        this.okText = okText;
    }

    /**
     * @param cancelText Text of cancel button
     */
    public void setCancelText(String cancelText)
    {
        this.cancelText = cancelText;
    }

    /////////////////--NESTED CLASSES--/////////////////////////////////

    /**
     * Callbacks for dialog option.
     */
    public interface OnDialogListener
    {
        /**
         * Called if the user confirms the dialog option.
         */
        public void onConfirm();

        /**
         * Called if the user declines the dialog option.
         */
        public void onDecline();
    }

    private class DefaultOnDialogListener implements OnDialogListener
    {

        @Override
        public void onConfirm()
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDecline()
        {
            // TODO Auto-generated method stub

        }

    }

}

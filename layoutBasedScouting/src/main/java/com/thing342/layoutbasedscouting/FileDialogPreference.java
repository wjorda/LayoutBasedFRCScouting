package com.thing342.layoutbasedscouting;

import android.app.Dialog;
import android.content.Context;
import android.preference.Preference;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import java.io.File;

import ar.com.daidalos.afiledialog.FileChooserDialog;

public class FileDialogPreference extends Preference
{

    private String filter;
    private String dialogTitle;
    private String toastText;
    private int toastLength = Toast.LENGTH_LONG;
    private OnFileSelectedListener listener;

    public FileDialogPreference(Context context)
    {
        super(context);
        setOnFileSelectedListener(new DefaultOnFileSelectedListener());
    }

    @Override
    public void onClick()
    {
        super.onClick();

        FileChooserDialog dialog =
                new FileChooserDialog(new ContextThemeWrapper(getContext(),
                        R.style.Theme_AppCompat_Light_Dialog));
        if (filter != null) dialog.setFilter(filter);
        if (dialogTitle != null) dialog.setTitle(dialogTitle);
        dialog.show();

        dialog.addListener(new FileChooserDialog.OnFileSelectedListener()
        {
            public void onFileSelected(Dialog source, File file)
            {
                source.hide();
                if (toastText != null)
                    Toast.makeText(source.getContext(), toastText, toastLength).show();
                listener.onFileSelected(file);
                //getContext().g.createFromFile(file);
                //((BaseAdapter) getListView().getAdapter()).notifyDataSetChanged();
            }

            public void onFileSelected(Dialog source, File folder, String name)
            {
                source.hide();
            }
        });
    }

    public FileDialogPreference setOnFileSelectedListener(OnFileSelectedListener listener)
    {
        this.listener = listener;
        return this;
    }

    public void removeOnFileSelectedListener()
    {
        setOnFileSelectedListener(new DefaultOnFileSelectedListener());
    }

    /**
     * @param filter the filter to set
     */
    public FileDialogPreference setFilter(String filter)
    {
        this.filter = filter;
        return this;
    }

    /**
     * @param title the title to set
     * @return
     */
    public FileDialogPreference setDialogTitle(String title)
    {
        this.dialogTitle = title;
        return this;
    }

    /**
     * @param toastText Text to display in toast when file is selected. (Toast does not show if null)
     * @return
     */
    public FileDialogPreference setToastText(String toastText)
    {
        this.toastText = toastText;
        return this;
    }

    /**
     * @param toastLength the toastLength to set
     * @return
     */
    public FileDialogPreference setToastLength(int toastLength)
    {
        this.toastLength = toastLength;
        return this;
    }

    public interface OnFileSelectedListener
    {
        public void onFileSelected(File file);
    }

    protected class DefaultOnFileSelectedListener implements OnFileSelectedListener
    {

        @Override
        public void onFileSelected(File file)
        {
            // do nothing

        }

    }


}

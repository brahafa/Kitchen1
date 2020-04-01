package com.dalpak.bringit.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.dalpak.bringit.R;

public class DialogWarningRemoveStock extends DialogFragment {
    /**
     * Create a new instance of DialogWarningRemoveStock, providing "num"
     * as an argument.
     */
    public static DialogWarningRemoveStock newInstance(String itemList) {
        DialogWarningRemoveStock f = new DialogWarningRemoveStock();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("itemList", itemList);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  mNum = getArguments().getInt("num");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;

        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_warning_remove_stock, container, false);
        TextView remove_list_TV = v.findViewById(R.id.remove_list_TV);
        if(getArguments() != null)
        remove_list_TV.setText(getArguments().getString("itemList", "אשימה ריקה"));


        return v;
    }


}
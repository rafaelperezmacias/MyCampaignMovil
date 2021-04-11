package com.rld.futuro.futuroapp.Utils;

import com.google.android.material.textfield.TextInputLayout;

public class TextInputLayoutUtils {

    public static boolean isValid(TextInputLayout lyt, String error) {
        if ( lyt.getEditText().getText().length() == 0 ) {
            lyt.setError(error);
            return false;
        }
        lyt.setError(null);
        return true;
    }
}

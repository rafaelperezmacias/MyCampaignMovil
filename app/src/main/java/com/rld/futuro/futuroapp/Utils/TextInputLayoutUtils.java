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

    public static boolean isValisMayus(TextInputLayout lyt, String error) {
        if ( lyt.getEditText().getText().length() == 0  ) {
            return false;
        }
        if ( !lyt.getEditText().getText().toString().matches("([A-Z]|[a-z]|[ ñÑ(),.-]|[0-9]|[\\n\\r]|[Á-Úá-ú])*") ) {
            lyt.setError(error);
            return false;
        }
        lyt.setError(null);
        return true;
    }

    public static boolean isVali40(TextInputLayout lyt, String error) {
        if ( lyt.getEditText().getText().length() == 0  ) {
            return false;
        }

        if ( !lyt.getEditText().getText().toString().matches("([A-Z]|[a-z]|[ ñÑ]){0,40}") ) {
            lyt.setError(error);
            return false;
        }
        lyt.setError(null);
        return true;
    }

    public static boolean isVali50(TextInputLayout lyt, String error) {
        if ( lyt.getEditText().getText().length() == 0  ) {
            return false;
        }

        if ( !lyt.getEditText().getText().toString().matches("([A-Z]|[a-z]|[ ñÑ]){0,50}") ) {
            lyt.setError(error);
            return false;
        }
        lyt.setError(null);
        return true;
    }

    public static boolean isValiKeyElector(TextInputLayout lyt, String error) {
        if ( lyt.getEditText().getText().length() == 0  ) {
            return false;
        }

        if ( !lyt.getEditText().getText().toString().matches("[A-Z]{6}[0-9]{8}[A-Z]{1}[0-9]{3}") ) {
            lyt.setError(error);
            return false;
        }
        lyt.setError(null);
        return true;
    }

    public static boolean isVali70(TextInputLayout lyt, String error) {
        if ( lyt.getEditText().getText().length() == 0  ) {
            return false;
        }

        if ( !lyt.getEditText().getText().toString().matches("([A-Z]|[a-z]|[ ñÑ]){0,70}") ) {
            lyt.setError(error);
            return false;
        }
        lyt.setError(null);
        return true;
    }

    public static boolean isVali10(TextInputLayout lyt, String error) {
        if ( lyt.getEditText().getText().length() == 0  ) {
            return false;
        }

        if ( !lyt.getEditText().getText().toString().matches("([A-Z]|[a-z]|[ ñÑ]){0,10}") ) {
            lyt.setError(error);
            return false;
        }
        lyt.setError(null);
        return true;
    }

    public static boolean isVali10_1(TextInputLayout lyt, String error) {
        if ( !lyt.getEditText().getText().toString().matches("([A-Z]|[a-z]|[ ñÑ]|[0-9]){0,10}") ) {
            lyt.setError(error);
            return false;
        }
        lyt.setError(null);
        return true;
    }

    public static boolean isValisNotes(TextInputLayout lyt, String error) {
        if ( !lyt.getEditText().getText().toString().matches("([A-Z]|[a-z]|[ ñÑ(),.-]|[=+*%$#&\"^@/_!¡'¿?]|[0-9]|[\\n\\r]|[Á-Úá-ú]){0,499}") ) {
            lyt.setError(error);
            return false;
        }
        lyt.setError(null);
        return true;
    }
}

package com.rld.app.mycampaign.utils;

import android.content.Context;
import android.text.InputFilter;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.R;

public class TextInputLayoutUtils {

    // RFC 5322 Format
    private static final String EMAIL_EXPRESSION = "^(?=.{1,64}@)(?:(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"@)|([0-9a-z](?:\\.(?!\\.)|[-!#\\$%&'\\*\\+\\/=\\?\\^`\\{\\}\\|~\\w])*@))(?=.{1,255}$)(?:(\\[(?:\\d{1,3}\\.){3}\\d{1,3}\\])|((?:(?=.{1,63}\\.)[0-9a-z][-\\w]*[0-9a-z]*\\.)+[a-z0-9][\\-a-z0-9]{0,22}[a-z0-9])|((?=.{1,63}$)[0-9a-z][-\\w]*))$";

    private static final String ELECTORAL_KEY_EXPRESSION = "[A-Z]{6}[0-9]{8}[A-Z]{1}[0-9]{3}";

    private static final String NOTES_EXPRESSION = "([A-Z]|[a-z]|[ ñÑ(),.-]|[\\\\=+*%$#&\"^@/_!¡'¿?{}:;<>\\[\\]\\|~]|[0-9]|[\\n\\r]|[Á-Úá-ú])*";

    public static boolean isNotEmpty(TextInputLayout lyt, String error, AppCompatImageView icon, Context context) {
        if ( lyt.getEditText().getText().toString().trim().isEmpty() ) {
            if ( icon != null ) {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.error));
            }
            lyt.setError(error);
            return false;
        }
        if ( icon != null ) {
            icon.setColorFilter(ContextCompat.getColor(context, R.color.black));
        }
        lyt.setError(null);
        return true;
    }

    private static boolean isValidWithRegularExpression(TextInputLayout lyt, String error, AppCompatImageView icon, Context context, String expression) {
        if ( !lyt.getEditText().getText().toString().trim().matches(expression) ) {
            if ( icon != null ) {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.error));
            }
            lyt.setError(error);
            return false;
        }
        if ( icon != null ) {
            icon.setColorFilter(ContextCompat.getColor(context, R.color.black));
        }
        lyt.setError(null);
        return true;
    }

    public static boolean isValidMayusWithNumbers(TextInputLayout lyt, AppCompatImageView icon, Context context) {
        return isValidWithRegularExpression(lyt, "Rellene el campo solo con letras y números", icon, context, "([A-Z]|[ Ñ]|[Á-Ú]|[0-9])*");
    }

    public static boolean isValidMayusWithoutNumbers(TextInputLayout lyt, AppCompatImageView icon, Context context) {
       return isValidWithRegularExpression(lyt, "Rellene el campo con solo letras mayúsculas", icon, context, "([A-Z]|[ Ñ]|[Á-Ú])*");
    }

    public static boolean isValidNumbers(TextInputLayout lyt, AppCompatImageView icon, Context context) {
        return isValidWithRegularExpression(lyt, "Rellene el campo con solo números", icon, context, "([0-9])*");
    }

    public static boolean isValidEmail(TextInputLayout lyt, AppCompatImageView icon, Context context) {
        return isValidWithRegularExpression(lyt, "Ingrese un correo electrónico válido", icon, context, EMAIL_EXPRESSION);
    }

    public static boolean isValidElectoralKey(TextInputLayout lyt, AppCompatImageView icon, Context context) {
        return isValidWithRegularExpression(lyt, "Ingrese la clave en el formato correcto", icon, context, ELECTORAL_KEY_EXPRESSION);
    }

    public static boolean isValidNotes(TextInputLayout lyt, AppCompatImageView icon, Context context) {
        return isValidWithRegularExpression(lyt, "Elimine los caracteres no permitidos", icon, context, NOTES_EXPRESSION);
    }

    public static void disableEditText(EditText editText) {
        editText.setCursorVisible(false);
        editText.setLongClickable(false);
        editText.setFocusable(false);
        editText.setClickable(false);
    }

    public static void initializeFilters(EditText editText, boolean allCaps, int maxLimit) {
        if ( !allCaps ) {
            editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLimit) });
            return;
        }
        InputFilter[] filterArray = new InputFilter[2];
        filterArray[0] = new InputFilter.AllCaps();
        filterArray[1] = new InputFilter.LengthFilter(maxLimit);
        editText.setFilters(filterArray);
    }

    public static void cursorToEnd(EditText editText) {
        editText.setSelection(editText.getText().toString().length());
    }

}


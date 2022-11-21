package com.rld.app.mycampaign.utils;

import android.content.Context;
import android.text.InputFilter;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.R;

public class TextInputLayoutUtils {

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

    public static boolean isValidMayusWithNumbers(TextInputLayout lyt, String error, AppCompatImageView icon, Context context) {
        return isValidWithRegularExpression(lyt, error, icon, context, "([A-Z]|[ Ñ]|[Á-Ú][0-9])*");
    }

    public static boolean isValidMayusWithoutNumbers(TextInputLayout lyt, String error, AppCompatImageView icon, Context context) {
       return isValidWithRegularExpression(lyt, error, icon, context, "([A-Z]|[ Ñ]|[Á-Ú])*");
    }

    public static boolean isValidNumbers(TextInputLayout lyt, String error, AppCompatImageView icon, Context context) {
        return isValidWithRegularExpression(lyt, error, icon, context, "([0-9])*");
    }

    public static void setEditableEditText(EditText editText, boolean editable) {
        editText.setCursorVisible(editable);
        editText.setLongClickable(editable);
        editText.setFocusable(editable);
        editText.setClickable(editable);
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

}


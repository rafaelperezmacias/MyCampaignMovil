package com.rld.app.mycampaign.dialogs;

import android.text.Spanned;

public class ErrorMessageDialogBuilder {

    private String title;
    private String message;
    private String error;
    private Spanned spannedError;
    private String buttonText;
    private boolean isCancelable;

    public ErrorMessageDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ErrorMessageDialogBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorMessageDialogBuilder setError(String error) {
        this.error = error;
        return this;
    }

    public ErrorMessageDialogBuilder setSpannedError(Spanned spannedError) {
        this.spannedError = spannedError;
        return this;
    }

    public ErrorMessageDialogBuilder setButtonText(String buttonText) {
        this.buttonText = buttonText;
        return this;
    }

    public ErrorMessageDialogBuilder setCancelable(boolean cancelable) {
        isCancelable = cancelable;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public Spanned getSpannedError() {
        return spannedError;
    }

    public String getButtonText() {
        return buttonText;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

}

package com.rld.app.mycampaign.dialogs;

public class MessageDialogBuilder {

    private String title;
    private String message;
    private String primaryButtonText;
    private String secondaryButtonText;

    private boolean isCancelable;

    public MessageDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public MessageDialogBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public MessageDialogBuilder setPrimaryButtonText(String primaryButtonText) {
        this.primaryButtonText = primaryButtonText;
        return this;
    }

    public MessageDialogBuilder setSecondaryButtonText(String secondaryButtonText) {
        this.secondaryButtonText = secondaryButtonText;
        return this;
    }

    public MessageDialogBuilder setCancelable(boolean cancelable) {
        isCancelable = cancelable;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getPrimaryButtonText() {
        return primaryButtonText;
    }

    public String getSecondaryButtonText() {
        return secondaryButtonText;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

}

package com.rld.app.mycampaign.dialogs;

public class ProgressDialogBuilder {

    private String title;

    private boolean isCancelable;

    public ProgressDialogBuilder()
    {

    }

    public ProgressDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ProgressDialogBuilder setCancelable(boolean cancelable) {
        isCancelable = cancelable;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

}

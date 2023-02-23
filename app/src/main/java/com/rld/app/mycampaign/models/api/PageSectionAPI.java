package com.rld.app.mycampaign.models.api;

import java.util.ArrayList;

public class PageSectionAPI {

    private int current_page;
    private ArrayList<SectionResponse> data;
    private int last_page;

    public PageSectionAPI()
    {

    }

    public PageSectionAPI(int current_page, ArrayList<SectionResponse> data, int last_page)
    {
        this.current_page = current_page;
        this.data = data;
        this.last_page = last_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public ArrayList<SectionResponse> getData() {
        return data;
    }

    public void setData(ArrayList<SectionResponse> data) {
        this.data = data;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    @Override
    public String toString() {
        return "PageSectionAPI{" +
                "current_page=" + current_page +
                ", data=" + data +
                ", last_page=" + last_page +
                '}';
    }

}

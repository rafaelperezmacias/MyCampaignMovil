package com.rld.app.mycampaign.models.api;

import java.util.ArrayList;

public class PageSectionAPI {

    private ArrayList<SectionResponse> data;
    private int totalPages;

    public PageSectionAPI()
    {

    }

    public PageSectionAPI(ArrayList<SectionResponse> data, int totalPages) {
        this.data = data;
        this.totalPages = totalPages;
    }

    public ArrayList<SectionResponse> getData() {
        return data;
    }

    public void setData(ArrayList<SectionResponse> data) {
        this.data = data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "PageSectionAPI{" +
                "data=" + data +
                ", totalPages=" + totalPages +
                '}';
    }

}

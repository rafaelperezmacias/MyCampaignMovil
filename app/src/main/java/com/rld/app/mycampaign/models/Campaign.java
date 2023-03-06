package com.rld.app.mycampaign.models;

public class Campaign {

    public static final String CAMPAIGN_PREFERENCES = "campaign-preferences";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PARTY = "party";
    public static final String DESCRIPTION = "description";
    public static final String START_DATE = "start-date";

    private int id;
    private String name;
    private String party;
    private String description;
    private String start_date;

    public Campaign()
    {

    }

    public Campaign(int id, String name, String party, String description, String start_date)
    {
        this.id = id;
        this.name = name;
        this.party = party;
        this.description = description;
        this.start_date = start_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                ", description='" + description + '\'' +
                ", start_date='" + start_date + '\'' +
                '}';
    }

}

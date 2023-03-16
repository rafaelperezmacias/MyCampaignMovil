package com.rld.app.mycampaign.models.api;

import java.io.Serializable;

public class VolunteerResponse implements Serializable {

    private String status;
    private int message;

    public VolunteerResponse()
    {

    }

    public VolunteerResponse(String status, int message)
    {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "VolunteerResponse{" +
                "status='" + status + '\'' +
                ", message=" + message +
                '}';
    }

}

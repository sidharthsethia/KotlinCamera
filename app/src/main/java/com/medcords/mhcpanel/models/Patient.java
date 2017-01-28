package com.medcords.mhcpanel.models;

/**
 * Created by sidharthsethia on 06/01/17.
 */

public class Patient {
    public String name;
    public String medcordsId;
    public String relation;
    public String phoneNumber;

    public Patient (String name, String medcordsId, String relation, String phoneNumber){
        this.name = name;
        this.medcordsId = medcordsId;
        this.relation = relation;
        this.phoneNumber = phoneNumber;
    }
}

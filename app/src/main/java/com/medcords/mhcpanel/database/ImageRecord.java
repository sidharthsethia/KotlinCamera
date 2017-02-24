package com.medcords.mhcpanel.database;

/**
 * Created by sidharthsethia on 24/02/17.
 */

public class ImageRecord {
    private int _id;
    private String _path;
    private String _batch_id;
    private String _patient_id;
    private String _doctor_name;
    private String _doc_type;
    private String _report_type;
    private String _date;
    private String _tags;
    private int _has_been_uploaded;

    public ImageRecord(int id, String path, String batch_id, String patient_id, String doctor_name, String doc_type, String report_type, String date, String tags, int has_been_uploaded) {
        _id = id;
        _path = path;
        _batch_id = batch_id;
        _patient_id = patient_id;
        _doctor_name = doctor_name;
        _doc_type = doc_type;
        _report_type = report_type;
        _date = date;
        _tags = tags;
        _has_been_uploaded = has_been_uploaded;
    }

    public ImageRecord(){

    }


    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String _path) {
        this._path = _path;
    }

    public String getPatientId() {
        return _patient_id;
    }

    public void setPatientId(String _patient_id) {
        this._patient_id = _patient_id;
    }

    public String getDoctorName() {
        return _doctor_name;
    }

    public void setDoctorName(String _doctor_name) {
        this._doctor_name = _doctor_name;
    }

    public String getDocType() {
        return _doc_type;
    }

    public void setDocType(String _doc_type) {
        this._doc_type = _doc_type;
    }

    public String getReportType() {
        return _report_type;
    }

    public void setReportType(String _report_type) {
        this._report_type = _report_type;
    }

    public String getDate() {
        return _date;
    }

    public void setDate(String _date) {
        this._date = _date;
    }

    public String getTags() {
        return _tags;
    }

    public void setTags(String _tags) {
        this._tags = _tags;
    }

    public int getHasBeenUploaded() {
        return _has_been_uploaded;
    }

    public void setHasBeenUploaded(int _has_been_uploaded) {
        this._has_been_uploaded = _has_been_uploaded;
    }

    public String getBatchId() {
        return _batch_id;
    }

    public void setBatchId(String _batch_id) {
        this._batch_id = _batch_id;
    }
}

package com.voicemanagementservice.model;

public class CallModel {

    private Long id;
    private String mobileNumber;
    private String callSID;
    private String digits;

    public CallModel() {
    }

    public CallModel(String mobileNumber, String callSID, String digits) {
        this.mobileNumber = mobileNumber;
        this.callSID = callSID;
        this.digits = digits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCallSID() {
        return callSID;
    }

    public void setCallSID(String callSID) {
        this.callSID = callSID;
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }
}

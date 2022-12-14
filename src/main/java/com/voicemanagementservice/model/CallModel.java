package com.voicemanagementservice.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
//@Entity
public class CallModel {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
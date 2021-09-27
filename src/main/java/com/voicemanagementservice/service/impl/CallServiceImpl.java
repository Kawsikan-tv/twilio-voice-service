package com.voicemanagementservice.service.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Redirect;
import com.twilio.twiml.voice.Say;
import com.voicemanagementservice.model.CallModel;
import com.voicemanagementservice.repository.CallRepository;
import com.voicemanagementservice.service.CallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Service
public class CallServiceImpl implements CallService {

//    private CallRepository callRepository;
    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);

    @Value("${accountSID}")
    private String accountSID;

    @Value("${accountAuthToken}")
    private String accountAuthToken;

    @Value("${twilioSenderNumber}")
    private String twilioSenderNumber;

//    @Autowired
//    public CallServiceImpl(CallRepository callRepository) {
//        this.callRepository = callRepository;
//    }

    @Override
    public String makeCall(CallModel callModel) {
        try {
            Twilio.init(accountSID, accountAuthToken);

            String mobileNumber = callModel.getMobileNumber();

            Call call = Call.creator(
                            new com.twilio.type.PhoneNumber(mobileNumber),
                            new com.twilio.type.PhoneNumber(twilioSenderNumber),
                            new URI("https://voice-management-service.herokuapp.com/twilio/initiate-voice"))
                    .create();

            logger.info("Call initiated Successfully to the number " + mobileNumber);

            return "Call initiated Successfully with " + " sid : " + call.getSid() +
                    "\nAccount sid " + call.getAccountSid() + "\nParent sid " + call.getParentCallSid()
                    + "\ngetTo " + call.getTo();

        } catch (Exception e) {
            logger.error("Exception in makeCall Method " + e);
            return "Call Failed";
        }
    }

    @Override
    public void initiateVoice(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String voiceMessage = "For approve, press 1. For reject, press 2.";
        TwiML twiml = new VoiceResponse.Builder()
                .gather(new Gather.Builder()
                        .numDigits(1)
                        .action("https://voice-management-service.herokuapp.com/twilio/gather")
                        .say(new Say.Builder(voiceMessage).build())
                        .build()
                )
                .redirect(new Redirect
                        .Builder("/voice").build())
                .build();

        response.setContentType("application/xml");
        try {
            response.getWriter().print(twiml.toXml());
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getUserResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        VoiceResponse.Builder builder = new VoiceResponse.Builder();

        String callSid = request.getParameter("CallSid");
        String digits = request.getParameter("Digits");
        String to = request.getParameter("To");

        if (digits != null) {
            switch (digits) {
                case "1":
                    builder.say(new Say.Builder("You selected one.").build());
                    break;
                case "2":
                    builder.say(new Say.Builder("You selected two.").build());
                    break;
                default:
                    builder.say(new Say.Builder("Sorry, this is default message.").build());
                    builder.redirect(new Redirect.Builder("/twilio/initiate-voice").build());
                    break;
            }
        } else {
            builder.redirect(new Redirect.Builder("/voice").build());
        }

        response.setContentType("application/xml");

        try {
            response.getWriter().print(builder.build().toXml());
//            CallModel callModel = new CallModel();
//            callModel.setCallSID(callSid);
//            callModel.setDigits(digits);
//            callModel.setMobileNumber(to);
//            callRepository.save(callModel);

        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }

}

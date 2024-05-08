package entities;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioSMS {

    private static String ACCOUNT_SID = "ACf8d65410d838452f825af6eb2a146004";
    private static String AUTH_TOKEN = "2e6f75c0b2bcb84257ef67a985932a5b";
    private static String SENDER_PHONE_NUMBER = "+13253123454";

    public static void initialize(String accountSid, String authToken, String senderPhoneNumber) {
        ACCOUNT_SID = accountSid;
        AUTH_TOKEN = authToken;
        SENDER_PHONE_NUMBER = senderPhoneNumber;
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendCustomMessage(String recipientPhoneNumber, String messageText) {
        if (ACCOUNT_SID == null || AUTH_TOKEN == null || SENDER_PHONE_NUMBER == null) {
            System.err.println("Twilio not initialized. Call initialize() method first.");
            return;
        }

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+" + recipientPhoneNumber),
                new com.twilio.type.PhoneNumber(SENDER_PHONE_NUMBER),
                messageText
        ).create();

        System.out.println("SMS sent: " + message.getSid());
    }
}
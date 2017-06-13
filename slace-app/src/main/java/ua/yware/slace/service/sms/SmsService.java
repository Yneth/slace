package ua.yware.slace.service.sms;

import ua.yware.slace.model.User;

public interface SmsService {

    void sendRegistrationSms(User user);

}

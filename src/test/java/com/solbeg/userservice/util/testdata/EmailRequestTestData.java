package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.entity.EmailRequest;
import com.solbeg.userservice.enums.EmailType;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;

@Data
@Builder(setterPrefix = "with")
public class EmailRequestTestData {

    @Builder.Default
    private EmailType emailType = EmailType.USER_WELCOME_EMAIL;

    @Builder.Default
    private String toEmail = EMAIL_JOURNALIST;

    @Builder.Default
    private Map<String, String> data = new HashMap<>() {{
        put("Name", "Nikolay");
    }};

    public EmailRequest getEmailRequest() {
        return EmailRequest.builder()
                .emailType(emailType)
                .toEmail(toEmail)
                .data(data)
                .build();
    }
}
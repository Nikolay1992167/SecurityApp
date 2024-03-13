package com.solbeg.userservice.dto.request;

import com.solbeg.userservice.enums.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    private EmailType emailType;

    private String toEmail;

    private Map<String, String> data;

    public Map<String, String> getData() {
        if (data == null) {
            data = new HashMap<>();
        }
        return data;
    }
}
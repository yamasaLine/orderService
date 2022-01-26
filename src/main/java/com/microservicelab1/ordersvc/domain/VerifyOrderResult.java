package com.microservicelab1.ordersvc.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize
@NoArgsConstructor
public class VerifyOrderResult {
    @Setter @Getter
    private Boolean pass;
    @Setter @Getter
    private String reason;

    public VerifyOrderResult(Boolean pass, String reason) {
        this.pass = pass;
        this.reason = reason;
    }
}

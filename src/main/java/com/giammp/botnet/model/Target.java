package com.giammp.botnet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Target {
    private String url;
    private long timeMin;
    private long timeMax;
    private long maxAttempts;
}

package com.w2m.spacecraft.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class DataNotFoundException extends RuntimeException {

    private final String code;
    private final String message;

}

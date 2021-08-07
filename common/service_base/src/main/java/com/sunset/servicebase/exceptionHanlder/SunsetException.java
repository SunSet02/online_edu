package com.sunset.servicebase.exceptionHanlder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SunsetException extends RuntimeException{
    private Integer code;
    private String msg;
}

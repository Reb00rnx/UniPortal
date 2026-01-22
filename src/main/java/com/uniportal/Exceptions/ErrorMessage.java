package com.uniportal.Exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

public record ErrorMessage(
        String message,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        Instant timestamp)
{}

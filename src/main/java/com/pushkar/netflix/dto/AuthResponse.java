package com.pushkar.netflix.dto;

import java.time.LocalDateTime;

public record AuthResponse(String email, String token, LocalDateTime registeredAt) {
}
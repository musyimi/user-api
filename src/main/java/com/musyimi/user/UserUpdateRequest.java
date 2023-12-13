package com.musyimi.user;

public record UserUpdateRequest(
        String first_name,
        String last_name,
        String phone_number,
        String email,
        String residence
) {
}

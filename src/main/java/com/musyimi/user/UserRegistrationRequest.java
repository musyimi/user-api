package com.musyimi.user;

public record UserRegistrationRequest(
      String first_name,
      String last_name,
      String phone_number,
      String email,
      String residence
) {
}

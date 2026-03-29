package com.pickup.organizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateDto {

    @NotBlank(message = "Old password is required!")
    private String oldPassword;

    @NotBlank(message = "New password cannot be empty!")
    @Size(min = 8, message = "New password must contain at least 8 characters.")
    private String newPassword;

}

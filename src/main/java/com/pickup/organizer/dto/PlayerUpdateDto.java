package com.pickup.organizer.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerUpdateDto {
    
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must only contain letters.")
    private String name;

    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name must only contain letters.")
    private String lastName;

    @Email(message = "Must be a valid email address.")
    private String email;

    @Past(message = "Must be a valid birth date.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

}

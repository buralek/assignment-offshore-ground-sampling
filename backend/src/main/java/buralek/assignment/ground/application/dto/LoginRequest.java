package buralek.assignment.ground.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Schema(description = "Request to login and receive JWT")
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @JsonCreator
    public LoginRequest(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password
    ) {
        this.username = username;
        this.password = password;
    }
}
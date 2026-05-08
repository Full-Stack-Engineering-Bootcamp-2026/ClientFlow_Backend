package com.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStaffStatusRequest {

    @NotNull
    private Boolean isActive;
}
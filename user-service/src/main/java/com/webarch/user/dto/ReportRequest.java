package com.webarch.user.dto;

import com.webarch.user.domain.ReportTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportRequest(
		@NotNull ReportTargetType targetType,
		@NotBlank String targetId,
		@NotBlank String targetLabel,
		@NotBlank String reason
) {
}

package com.webarch.user.dto;

import com.webarch.user.domain.ReportStatus;
import com.webarch.user.domain.ReportTargetType;

import java.time.Instant;

public record ReportResponse(
		Long id,
		String reporterUsername,
		ReportTargetType targetType,
		String targetId,
		String targetLabel,
		String reason,
		ReportStatus status,
		String resolutionAction,
		String resolvedBy,
		Instant createdAt,
		Instant resolvedAt
) {
}

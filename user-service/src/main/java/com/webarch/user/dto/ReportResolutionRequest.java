package com.webarch.user.dto;

import jakarta.validation.constraints.NotNull;

public record ReportResolutionRequest(
		@NotNull ReportAction action
) {
	public enum ReportAction {
		REMOVE,
		DISMISS
	}
}

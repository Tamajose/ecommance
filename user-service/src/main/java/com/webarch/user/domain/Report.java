package com.webarch.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String reporterUsername;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportTargetType targetType;

	// Username for USER reports, product id (as string) for LISTING reports.
	@Column(nullable = false)
	private String targetId;

	// Display label (username or product name) captured at report time to
	// avoid the admin dashboard needing a cross-service lookup just to render the list.
	@Column(nullable = false)
	private String targetLabel;

	@Column(nullable = false, length = 1000)
	private String reason;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportStatus status;

	private String resolutionAction;

	private String resolvedBy;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	private Instant resolvedAt;

	@PrePersist
	void onCreate() {
		this.createdAt = Instant.now();
		if (this.status == null) {
			this.status = ReportStatus.OPEN;
		}
	}
}

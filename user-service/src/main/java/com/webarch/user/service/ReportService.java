package com.webarch.user.service;

import com.webarch.user.client.ProductClient;
import com.webarch.user.domain.Report;
import com.webarch.user.domain.ReportStatus;
import com.webarch.user.domain.ReportTargetType;
import com.webarch.user.domain.User;
import com.webarch.user.dto.ReportRequest;
import com.webarch.user.dto.ReportResolutionRequest.ReportAction;
import com.webarch.user.dto.ReportResponse;
import com.webarch.user.repository.ReportRepository;
import com.webarch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final ReportRepository reportRepository;
	private final UserRepository userRepository;
	private final ProductClient productClient;

	@Transactional
	public ReportResponse createReport(String reporterUsername, ReportRequest request) {
		Report report = Report.builder()
				.reporterUsername(reporterUsername)
				.targetType(request.targetType())
				.targetId(request.targetId())
				.targetLabel(request.targetLabel())
				.reason(request.reason())
				.status(ReportStatus.OPEN)
				.build();

		return toResponse(reportRepository.save(report));
	}

	@Transactional(readOnly = true)
	public List<ReportResponse> getAllReports() {
		return reportRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional
	public ReportResponse resolveReport(Long id, ReportAction action, String adminUsername) {
		Report report = reportRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Report not found: " + id));

		if (report.getStatus() != ReportStatus.OPEN) {
			throw new IllegalStateException("Report has already been resolved");
		}

		if (action == ReportAction.REMOVE) {
			if (report.getTargetType() == ReportTargetType.USER) {
				User user = userRepository.findByUsername(report.getTargetId())
						.orElseThrow(() -> new IllegalArgumentException("Reported user not found: " + report.getTargetId()));
				user.setEnabled(false);
				userRepository.save(user);
				report.setResolutionAction("USER_DISABLED");
			} else {
				productClient.removeListing(Long.valueOf(report.getTargetId()));
				report.setResolutionAction("LISTING_REMOVED");
			}
			report.setStatus(ReportStatus.RESOLVED);
		} else {
			report.setStatus(ReportStatus.DISMISSED);
			report.setResolutionAction("DISMISSED");
		}

		report.setResolvedBy(adminUsername);
		report.setResolvedAt(Instant.now());

		return toResponse(reportRepository.save(report));
	}

	private ReportResponse toResponse(Report report) {
		return new ReportResponse(
				report.getId(),
				report.getReporterUsername(),
				report.getTargetType(),
				report.getTargetId(),
				report.getTargetLabel(),
				report.getReason(),
				report.getStatus(),
				report.getResolutionAction(),
				report.getResolvedBy(),
				report.getCreatedAt(),
				report.getResolvedAt()
		);
	}
}

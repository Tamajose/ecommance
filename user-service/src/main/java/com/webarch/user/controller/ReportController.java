package com.webarch.user.controller;

import com.webarch.user.dto.ReportRequest;
import com.webarch.user.dto.ReportResolutionRequest;
import com.webarch.user.dto.ReportResponse;
import com.webarch.user.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@PostMapping
	public ResponseEntity<ReportResponse> create(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody ReportRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(jwt.getSubject(), request));
	}

	@GetMapping
	public ResponseEntity<List<ReportResponse>> getAll() {
		return ResponseEntity.ok(reportService.getAllReports());
	}

	@PostMapping("/{id}/resolve")
	public ResponseEntity<ReportResponse> resolve(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id,
			@Valid @RequestBody ReportResolutionRequest request) {
		return ResponseEntity.ok(reportService.resolveReport(id, request.action(), jwt.getSubject()));
	}
}

import { useState } from "react";
import { createReport } from "../api/reportApi";

export default function ReportModal({ targetType, targetId, targetLabel, onClose }) {
    const [reason, setReason] = useState("");
    const [error, setError] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const [submitted, setSubmitted] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSubmitting(true);
        try {
            await createReport({ targetType, targetId, targetLabel, reason });
            setSubmitted(true);
        } catch (err) {
            setError(err.message || "Failed to submit report");
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="modal-backdrop" onClick={onClose}>
            <div className="modal" onClick={(e) => e.stopPropagation()}>
                {submitted ? (
                    <>
                        <h3>Report submitted</h3>
                        <p className="success">Thanks — an admin will review this report.</p>
                        <button className="button" onClick={onClose}>Close</button>
                    </>
                ) : (
                    <>
                        <h3>Report {targetType === "USER" ? "seller" : "listing"}</h3>
                        <p className="modal-subtitle">{targetLabel}</p>
                        {error && <p className="error">{error}</p>}
                        <form onSubmit={handleSubmit}>
                            <textarea
                                placeholder="Describe the issue..."
                                value={reason}
                                onChange={(e) => setReason(e.target.value)}
                                required
                                rows={4}
                            />
                            <div className="modal-actions">
                                <button type="button" className="button-secondary" onClick={onClose}>Cancel</button>
                                <button type="submit" className="button-danger" disabled={submitting}>
                                    {submitting ? "Submitting..." : "Submit Report"}
                                </button>
                            </div>
                        </form>
                    </>
                )}
            </div>
        </div>
    );
}

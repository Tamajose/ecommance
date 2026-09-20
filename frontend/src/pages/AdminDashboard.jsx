import { useState, useEffect, useCallback } from "react";
import { getAllOrders } from "../api/orderApi";
import { getAllPayments, processPayment, failPayment, refundPayment } from "../api/paymentApi";
import { getAllReports, resolveReport } from "../api/reportApi";
import { getAllUsers } from "../api/userApi";

const TABS = ["Orders", "Payments", "Reports", "Users"];

const PAYMENT_TRANSITIONS = {
    PENDING: [
        { label: "Mark Paid", action: processPayment, className: "button" },
        { label: "Mark Failed", action: failPayment, className: "button-danger" },
    ],
    COMPLETED: [
        { label: "Refund", action: refundPayment, className: "button-danger" },
    ],
    FAILED: [],
    REFUNDED: [],
};

export default function AdminDashboard() {
    const [tab, setTab] = useState("Orders");

    const [orders, setOrders] = useState([]);
    const [ordersLoading, setOrdersLoading] = useState(true);

    const [payments, setPayments] = useState([]);
    const [paymentsLoading, setPaymentsLoading] = useState(true);
    const [paymentError, setPaymentError] = useState(null);

    const [reports, setReports] = useState([]);
    const [reportsLoading, setReportsLoading] = useState(true);
    const [reportError, setReportError] = useState(null);

    const [users, setUsers] = useState([]);
    const [usersLoading, setUsersLoading] = useState(true);

    const loadOrders = useCallback(async () => {
        try {
            setOrders(await getAllOrders());
        } catch (err) {
            console.error("Failed to load orders:", err);
        } finally {
            setOrdersLoading(false);
        }
    }, []);

    const loadPayments = useCallback(async () => {
        try {
            setPayments(await getAllPayments());
        } catch (err) {
            console.error("Failed to load payments:", err);
        } finally {
            setPaymentsLoading(false);
        }
    }, []);

    const loadReports = useCallback(async () => {
        try {
            setReports(await getAllReports());
        } catch (err) {
            console.error("Failed to load reports:", err);
        } finally {
            setReportsLoading(false);
        }
    }, []);

    const loadUsers = useCallback(async () => {
        try {
            setUsers(await getAllUsers());
        } catch (err) {
            console.error("Failed to load users:", err);
        } finally {
            setUsersLoading(false);
        }
    }, []);

    useEffect(() => {
        loadOrders();
        loadPayments();
        loadReports();
        loadUsers();
    }, [loadOrders, loadPayments, loadReports, loadUsers]);

    const handlePaymentAction = async (id, action) => {
        setPaymentError(null);
        try {
            await action(id);
            await loadPayments();
        } catch (err) {
            setPaymentError(err.message || "Failed to update payment status");
        }
    };

    const handleReportAction = async (id, resolution) => {
        setReportError(null);
        try {
            await resolveReport(id, resolution);
            await loadReports();
        } catch (err) {
            setReportError(err.message || "Failed to resolve report");
        }
    };

    return (
        <div className="admin-dashboard">
            <h2>Admin Dashboard</h2>

            <div className="admin-tabs">
                {TABS.map((t) => (
                    <button
                        key={t}
                        className={`admin-tab ${tab === t ? "active" : ""}`}
                        onClick={() => setTab(t)}
                    >
                        {t}
                    </button>
                ))}
            </div>

            {tab === "Orders" && (
                <div className="admin-panel">
                    {ordersLoading ? (
                        <div className="loading">Loading orders...</div>
                    ) : orders.length === 0 ? (
                        <p>No orders found.</p>
                    ) : (
                        <table className="admin-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Username</th>
                                    <th>Status</th>
                                    <th>Total</th>
                                    <th>Placed</th>
                                </tr>
                            </thead>
                            <tbody>
                                {orders.map((order) => (
                                    <tr key={order.id}>
                                        <td>#{order.id}</td>
                                        <td>{order.username}</td>
                                        <td><span className={`order-status ${order.status}`}>{order.status}</span></td>
                                        <td>BDT {Number(order.totalAmount).toFixed(2)}</td>
                                        <td>{new Date(order.createdAt).toLocaleString()}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            {tab === "Payments" && (
                <div className="admin-panel">
                    {paymentError && <p className="error">{paymentError}</p>}
                    {paymentsLoading ? (
                        <div className="loading">Loading payments...</div>
                    ) : payments.length === 0 ? (
                        <p>No payments found.</p>
                    ) : (
                        <table className="admin-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Username</th>
                                    <th>Order</th>
                                    <th>Amount</th>
                                    <th>Method</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {payments.map((payment) => (
                                    <tr key={payment.paymentId}>
                                        <td>#{payment.paymentId}</td>
                                        <td>{payment.username}</td>
                                        <td>#{payment.orderId}</td>
                                        <td>BDT {Number(payment.amount).toFixed(2)}</td>
                                        <td>{payment.paymentMethod}</td>
                                        <td><span className={`order-status ${payment.paymentStatus}`}>{payment.paymentStatus}</span></td>
                                        <td className="admin-table-actions">
                                            {(PAYMENT_TRANSITIONS[payment.paymentStatus] || []).map((t) => (
                                                <button
                                                    key={t.label}
                                                    className={t.className}
                                                    onClick={() => handlePaymentAction(payment.paymentId, t.action)}
                                                >
                                                    {t.label}
                                                </button>
                                            ))}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            {tab === "Reports" && (
                <div className="admin-panel">
                    {reportError && <p className="error">{reportError}</p>}
                    {reportsLoading ? (
                        <div className="loading">Loading reports...</div>
                    ) : reports.length === 0 ? (
                        <p>No reports found.</p>
                    ) : (
                        <table className="admin-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Reporter</th>
                                    <th>Type</th>
                                    <th>Target</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {reports.map((report) => (
                                    <tr key={report.id}>
                                        <td>#{report.id}</td>
                                        <td>{report.reporterUsername}</td>
                                        <td>{report.targetType}</td>
                                        <td>{report.targetLabel}</td>
                                        <td className="report-reason">{report.reason}</td>
                                        <td>
                                            <span className={`report-status ${report.status}`}>{report.status}</span>
                                            {report.resolutionAction && report.status !== "OPEN" && (
                                                <div className="report-resolution">{report.resolutionAction}</div>
                                            )}
                                        </td>
                                        <td className="admin-table-actions">
                                            {report.status === "OPEN" ? (
                                                <>
                                                    <button
                                                        className="button-danger"
                                                        onClick={() => handleReportAction(report.id, "REMOVE")}
                                                    >
                                                        Remove {report.targetType === "USER" ? "User" : "Listing"}
                                                    </button>
                                                    <button
                                                        className="button-secondary"
                                                        onClick={() => handleReportAction(report.id, "DISMISS")}
                                                    >
                                                        Dismiss
                                                    </button>
                                                </>
                                            ) : (
                                                <span className="text-muted">Resolved</span>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            {tab === "Users" && (
                <div className="admin-panel">
                    {usersLoading ? (
                        <div className="loading">Loading users...</div>
                    ) : users.length === 0 ? (
                        <p>No users found.</p>
                    ) : (
                        <table className="admin-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Enabled</th>
                                    <th>Joined</th>
                                </tr>
                            </thead>
                            <tbody>
                                {users.map((u) => (
                                    <tr key={u.id}>
                                        <td>{u.id}</td>
                                        <td>{u.username}</td>
                                        <td>{u.email}</td>
                                        <td>{u.role}</td>
                                        <td>{u.enabled ? "Yes" : "No"}</td>
                                        <td>{new Date(u.createdAt).toLocaleDateString()}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}
        </div>
    );
}

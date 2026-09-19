import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { getMyOrders, cancelOrder } from "../api/orderApi";

export default function Orders(){
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [actionError, setActionError] = useState(null);
    const location = useLocation();
    const justPlacedOrderId = location.state?.justPlacedOrderId;

    const fetchOrders = async () => {
        try{
            const data = await getMyOrders();
            setOrders(data);
        } catch(error) {
            console.error("Failed to fetch orders:", error);
        } finally{
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, []);

    const handleCancel = async (orderId) => {
        if (!window.confirm(`Are you sure you want to cancel Order #${orderId}?`)) return;
        try {
            setActionError(null);
            await cancelOrder(orderId);
            await fetchOrders();
        } catch (err) {
            setActionError(err.message || "Failed to cancel order");
        }
    };

    if(loading)
        return <div className="loading">Loading orders...</div>;

    return(
        <div className="orders-container">
            <h2>My Orders</h2>

            {justPlacedOrderId && (
                <p className="success">Order #{justPlacedOrderId} placed successfully!</p>
            )}

            {actionError && (
                <p className="error">{actionError}</p>
            )}

            {(!orders || orders.length === 0) ? (
                <p>No orders found.</p>
            ) : (
                orders.map(order => (
                    <div key={order.id} className="order-card">
                        <div className="order-card-header">
                            <span>Order <strong>#{order.id}</strong></span>
                            <span>{new Date(order.createdAt).toLocaleDateString()}</span>
                        </div>
                        <div className="order-card-body">
                            <span className={`order-status ${order.status}`}>{order.status}</span>
                            <strong>BDT{Number(order.totalAmount).toFixed(2)}</strong>
                        </div>

                        {order.items && order.items.length > 0 && (
                            <div className="order-items-list" style={{ marginTop: "1rem", borderTop: "1px solid #eee", paddingTop: "0.5rem" }}>
                                <small><strong>Items:</strong></small>
                                <ul style={{ listStyle: "none", paddingLeft: 0, margin: "0.5rem 0" }}>
                                    {order.items.map((item, idx) => (
                                        <li key={idx} style={{ display: "flex", justifyContent: "space-between", fontSize: "0.9rem", padding: "0.25rem 0" }}>
                                            <span>{item.productName} &times; {item.quantity}</span>
                                            <span>BDT{Number(item.subtotal).toFixed(2)}</span>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}

                        {order.shippingLine && (
                            <div style={{ fontSize: "0.85rem", color: "#666", marginTop: "0.5rem" }}>
                                Deliver to: {order.recipientName} ({order.shippingLine}, {order.shippingCity}, {order.shippingPostalCode}, {order.shippingCountry})
                            </div>
                        )}

                        {(order.status === "PENDING_PAYMENT" || order.status === "PAID") && (
                            <div style={{ marginTop: "0.75rem" }}>
                                <button className="button-danger" onClick={() => handleCancel(order.id)}>
                                    Cancel Order
                                </button>
                            </div>
                        )}
                    </div>
                ))
            )}
        </div>
    );
}

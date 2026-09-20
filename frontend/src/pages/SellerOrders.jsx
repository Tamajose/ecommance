import { useState, useEffect, useCallback } from "react";
import { getSellerOrders } from "../api/orderApi";

export default function SellerOrders() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    const loadOrders = useCallback(async () => {
        try {
            setOrders(await getSellerOrders());
        } catch (error) {
            console.error("Failed to fetch outgoing orders:", error);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        loadOrders();
    }, [loadOrders]);

    if (loading) return <div className="loading">Loading outgoing orders...</div>;

    return (
        <div className="orders-container">
            <h2>Outgoing Orders</h2>

            {(!orders || orders.length === 0) ? (
                <p>No outgoing orders yet.</p>
            ) : (
                orders.map((order) => (
                    <div key={order.id} className="order-card">
                        <div className="order-card-header">
                            <span>Order <strong>#{order.id}</strong> &middot; Buyer: <strong>{order.username}</strong></span>
                            <span>{new Date(order.createdAt).toLocaleDateString()}</span>
                        </div>
                        <div className="order-card-body">
                            <span className={`order-status ${order.status}`}>{order.status}</span>
                            <strong>BDT{Number(order.totalAmount).toFixed(2)}</strong>
                        </div>

                        <div className="order-card-details">
                            {order.items && order.items.length > 0 && (
                                <div className="order-items-list">
                                    <small><strong>Items:</strong></small>
                                    <ul>
                                        {order.items.map((item, idx) => (
                                            <li key={idx}>
                                                <span>{item.productName} &times; {item.quantity}</span>
                                                <span>BDT{Number(item.subtotal).toFixed(2)}</span>
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            )}

                            {order.shippingLine && (
                                <div className="order-shipping">
                                    Deliver to: {order.recipientName} ({order.shippingLine}, {order.shippingCity}, {order.shippingPostalCode}, {order.shippingCountry})
                                </div>
                            )}
                        </div>
                    </div>
                ))
            )}
        </div>
    );
}

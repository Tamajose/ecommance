import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { getMyOrders } from "../api/orderApi";

export default function Orders(){
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const location = useLocation();
    const justPlacedOrderId = location.state?.justPlacedOrderId;

    useEffect(() => {
        fetchOrders();
    }, []);

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

    if(loading)
        return <div className="loading">Loading orders...</div>;

    return(
        <div className="orders-container">
            <h2>My Orders</h2>

            {justPlacedOrderId && (
                <p className="success">Order #{justPlacedOrderId} placed successfully!</p>
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
                    </div>
                ))
            )}
        </div>
    );
}

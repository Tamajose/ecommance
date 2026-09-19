import { useState, useEffect } from "react";
import { getMyOrders } from "../api/orderApi";

export default function Orders(){
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

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
        return <div>Loading orders...</div>;
    if(!orders || orders.length === 0)
        return <div>No orders found.</div>;

    return(
        <div className="orders-container">
            <h2>My Orders</h2>
            {orders.map(order => (
                <div key={order.id} className="order-card">
                    <h3>Order #{order.id}</h3>
                    <p>Status: {order.status}</p>
                    <p>Total: BDT{order.totalAmount}</p>
                    <p>Date: {new Date(order.createdAt).toLocaleDateString()}</p>
                </div>
            ))}
        </div>
    );
}
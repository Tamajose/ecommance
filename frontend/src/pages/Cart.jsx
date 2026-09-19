import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { checkout } from "../api/cartApi";

const PAYMENT_METHODS = [
    { value: "CASH_ON_DELIVERY", label: "Cash on delivery" },
    { value: "CARD", label: "Card" },
    { value: "MOBILE_BANKING", label: "Mobile banking" },
];

export default function Cart() {
    const { cart, updateQuantity, removeItem, clearCart } = useCart();
    const navigate = useNavigate();
    const [isCheckingOut, setIsCheckingOut] = useState(false);
    const [error, setError] = useState(null);

    const [recipientName, setRecipientName] = useState("");
    const [line, setLine] = useState("");
    const [city, setCity] = useState("");
    const [postalCode, setPostalCode] = useState("");
    const [country, setCountry] = useState("");
    const [paymentMethod, setPaymentMethod] = useState("CASH_ON_DELIVERY");

    const handleCheckout = async (e) => {
        e.preventDefault();
        setError(null);
        try {
            const checkoutData = {
                recipientName,
                shippingAddress: { line, city, postalCode, country },
                paymentMethod
            };
            const orderId = await checkout(checkoutData);
            clearCart();
            navigate("/orders", { state: { justPlacedOrderId: orderId } });
        } catch (err) {
            setError(err.message || "Failed to checkout");
        }
    };

    if (!cart || !cart.items || cart.items.length === 0) {
        return (
            <div className="cart-empty">
                Your cart is empty. <Link to="/products">Browse products</Link>
            </div>
        );
    }

    return (
        <div className="cart-container">
            <h2>Your Cart</h2>

            <div className="cart-grid">
                <div className="cart-items-panel">
                    {cart.items.map(item => (
                        <div key={item.productId} className="cart-item">
                            <div className="cart-item-info">
                                <h4>{item.productName}</h4>
                                <span>BDT{Number(item.unitPrice).toFixed(2)} each</span>
                                <div className="cart-item-controls">
                                    <input
                                        type="number" min="1" value={item.quantity}
                                        onChange={(e) => updateQuantity(item.productId, parseInt(e.target.value))}
                                    />
                                    <button className="button-danger" onClick={() => removeItem(item.productId)}>Remove</button>
                                </div>
                            </div>
                            <span className="cart-item-price">BDT{Number(item.lineTotal).toFixed(2)}</span>
                        </div>
                    ))}
                </div>

                <div className="cart-summary-panel">
                    <h3>Items: <span>{cart.totalItems}</span></h3>
                    <div className="cart-total">Total: BDT{Number(cart.totalAmount).toFixed(2)}</div>
                    {!isCheckingOut && (
                        <button className="button" onClick={() => setIsCheckingOut(true)}>Proceed to Checkout</button>
                    )}
                </div>
            </div>

            {isCheckingOut && (
                <div className="checkout-form">
                    <h3>Checkout Details</h3>
                    {error && <p className="error">{error}</p>}
                    <form onSubmit={handleCheckout}>
                        <input type="text" placeholder="Recipient Name" value={recipientName} onChange={e => setRecipientName(e.target.value)} required />
                        <input type="text" placeholder="Address Line" value={line} onChange={e => setLine(e.target.value)} required />
                        <input type="text" placeholder="City" value={city} onChange={e => setCity(e.target.value)} required />
                        <input type="text" placeholder="Postal Code" value={postalCode} onChange={e => setPostalCode(e.target.value)} required />
                        <input type="text" placeholder="Country" value={country} onChange={e => setCountry(e.target.value)} required />
                        <select value={paymentMethod} onChange={e => setPaymentMethod(e.target.value)}>
                            {PAYMENT_METHODS.map(m => (
                                <option key={m.value} value={m.value}>{m.label}</option>
                            ))}
                        </select>
                        <button className="button" type="submit">Place Order</button>
                    </form>
                </div>
            )}
        </div>
    );
}

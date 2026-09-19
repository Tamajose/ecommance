import { useState } from "react";
import { useCart } from "../context/CartContext";
import { checkout } from "../api/cartApi";
import { useNavigate } from "react-router-dom";

export default function Cart() {
    const { cart, updateQuantity, removeItem, clearCart } = useCart();
    const navigate = useNavigate();
    const [isCheckingOut, setIsCheckingOut] = useState(false);
    
    const [recipientName, setRecipientName] = useState("");
    const [line, setLine] = useState("");
    const [city, setCity] = useState("");
    const [postalCode, setPostalCode] = useState("");
    const [country, setCountry] = useState("");
    const [paymentMethod, setPaymentMethod] = useState("CASH_ON_DELIVERY");

    const handleCheckout = async (e) => {
        e.preventDefault();
        try {
            const checkoutData = {
                recipientName,
                shippingAddress: { line, city, postalCode, country },
                paymentMethod
            };
            const orderId = await checkout(checkoutData);
            clearCart();
            alert(`Order placed successfully! Order ID: ${orderId}`);
            navigate("/orders");
        } catch (error) {
            console.error("Checkout failed:", error);
            alert("Failed to checkout");
        }
    };

    if (!cart || !cart.items || cart.items.length === 0) {
        return <div className="cart-empty">Your cart is empty</div>;
    }

    return (
        <div className="cart-container">
            <h2>Your Cart</h2>
            <div className="cart-items">
                {cart.items.map(item => (
                    <div key={item.productId} className="cart-item">
                        <div className="cart-item-info">
                            <h4>{item.productName}</h4>
                            <span>BDT{Number(item.unitPrice).toFixed(2)} each</span>
                        </div>
                        <span>Quantity:
                            <input
                                type="number" min="1" value={item.quantity}
                                onChange={(e) => updateQuantity(item.productId, parseInt(e.target.value))}
                            />
                        </span>
                        <span className="cart-item-price">BDT{Number(item.lineTotal).toFixed(2)}</span>
                        <button onClick={() => removeItem(item.productId)}>Remove</button>
                    </div>
                ))}
            </div>

            <div className="cart-summary">
                <p>Total items: {cart.totalItems}</p>
                <p>Total: BDT{Number(cart.totalAmount).toFixed(2)}</p>
                <button onClick={() => setIsCheckingOut(!isCheckingOut)}>Proceed to Checkout</button>
            </div>

            {isCheckingOut && (
                <div className="checkout-form">
                    <h3>Checkout Details</h3>
                    <form onSubmit={handleCheckout}>
                        <input type="text" placeholder="Recipient Name" value={recipientName} onChange={e => setRecipientName(e.target.value)} required />
                        <input type="text" placeholder="Address Line" value={line} onChange={e => setLine(e.target.value)} required />
                        <input type="text" placeholder="City" value={city} onChange={e => setCity(e.target.value)} required />
                        <input type="text" placeholder="Postal Code" value={postalCode} onChange={e => setPostalCode(e.target.value)} required />
                        <input type="text" placeholder="Country" value={country} onChange={e => setCountry(e.target.value)} required />
                        <select value={paymentMethod} onChange={e => setPaymentMethod(e.target.value)}>
                            <option value="CASH_ON_DELIVERY">Cash on delivery</option>
                            <option value="CARD">Card</option>
                            <option value="MOBILE_BANKING">Mobile banking</option>
                        </select>
                        <button type="submit">Place Order</button>
                    </form>
                </div>
            )}
        </div>
    );
}
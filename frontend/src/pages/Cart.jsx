import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { checkout } from "../api/cartApi";
import { getProducts } from "../api/productApi";
import { getCurrentUser } from "../api/userApi";

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
    const [productImageMap, setProductImageMap] = useState({});

    const [recipientName, setRecipientName] = useState("");
    const [line, setLine] = useState("");
    const [city, setCity] = useState("");
    const [postalCode, setPostalCode] = useState("");
    const [country, setCountry] = useState("");
    const [paymentMethod, setPaymentMethod] = useState("CASH_ON_DELIVERY");

    useEffect(() => {
        async function loadImages() {
            try{
                const products = await getProducts();
                const map = {};
                products.forEach(p => {
                    if(p.id && p.imageURL){
                        map[p.id] = p.imageURL;
                    }
                });
                setProductImageMap(map);
            } catch(err) {
                console.error("Failed to load product images for cart:", err);
            }
        }
        loadImages();
    }, []);

    useEffect(() => {
        async function loadProfileAddress() {
            try {
                const profile = await getCurrentUser();
                if (profile.name) {
                    setRecipientName(prev => prev || profile.name);
                }
                if (profile.addressLine) {
                    setLine(prev => prev || profile.addressLine);
                    setCity(prev => prev || profile.addressCity || "");
                    setPostalCode(prev => prev || profile.addressPostalCode || "");
                    setCountry(prev => prev || profile.addressCountry || "");
                }
            } catch (err) {
                console.error("Failed to load saved profile address:", err);
            }
        }
        loadProfileAddress();
    }, []);

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

    const subtotal = Number(cart.totalAmount || 0);
    const tax = subtotal * 0.05;
    const shipping = subtotal > 5000 || subtotal === 0 ? 0 : 60;
    const grandTotal = subtotal + tax + shipping;

    return (
        <div className="cart-container">
            <h2>Your Shopping Cart</h2>

            <div className="cart-grid">
                <div className="cart-items-panel">
                    {cart.items.map(item => {
                        const imgUrl = productImageMap[item.productId];
                        return (
                            <div key={item.productId} className="cart-item">
                                <div className="cart-item-thumbnail">
                                    {imgUrl ? (
                                        <img src={imgUrl} alt={item.productName} />
                                    ) : (
                                        <div className="cart-item-no-image"></div>
                                    )}
                                </div>
                                <div className="cart-item-info">
                                    <h4>{item.productName}</h4>
                                    <span className="unit-price">BDT {Number(item.unitPrice).toFixed(2)} each</span>
                                    
                                    <div className="cart-item-controls">
                                        <button
                                            type="button"
                                            className="btn-qty btn-qty-minus"
                                            title="Decrease quantity"
                                            onClick={() => {
                                                if (item.quantity > 1) {
                                                    updateQuantity(item.productId, item.quantity - 1);
                                                } else {
                                                    removeItem(item.productId);
                                                }
                                            }}
                                        >
                                            −
                                        </button>
                                        <span className="qty-badge">{item.quantity}</span>
                                        <button
                                            type="button"
                                            className="btn-qty btn-qty-plus"
                                            title="Increase quantity"
                                            onClick={() => updateQuantity(item.productId, item.quantity + 1)}
                                        >
                                            +
                                        </button>
                                    </div>
                                </div>

                                <div className="cart-item-price-wrapper">
                                    <span className="cart-item-price">BDT {Number(item.lineTotal).toFixed(2)}</span>
                                </div>
                            </div>
                        );
                    })}
                </div>

                <div className="cart-summary-panel">
                    <h3>Order Summary</h3>
                    
                    <div className="summary-row">
                        <span>Items ({cart.totalItems})</span>
                        <span>BDT {subtotal.toFixed(2)}</span>
                    </div>
                    
                    <div className="summary-row">
                        <span>Subtotal</span>
                        <span>BDT {subtotal.toFixed(2)}</span>
                    </div>

                    <div className="summary-row">
                        <span>Est. Tax (5%)</span>
                        <span>BDT {tax.toFixed(2)}</span>
                    </div>

                    <div className="summary-row">
                        <span>Shipping Cost</span>
                        <span>{shipping === 0 ? <strong className="free-shipping">FREE</strong> : `BDT ${shipping.toFixed(2)}`}</span>
                    </div>

                    <hr className="summary-divider" />

                    <div className="summary-row total-row">
                        <span>Total Amount</span>
                        <span className="total-price">BDT {grandTotal.toFixed(2)}</span>
                    </div>

                    {!isCheckingOut && (
                        <button className="button button-checkout" onClick={() => setIsCheckingOut(true)}>
                            Proceed to Checkout
                        </button>
                    )}
                </div>
            </div>

            {isCheckingOut && (
                <div className="checkout-form">
                    <h3>Checkout Details</h3>
                    {error && <p className="error">{error}</p>}
                    <form onSubmit={handleCheckout}>
                        <div className="form-group full-width">
                            <label>Recipient Name</label>
                            <input
                                type="text"
                                placeholder="Full Name of recipient"
                                value={recipientName}
                                onChange={e => setRecipientName(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group full-width">
                            <label>Street Address</label>
                            <input
                                type="text"
                                placeholder="House no, Street name, Area"
                                value={line}
                                onChange={e => setLine(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>City</label>
                            <input
                                type="text"
                                placeholder="City"
                                value={city}
                                onChange={e => setCity(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Postal Code</label>
                            <input
                                type="text"
                                placeholder="Postal Code"
                                value={postalCode}
                                onChange={e => setPostalCode(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Country</label>
                            <input
                                type="text"
                                placeholder="Country"
                                value={country}
                                onChange={e => setCountry(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Payment Method</label>
                            <select value={paymentMethod} onChange={e => setPaymentMethod(e.target.value)}>
                                {PAYMENT_METHODS.map(m => (
                                    <option key={m.value} value={m.value}>{m.label}</option>
                                ))}
                            </select>
                        </div>

                        <div className="form-actions full-width">
                            <button className="button button-place-order" type="submit">
                                Place Order (BDT {grandTotal.toFixed(2)})
                            </button>
                        </div>
                    </form>
                </div>
            )}
        </div>
    );
}

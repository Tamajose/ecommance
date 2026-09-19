import { useState, useEffect, useCallback } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { getProductById } from "../api/productApi";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";

export default function ProductDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const { isAuthenticated } = useAuth();
    const { addItem } = useCart();

    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [quantity, setQuantity] = useState(1);
    const [actionMessage, setActionMessage] = useState(null);

    const loadProduct = useCallback(async () => {
        try {
            const data = await getProductById(id);
            setProduct(data);
        } catch (error) {
            console.error("Failed to fetch product details:", error);
        } finally {
            setLoading(false);
        }
    }, [id]);

    useEffect(() => {
        loadProduct();
    }, [loadProduct]);

    const handleAddToCart = async () => {
        if (!isAuthenticated) {
            navigate("/login", { state: { from: location } });
            return;
        }

        if (product) {
            try {
                setActionMessage(null);
                await addItem(product.id, quantity);
                setActionMessage({ type: "success", text: "Added to cart successfully!" });
            } catch (err) {
                setActionMessage({ type: "error", text: err.message || "Failed to add to cart" });
            }
        }
    };

    if (loading) return <div className="loading">Loading...</div>;
    if (!product) return <div className="loading">Product not found</div>;

    return (
        <div className="product-detail">
            <div className="product-image">
                {product.imageURL ? (
                    <img src={product.imageURL} alt={product.name} />
                ) : (
                    <div className="no-image">No Image</div>
                )}
            </div>

            <div className="product-info">
                <h2>{product.name}</h2>
                <p className="category">{product.category}</p>
                <p className="description">{product.description}</p>
            </div>

            <div className="buy-box">
                <p className="price">BDT{Number(product.price).toFixed(2)}</p>
                <p className="in-stock">
                    {product.stockQuantity > 0 ? `${product.stockQuantity} in stock` : "Out of stock"}
                </p>
                <input
                    type="number"
                    min="1"
                    max={product.stockQuantity}
                    value={quantity}
                    onChange={(e) => setQuantity(Number(e.target.value))}
                />
                <button className="button" onClick={handleAddToCart} disabled={product.stockQuantity === 0}>
                    Add to Cart
                </button>
                {actionMessage && (
                    <p className={actionMessage.type === "success" ? "success" : "error"}>
                        {actionMessage.text}
                    </p>
                )}
            </div>
        </div>
    );
}

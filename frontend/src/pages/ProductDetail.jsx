import { useState, useEffect, useCallback } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { getProductById } from "../api/productApi";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";
import ReportModal from "../components/ReportModal";

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
    const [reportTarget, setReportTarget] = useState(null);

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
                setActionMessage({
                    type: "success",
                    text: "Added to cart successfully!",
                });
            } catch (err) {
                setActionMessage({
                    type: "error",
                    text: err.message || "Failed to add to cart",
                });
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
                <p className="seller-line">
                    Sold by <strong>{product.sellerUsername}</strong>
                </p>

                {isAuthenticated && (
                    <div className="report-links">
                        <button
                            type="button"
                            className="link-button"
                            onClick={() => setReportTarget({
                                targetType: "LISTING",
                                targetId: product.id,
                                targetLabel: product.name,
                            })}
                        >
                            Report this listing
                        </button>
                        <button
                            type="button"
                            className="link-button"
                            onClick={() => setReportTarget({
                                targetType: "USER",
                                targetId: product.sellerUsername,
                                targetLabel: product.sellerUsername,
                            })}
                        >
                            Report seller
                        </button>
                    </div>
                )}
            </div>

            {reportTarget && (
                <ReportModal
                    targetType={reportTarget.targetType}
                    targetId={reportTarget.targetId}
                    targetLabel={reportTarget.targetLabel}
                    onClose={() => setReportTarget(null)}
                />
            )}

            <div className="buy-box">
                <p className="price">BDT{Number(product.price).toFixed(2)}</p>
                <div className="product-stock-status">
                    {product.stockQuantity > 0 ? (
                        <span className="stock-badge available">Available</span>
                    ) : (
                        <span className="stock-badge unavailable">
                            Not Available
                        </span>
                    )}
                </div>
                <input
                    type="number"
                    min="1"
                    max={product.stockQuantity}
                    value={quantity}
                    onChange={(e) => setQuantity(Number(e.target.value))}
                />
                <button
                    className="button"
                    onClick={handleAddToCart}
                    disabled={product.stockQuantity === 0}
                >
                    Add to Cart
                </button>
                {actionMessage && (
                    <p
                        className={
                            actionMessage.type === "success"
                                ? "success"
                                : "error"
                        }
                    >
                        {actionMessage.text}
                    </p>
                )}
            </div>
        </div>
    );
}

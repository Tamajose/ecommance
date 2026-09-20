import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { deleteProduct } from "../api/productApi";

export default function ProductCard({ product, onDeleted }) {
    const { isAdmin } = useAuth();

    const handleDelete = async () => {
        if (!window.confirm(`Delete "${product.name}"?`)) return;
        try {
            await deleteProduct(product.id);
            onDeleted?.(product.id);
        } catch (err) {
            alert(err.message || "Failed to delete product");
        }
    };

    return (
        <div className="product-card">
            <div className="product-image">
                {product.imageURL ? (
                    <img src={product.imageURL} alt={product.name} />
                ) : (
                    <div className="no-image">No Image</div>
                )}
            </div>

            <div className="product-info">
                <h3>{product.name}</h3>

                <p className="product-category">{product.category}</p>

                <p className="product-seller">Sold by {product.sellerUsername}</p>

                <p className="product-price">
                    BDT {Number(product.price).toFixed(2)}
                </p>

                <div className="product-stock-status">
                    {product.stockQuantity > 0 ? (
                        <span className="stock-badge available">Available</span>
                    ) : (
                        <span className="stock-badge unavailable">
                            Not Available
                        </span>
                    )}
                </div>

                <div className="product-card-actions">
                    <Link className="button" to={`/product/${product.id}`}>
                        View Product
                    </Link>
                    {isAdmin && (
                        <button type="button" className="button-danger" onClick={handleDelete}>
                            Delete
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
}

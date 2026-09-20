import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { getMyProducts, deleteProduct } from "../api/productApi";

export default function MyProducts() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        loadProducts();
    }, []);

    const loadProducts = async () => {
        try {
            const data = await getMyProducts();
            // getMyProducts includes soft-deleted (inactive) products; hide them here.
            setProducts(data.filter(p => p.active !== false));
        } catch (err) {
            setError(err.message || "Failed to load products");
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Delete this product?")) return;
        try {
            await deleteProduct(id);
            setProducts(products.filter(p => p.id !== id));
        } catch (err) {
            alert(err.message || "Failed to delete product");
        }
    };

    if (loading) return <div className="loading">Loading your products...</div>;

    return (
        <div className="my-products-container">
            <div className="my-products-header">
                <h2>My Products</h2>
                <Link to="/my-products/new" className="button">Add Product</Link>
            </div>

            {error && <p className="error">{error}</p>}

            {products.length === 0 ? (
                <p>You haven't listed any products yet.</p>
            ) : (
                <div className="my-products-list">
                    {products.map(product => (
                        <div key={product.id} className="my-product-card">
                            <div className="my-product-info">
                                <h4>{product.name}</h4>
                                <p>
                                    {product.category} &middot; BDT{Number(product.price).toFixed(2)} &middot;{" "}
                                    {product.stockQuantity > 0 ? (
                                        <span className="stock-badge available">Available</span>
                                    ) : (
                                        <span className="stock-badge unavailable">Not Available</span>
                                    )}
                                </p>
                            </div>
                            <div className="my-product-actions">
                                <Link to={`/my-products/${product.id}/edit`}>Edit</Link>
                                <button className="button-danger" onClick={() => handleDelete(product.id)}>Delete</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

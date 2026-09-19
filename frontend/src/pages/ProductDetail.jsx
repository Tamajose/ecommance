import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { getProductById } from "../api/productApi";
import { useCart } from "../context/CartContext";

export default function ProductDetail() {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [quantity, setQuantity] = useState(1);
    const { addItem } = useCart();

    useEffect(() => {
        loadProduct();
    }, [id]);

    const loadProduct = async () => {
        try {
            const data = await getProductById(id);
            setProduct(data);
        } catch (error) {
            console.error("Failed to fetch product details:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleAddToCart = () => {
        if (product) {
            addItem(product.id, quantity);
            alert("Added to cart!");
        }
    };

    if (loading) return <div>Loading...</div>;
    if (!product) return <div>Product not found</div>;

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
                <p className="price">BDT{Number(product.price).toFixed(2)}</p>
                <p className="description">{product.description}</p>
                <p>Available: {product.stockQuantity}</p>
                <input
                    type="number"
                    min="1"
                    max={product.stockQuantity}
                    value={quantity}
                    onChange={(e) => setQuantity(Number(e.target.value))}
                />
                <button onClick={handleAddToCart}>Add to Cart</button>
            </div>
        </div>
    );
}
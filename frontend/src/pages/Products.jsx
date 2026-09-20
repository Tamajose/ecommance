import { useState, useEffect } from "react";
import { getProducts } from "../api/productApi";
import ProductCard from "../components/ProductCard";

export default function Products() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadProducts();
    }, []);

    const loadProducts = async () => {
        try {
            const data = await getProducts();
            setProducts(data);
        } catch (error) {
            console.error("Failed to fetch products:", error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <div className="loading">Loading products...</div>;

    return (
        <div className="products-container">
            <h2>All Products</h2>
            <div className="products-grid">
                {products.map(product => (
                    <ProductCard
                        key={product.id}
                        product={product}
                        onDeleted={(id) => setProducts(prev => prev.filter(p => p.id !== id))}
                    />
                ))}
            </div>
        </div>
    );
}
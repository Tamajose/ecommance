import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createProduct, updateProduct, getProductById } from "../api/productApi";

const CATEGORIES = ["ELECTRONICS", "CLOTHING", "BOOKS", "HOME", "GROCERY", "SPORTS", "OTHER"];

export default function ProductForm() {
    const { id } = useParams();
    const isEditing = Boolean(id);
    const navigate = useNavigate();

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");
    const [stockQuantity, setStockQuantity] = useState("");
    const [category, setCategory] = useState(CATEGORIES[0]);
    const [imageURL, setImageURL] = useState("");
    const [loading, setLoading] = useState(isEditing);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (isEditing) {
            loadProduct();
        }
    }, [id]);

    const loadProduct = async () => {
        try {
            const product = await getProductById(id);
            setName(product.name);
            setDescription(product.description || "");
            setPrice(product.price);
            setStockQuantity(product.stockQuantity);
            setCategory(product.category);
            setImageURL(product.imageURL || "");
        } catch (err) {
            setError(err.message || "Failed to load product");
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        const payload = {
            name,
            description,
            price: Number(price),
            stockQuantity: Number(stockQuantity),
            category,
            imageURL: imageURL || null
        };

        try {
            if (isEditing) {
                await updateProduct(id, payload);
            } else {
                await createProduct(payload);
            }
            navigate("/my-products");
        } catch (err) {
            setError(err.message || "Failed to save product");
        }
    };

    if (loading) return <div className="loading">Loading...</div>;

    return (
        <div className="auth-container">
            <h2>{isEditing ? "Edit Product" : "Add Product"}</h2>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="Name" value={name} onChange={e => setName(e.target.value)} required />
                <textarea placeholder="Description" value={description} onChange={e => setDescription(e.target.value)} />
                <input type="number" step="0.01" min="0.01" placeholder="Price" value={price} onChange={e => setPrice(e.target.value)} required />
                <input type="number" min="0" placeholder="Stock Quantity" value={stockQuantity} onChange={e => setStockQuantity(e.target.value)} required />
                <select value={category} onChange={e => setCategory(e.target.value)}>
                    {CATEGORIES.map(c => <option key={c} value={c}>{c}</option>)}
                </select>
                <input type="text" placeholder="Image URL (optional)" value={imageURL} onChange={e => setImageURL(e.target.value)} />
                <button type="submit">{isEditing ? "Save Changes" : "Create Product"}</button>
            </form>
        </div>
    );
}

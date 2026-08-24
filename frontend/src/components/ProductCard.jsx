import { Link } from "react-router-dom";

export default function ProductCard({ product }){
    return(
        <div className="product-card">
            <div className="product-image">
                {product.imageURL ? (
                    <img src={product.imageURL} alt={product.name} />
                ) : (
                    <div className="no-image">
                        No Image
                    </div>
                )}
            </div>

            <div className="product-info">
                <h3>{product.name}</h3>

                <p className="product-category">
                    {product.category}
                </p>

                <p className="product-description">
                    {product.description}
                </p>

                <p className="product-price">
                    BDT{Number(product.price).toFixed(2)}
                </p>

                <p>
                    Available Stock: {product.stockQuantity}
                </p>

                <Link className="button" to={`/product/${product.id}`}>
                    View Product
                </Link>
            </div>
        </div>
    );
}
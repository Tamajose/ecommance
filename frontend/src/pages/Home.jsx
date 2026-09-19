import { Link } from "react-router-dom";

export default function Home() {
    return (
        <div className="home-container">
            <h1>Welcome to ecommance</h1>
            <p>Discover the best products at the best prices.</p>
            <Link to="/products" className="button">Shop Now</Link>
        </div>
    );
}
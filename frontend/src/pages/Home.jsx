import { Link } from "react-router-dom";

export default function Home() {
    return (
        <div className="home-container">
            <div className="hero-banner">
                <h1>Welcome to <span>ecommance</span></h1>
                <p>Discover the best products at the best prices.</p>
                <Link to="/products" className="hero-cta">Shop Now</Link>
            </div>
        </div>
    );
}

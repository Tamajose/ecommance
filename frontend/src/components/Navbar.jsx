import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";

export default function Navbar(){
    const { user, logout } = useAuth();
    const { cart } = useCart();

    return(
        <nav className="navbar">
            <div className="navbar-brand">
                <Link to="/">Ecommance</Link>
            </div>

            <div className="navbar-links">
                <Link to="/products">Products</Link>
                {user && <Link to="/cart">Cart{cart?.totalItems ? ` (${cart.totalItems})` : ""}</Link>}
                {user && <Link to="/orders">My Orders</Link>}
            </div>

            {user ? (
                <>
                    <span className="username">
                        {user.username}
                    </span>

                    <button onClick={logout}>
                        Logout
                    </button>
                </>
            ) : (
                <Link to="/login">
                    Login
                </Link>
            )}
        </nav>
    );
}

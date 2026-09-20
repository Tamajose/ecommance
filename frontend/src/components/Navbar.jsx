import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";

export default function Navbar(){
    const { user, logout, isAdmin, isSeller } = useAuth();
    const { cart } = useCart();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return(
        <nav className="navbar">
            <div className="navbar-brand">
                <Link to="/">Ecommance</Link>
            </div>

            <div className="navbar-links">
                {isAdmin ? (
                    <>
                        <Link to="/products">Products</Link>
                        <Link to="/admin">Admin Dashboard</Link>
                    </>
                ) : isSeller ? (
                    <>
                        <Link to="/my-products">My Products</Link>
                        <Link to="/outgoing-orders">Outgoing Orders</Link>
                    </>
                ) : (
                    <>
                        <Link to="/products">Products</Link>
                        {user && <Link to="/orders">My Orders</Link>}
                    </>
                )}
            </div>

            <div className="navbar-right">
                {user && (
                    <Link to="/cart" className="cart-link">
                        Cart
                        {cart?.totalItems ? <span className="cart-count">{cart.totalItems}</span> : null}
                    </Link>
                )}

                {user ? (
                    <>
                        <Link to="/profile" className="username" title="View my profile">
                            <span className="navbar-avatar">{user.username.charAt(0).toUpperCase()}</span>
                            {user.username}
                        </Link>

                        <button onClick={handleLogout}>
                            Logout
                        </button>
                    </>
                ) : (
                    <Link to="/login">
                        Login
                    </Link>
                )}
            </div>
        </nav>
    );
}

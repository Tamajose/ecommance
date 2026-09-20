import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";

export default function Navbar(){
    const { user, logout, isAdmin, isSeller } = useAuth();
    const { cart } = useCart();

    return(
        <nav className="navbar">
            <div className="navbar-brand">
                <Link to="/">Ecommance</Link>
            </div>

            <div className="navbar-links">
                {isAdmin ? (
                    <Link to="/admin">Admin Dashboard</Link>
                ) : (
                    <>
                        <Link to="/products">Products</Link>
                        {user && <Link to="/orders">My Orders</Link>}
                        {isSeller && <Link to="/my-products">My Products</Link>}
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
            </div>
        </nav>
    );
}

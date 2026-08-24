import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar(){
    const { user, logout, isAdmin } = useAuth();

    return(
        <nav className="navbar">
            <div className="navbar-brand">
                <Link to="/">Ecommance</Link>
            </div>

            <div className="navbar-links">
                <Link to="/products">Products</Link>
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
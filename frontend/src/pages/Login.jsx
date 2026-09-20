import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import GoogleButton from "../components/GoogleButton";

export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const { login } = useAuth();
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        try {
            const response = await login(username, password);
            const roleTokens = response.role ? response.role.split(" ") : [];
            const isAdmin = roleTokens.includes("ADMIN");
            const isSeller = roleTokens.includes("SELLER");
            const defaultTarget = isAdmin ? "/admin" : isSeller ? "/my-products" : "/";

            navigate(defaultTarget);
        } catch (err) {
            setError(err.message || "Failed to login");
        }
    };

    return (
        <div className="auth-container">
            <h2>Welcome back!</h2>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleSubmit}>
                <label htmlFor="login-username">Username</label>
                <input type="text" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} required />
                <label htmlFor="login-password">Password</label>
                <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} required />
                <button type="submit">Login</button>
            </form>

            <div className="auth-divider">or</div>
            <GoogleButton onError={setError} />

            <p>Don't have an account? <Link to="/register">Register here</Link></p>
        </div>
    );
}
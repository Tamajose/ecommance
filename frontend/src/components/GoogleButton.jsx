import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import GoogleIcon from "./GoogleIcon";

export default function GoogleButton({ label = "Continue with Google", onError }){
    const { loginWithGoogle } = useAuth();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    const handleClick = async () => {
        if(onError){
            onError(null);
        }
        setLoading(true);
        try{
            const { role } = await loginWithGoogle();
            const roleTokens = role ? role.split(" ") : [];
            navigate(
                roleTokens.includes("ADMIN") ? "/admin" : roleTokens.includes("SELLER") ? "/my-products" : "/"
            );
        } catch(err){
            if(onError){
                onError(err.message || "Google sign-in failed");
            }
        } finally{
            setLoading(false);
        }
    };

    return (
        <button type="button" className="google-button" onClick={handleClick} disabled={loading}>
            {loading ? <span className="auth-spinner" aria-hidden="true" /> : <GoogleIcon />}
            {loading ? "Connecting to Google…" : label}
        </button>
    );
}
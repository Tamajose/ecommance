import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { GOOGLE_POPUP_NAME } from "../api/authApi";

export default function GoogleCallback() {
    const { loginWithToken } = useAuth();
    const navigate = useNavigate();
    const handled = useRef(false);
    const [message, setMessage] = useState("Signing you in…");

    useEffect(() => {
        if(handled.current){
            return;
        }
        handled.current = true;

        const payload = new URLSearchParams(window.location.hash.slice(1));
        const token = payload.get("token");
        const username = payload.get("username");
        const role = payload.get("role");

        if(!token){
            navigate("/login?error=google", { replace: true });
            return;
        }

        loginWithToken({ token, username, role });

        const roleTokens = role ? role.split(" ") : [];
        const target = roleTokens.includes("ADMIN") ? "/admin" : roleTokens.includes("SELLER") ? "/my-products" : "/";

        if(window.name !== GOOGLE_POPUP_NAME){
            navigate(target, { replace: true });
            return;
        }

        try{
            if(window.opener && !window.opener.closed){
                window.opener.postMessage(
                    { type: "ecommance:google-login", token, username, role },
                    window.location.origin
                );
            }
        } catch{
            //
        }

        setMessage("You're signed in. This window will close…");
        setTimeout(() => window.close(), 500);
    }, [loginWithToken, navigate]);

    return(
        <div className="auth-container">
            <h2>Google sign-in</h2>
            <p className="auth-subtitle">{message}</p>
        </div>
    );
}
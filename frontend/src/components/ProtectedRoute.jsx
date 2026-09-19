import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function ProtectedRoute({ children, roles }){
    const { isAuthenticated, roleTokens } = useAuth();
    const location = useLocation();

    if (!isAuthenticated) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    if (roles && !roles.some(role => roleTokens.includes(role))) {
        return <Navigate to="/" replace />;
    }

    return children;
}

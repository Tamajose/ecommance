import { createContext, useContext, useState } from "react";
import { login as loginAPI } from "../api/authApi";

const authContext = createContext(null);

export function AuthProvider({ children }){
    const [user, setUser] = useState(() => {
        const storedUser = localStorage.getItem("user");

        return storedUser ? JSON.parse(storedUser) : null;
    });

    async function login(username, password){
        const response = await loginAPI(username, password);

        const userData = {
            username: response.username,
            role: response.role
        };

        localStorage.setItem("token", response.token);
        localStorage.setItem("user", JSON.stringify(userData));

        setUser(userData);

        return response;
    }

    function logout(){
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setUser(null);
    }

    const isAdmin = user.role === "ADMIN";

    return(
        <authContext.Provider
            value={{
                user,
                login,
                logout,
                isAdmin,
                isAuthenticated: !!user,
            }}
        >
            {children}
        </authContext.Provider>
    );
}

export function useAuth(){
    return useContext(authContext);
}
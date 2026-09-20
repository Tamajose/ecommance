import { createContext, useCallback, useContext, useEffect, useRef, useState } from "react";
import { login as loginAPI, GOOGLE_POPUP_NAME, googleLoginUrl } from "../api/authApi";

const authContext = createContext(null);

const POPUP_FEATURES = "width=480,height=640,left=0,top=0,resizable=yes,scrollbars=yes";
const GOOGLE_MESSAGE_TYPE = "ecommance:google-login";

export function AuthProvider({ children }){
    const [user, setUser] = useState(() => {
        const storedUser = localStorage.getItem("user");

        return storedUser ? JSON.parse(storedUser) : null;
    });

    const pendingGoogle = useRef(null);

    const saveSession = useCallback(( { token, username, role }) => {
        const userData = { username, role };

        localStorage.setItem("token", token);
        localStorage.setItem("user", JSON.stringify(userData));

        setUser(userData);
        return userData;
    }, []);

    const finishGoogleLogin = useCallback(( payload ) => {
        const pending = pendingGoogle.current;
        if(!pending)
            return;

        pendingGoogle.current = null;
        clearInterval(pending.timer);
        pending.resolve(saveSession(payload));
    }, [saveSession]);

    useEffect(() => {
        function handleMessage(event){
            if(event.origin !== window.location.origin)
                return;

            const payload = event.data;
            if(!payload || payload.type !== GOOGLE_MESSAGE_TYPE || !payload.token)
                return;

            finishGoogleLogin(payload);
        }

        function handleStorage(event){
            if(event.key !== "user" || !event.newValue)
                return;
            
            try{
                setUser(JSON.parse(event.newValue));
            } catch {
                //
            }
        }

        window.addEventListener("message", handleMessage);
        window.addEventListener("storage", handleStorage);

        return () => {
            window.removeEventListener("message", handleMessage);
            window.removeEventListener("storage", handleStorage);
        };
    }, [finishGoogleLogin]);

    async function login(username, password){
        const response = await loginAPI(username, password);
        saveSession(response);
        return response;
    }

    function loginWithToken(payload){
        return saveSession(payload);
    }

    function loginWithGoogle(){
        return new Promise((resolve, reject) => {
            const popup = window.open(googleLoginUrl, GOOGLE_POPUP_NAME, POPUP_FEATURES);

            if(!popup){
                reject(new Error("Your browser blocked the Google window. Allow popups for this site and try again."));
                return;
            }

            popup.focus();

            const timer = setInterval(() => {
                let closed = false;
                try{
                    closed = popup.closed;
                } catch{
                    closed = true;
                }

                if(!closed)
                    return;

                clearInterval(timer);
                setTimeout(() => {
                    if(pendingGoogle.current){
                        pendingGoogle.current = null;
                        reject(new Error("Google sign-in was cancelled."));
                    }
                }, 800);
            }, 500);

            pendingGoogle.current = { resolve, reject, timer };
        });
    }
    
    function logout(){
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setUser(null);
    }

    // user.role is really the JWT's space-separated scope (e.g. "SELLER FACTOR_PASSWORD"),
    // not a single value, so membership must be checked token-by-token.
    const roleTokens = user?.role ? user.role.split(" ") : [];
    const isAdmin = roleTokens.includes("ADMIN");
    const isSeller = roleTokens.includes("SELLER");

    return(
        <authContext.Provider
            value={{
                user,
                login,
                loginWithToken,
                loginWithGoogle,
                logout,
                isAdmin,
                isSeller,
                roleTokens,
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
import { API_URLS, GATEWAY_URL, request } from "./apiClient";

export const GOOGLE_POPUP_NAME = "ecommance-google-login";

export const googleLoginUrl = `${GATEWAY_URL}/oauth2/authorization/google`;

export async function login(username, password){
    return request(API_URLS.user, "/api/auth/login", {
        method: "POST",
        body: JSON.stringify({
            username,
            password
        })
    });
}

export async function register(userData){
    return request(API_URLS.user, "/api/users/register", {
        method: "POST",
        body: JSON.stringify(userData)
    });
}
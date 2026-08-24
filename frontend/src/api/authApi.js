import { API_URLS, request } from "./apiClient";

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
    return request(API_URLS.user, "/api/auth/register", {
        method: "POST",
        body: JSON.stringify(userData)
    });
}
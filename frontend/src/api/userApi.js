import { API_URLS, request } from "./apiClient";

export async function getAllUsers(){
    return request(API_URLS.user, "/api/users");
}

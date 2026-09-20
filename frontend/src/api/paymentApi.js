import { API_URLS, request } from "./apiClient";

export async function getAllPayments(){
    return request(API_URLS.payment, "/api/payments");
}

export async function processPayment(id){
    return request(API_URLS.payment, `/api/payments/${id}/process`, { method: "POST" });
}

export async function failPayment(id){
    return request(API_URLS.payment, `/api/payments/${id}/fail`, { method: "POST" });
}

export async function refundPayment(id){
    return request(API_URLS.payment, `/api/payments/${id}/refund`, { method: "POST" });
}

import { API_URLS, request } from "./apiClient";

export async function createOrder(orderRequest){
    return request(API_URLS.order, "/api/orders", {
        method: "POST",
        body: JSON.stringify(orderRequest)
    });
}

export async function getMyOrders(){
    return request(API_URLS.order, "/api/orders/me");
}

export async function getAllOrders(){
    return request(API_URLS.order, "/api/orders");
}

export async function getOrderById(id){
    return request(API_URLS.order, `/api/orders/{id}`);
}

export async function updateOrderStatus(id, status){
    return request(API_URLS.order, `/api/orders/${id}/status`, {
        method: "PATCH",
        body: JSON.stringify({ status })
    });
}

export async function cancelOrder(id){
    return request(API_URLS.order, `/api/orders/${id}/cancel`, {
        method: "POST"
    });
}
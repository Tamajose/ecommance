import { API_URLS, request } from './apiClient';

export async function getCart(){
    return request(API_URLS.cart, "/api/carts");
}

export async function addItem(productId, quantity){
    return request(API_URLS.cart, "/api/carts/items", {
        method: "POST",
        body: JSON.stringify({ productId, quantity })
    });
}

export async function updateItemQuantity(productId, quantity){
    return request(API_URLS.cart, `/api/carts/items/${productId}`, {
        method: "PATCH",
        body: JSON.stringify({ quantity })
    });
}

export async function removeItem(productId) {
    return request(API_URLS.cart, `/api/carts/items/${productId}`, {
        method: "DELETE"
    });
}

export async function clearCart() {
    return request(API_URLS.cart, "/api/carts", {
        method: "DELETE"
    });
}

export async function checkout(checkoutData){
    return request(API_URLS.cart, "/api/carts/checkout", {
        method: "POST",
        body: JSON.stringify(checkoutData)
    });
}
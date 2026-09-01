import { API_URLS, request } from "./apiClient";

export async function getProducts({ name, category} = {}){
    const params = new URLSearchParams;

    if(name){
        params.append("name", name);
    }

    if(category){
        params.append("category", category);
    }

    const query = params.toString();

    return request(
        API_URLS.product,
        `/api/products${query ? `?${query}` : ""}`
    );
}

export async function getProductById(id){
    return request(API_URLS.product, `/api/products/${id}`);
}

export async function createProduct(product){
    return request(API_URLS.product, "/api/products", {
        method: "POST",
        body: JSON.stringify(product)
    });
}

export async function updateProduct(id, product){
    return request(API_URLS.product, `/api/products/${id}`, {
        method: "PATCH",
        body: JSON.stringify(product)
    });
}

export async function deleteProduct(id){
    return request(API_URLS.product, `/api/products/${id}`, {
        method: "DELETE"
    });
}
const API_URLS = {
    user: "http://localhost:8081",
    product: "http://localhost:8082",
    order: "http://localhost:8083",
    cart: "http://localhost:8084",
    payment: "http://localhost:8085"
}

async function request(baseURL, endpoint, options = {}){
    const token = localStorage.getItem("token");

    const headers = {
        "Content-Type": "application/json",
        ...API_URLS(options.headers || {}),
    }

    if(token){
        headers.Authorization = `Bearer ${token}`;
    }

    const response = await fetch (`${baseURL}${endpoint}`, {
        ...options,
        headers,
    });

    if(!response.ok){
        let message = `Request failed: ${response.status}`;

        try{
            const error = await response.json();
            message = error.message || message;
        } catch{
            // no response
        }

        throw new Error(message);
    }

    if(response.status === 204){
        return null;
    }

    return response.json();
}

export { API_URLS, request };
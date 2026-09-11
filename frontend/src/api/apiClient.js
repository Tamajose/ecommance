const GATEWAY_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

const API_URLS = {
    gateway: GATEWAY_URL,
    user: GATEWAY_URL,
    product: GATEWAY_URL,
    order: GATEWAY_URL,
    cart: GATEWAY_URL,
    payment: GATEWAY_URL
};

async function request(baseURLOrEndpoint, endpointOrOptions, maybeOptions) {
    let baseURL = GATEWAY_URL;
    let endpoint = "";
    let options = {};

    // Support both request("/api/path", options) and request(baseURL, "/api/path", options)
    if (typeof baseURLOrEndpoint === "string" && typeof endpointOrOptions === "string") {
        baseURL = baseURLOrEndpoint;
        endpoint = endpointOrOptions;
        options = maybeOptions || {};
    } else if (typeof baseURLOrEndpoint === "string") {
        endpoint = baseURLOrEndpoint;
        options = endpointOrOptions || {};
    }

    const token = localStorage.getItem("token");

    const headers = {
        "Content-Type": "application/json",
        ...(options.headers || {}),
    };

    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }

    const response = await fetch(`${baseURL}${endpoint}`, {
        ...options,
        headers,
    });

    if (!response.ok) {
        let message = `Request failed: ${response.status}`;

        try {
            const error = await response.json();
            message = error.message || message;
        } catch {
            // no response body or JSON parse failed
        }

        throw new Error(message);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

export { API_URLS, GATEWAY_URL, request };
import { API_URLS, request } from "./apiClient";

export async function createReport({ targetType, targetId, targetLabel, reason }){
    return request(API_URLS.user, "/api/reports", {
        method: "POST",
        body: JSON.stringify({ targetType, targetId: String(targetId), targetLabel, reason })
    });
}

export async function getAllReports(){
    return request(API_URLS.user, "/api/reports");
}

export async function resolveReport(id, action){
    return request(API_URLS.user, `/api/reports/${id}/resolve`, {
        method: "POST",
        body: JSON.stringify({ action })
    });
}

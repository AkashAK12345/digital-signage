/**
 * API client to route requests between real FastAPI backend and mock fallback.
 */

const LATENCY_MS = 600;
const API_BASE = "http://localhost:8000";

const isRealApi = (url: string) => {
  return url.startsWith("/devices") || url.startsWith("/media") || url.startsWith("/playlists") || url.startsWith("/schedules");
};

export const apiClient = {
  get: async <T>(url: string, mockData: T): Promise<T> => {
    if (isRealApi(url)) {
      const res = await fetch(`${API_BASE}${url}`);
      if (!res.ok) throw new Error(`GET ${url} failed`);
      return res.json();
    }
    return new Promise((resolve) => {
      setTimeout(() => resolve(mockData), LATENCY_MS);
    });
  },
  
  post: async <T>(url: string, mockData: T, body?: any): Promise<T> => {
    if (isRealApi(url)) {
      const res = await fetch(`${API_BASE}${url}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body || mockData),
      });
      if (!res.ok) throw new Error(`POST ${url} failed`);
      return res.json();
    }
    return new Promise((resolve) => {
      setTimeout(() => resolve(mockData), LATENCY_MS);
    });
  },

  put: async <T>(url: string, mockData: T, body?: any): Promise<T> => {
    if (isRealApi(url)) {
      const res = await fetch(`${API_BASE}${url}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body || mockData),
      });
      if (!res.ok) throw new Error(`PUT ${url} failed`);
      return res.json();
    }
    return new Promise((resolve) => {
      setTimeout(() => resolve(mockData), LATENCY_MS);
    });
  },

  delete: async (url: string): Promise<void> => {
    if (isRealApi(url)) {
      const res = await fetch(`${API_BASE}${url}`, { method: "DELETE" });
      if (!res.ok) throw new Error(`DELETE ${url} failed`);
      return;
    }
    return new Promise((resolve) => {
      setTimeout(() => resolve(), LATENCY_MS);
    });
  },
};

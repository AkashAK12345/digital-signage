/**
 * A mock API client to simulate asynchronous backend requests.
 * It adds a synthetic delay to all requests.
 * Later, this file will be swapped out to use standard `fetch` or `axios` 
 * configured with the FastAPI base URL.
 */

const LATENCY_MS = 600;

export const apiClient = {
  get: async <T>(_url: string, mockData: T): Promise<T> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve(mockData), LATENCY_MS);
    });
  },
  
  post: async <T>(_url: string, mockData: T): Promise<T> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve(mockData), LATENCY_MS);
    });
  },

  put: async <T>(_url: string, mockData: T): Promise<T> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve(mockData), LATENCY_MS);
    });
  },

  delete: async (_url: string): Promise<void> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve(), LATENCY_MS);
    });
  },
};

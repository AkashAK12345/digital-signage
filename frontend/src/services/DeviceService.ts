import { apiClient } from "../api/apiClient";
import { devices } from "../data/devices";
import type { Device } from "../types/device";

// Simulating an in-memory database to persist changes during the session
let mockDb = [...devices];

export const DeviceService = {
  getDevices: async (): Promise<Device[]> => {
    return apiClient.get("/devices", [...mockDb]);
  },

  createDevice: async (data: Omit<Device, "id">): Promise<Device> => {
    const newDevice: Device = {
      ...data,
      id: `TV-NEW-${Date.now()}`
    };
    mockDb.push(newDevice);
    return apiClient.post("/devices", newDevice);
  },

  updateDevice: async (id: string, data: Partial<Device>): Promise<Device> => {
    const index = mockDb.findIndex(d => d.id === id);
    if (index === -1) throw new Error("Device not found");
    mockDb[index] = { ...mockDb[index], ...data };
    return apiClient.put(`/devices/${id}`, mockDb[index]);
  },

  deleteDevice: async (id: string): Promise<void> => {
    mockDb = mockDb.filter(d => d.id !== id);
    return apiClient.delete(`/devices/${id}`);
  }
};

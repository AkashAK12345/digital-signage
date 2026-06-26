import { apiClient } from "../api/apiClient";
import { schedules } from "../data/schedules";
import type { Schedule } from "../types/schedule";

let mockDb = [...schedules];

export const ScheduleService = {
  getSchedules: async (): Promise<Schedule[]> => {
    return apiClient.get("/schedules", [...mockDb]);
  },

  createSchedule: async (data: Omit<Schedule, "id">): Promise<Schedule> => {
    const newItem: Schedule = {
      ...data,
      id: `SCH-NEW-${Date.now()}`
    };
    mockDb.unshift(newItem);
    return apiClient.post("/schedules", newItem);
  },

  updateSchedule: async (id: string, data: Partial<Schedule>): Promise<Schedule> => {
    const index = mockDb.findIndex(s => s.id === id);
    if (index === -1) throw new Error("Schedule not found");
    mockDb[index] = { ...mockDb[index], ...data };
    return apiClient.put(`/schedules/${id}`, mockDb[index]);
  },

  deleteSchedule: async (id: string): Promise<void> => {
    mockDb = mockDb.filter(s => s.id !== id);
    return apiClient.delete(`/schedules/${id}`);
  }
};

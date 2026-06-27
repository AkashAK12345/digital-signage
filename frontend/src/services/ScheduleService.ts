import { apiClient } from "../api/apiClient";
import type { Schedule } from "../types/schedule";

export const ScheduleService = {
  getSchedules: async (): Promise<Schedule[]> => {
    return apiClient.get("/schedules", []);
  },

  createSchedule: async (data: Omit<Schedule, "id">): Promise<Schedule> => {
    return apiClient.post("/schedules", {} as Schedule, data);
  },

  updateSchedule: async (id: string, data: Partial<Schedule>): Promise<Schedule> => {
    return apiClient.put(`/schedules/${id}`, {} as Schedule, data);
  },

  deleteSchedule: async (id: string): Promise<void> => {
    return apiClient.delete(`/schedules/${id}`);
  }
};

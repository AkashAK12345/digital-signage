import { apiClient } from "../api/apiClient";
import { mediaItems } from "../data/media";
import type { MediaItem } from "../types/media";

let mockDb = [...mediaItems];

export const MediaService = {
  getMedia: async (): Promise<MediaItem[]> => {
    return apiClient.get("/media", [...mockDb]);
  },

  uploadMedia: async (data: Omit<MediaItem, "id">): Promise<MediaItem> => {
    const newItem: MediaItem = {
      ...data,
      id: `MEDIA-NEW-${Date.now()}`
    };
    mockDb.unshift(newItem);
    return apiClient.post("/media/upload", newItem);
  },

  deleteMedia: async (id: string): Promise<void> => {
    mockDb = mockDb.filter(m => m.id !== id);
    return apiClient.delete(`/media/${id}`);
  }
};

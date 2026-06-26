import { apiClient } from "../api/apiClient";
import { playlists } from "../data/playlists";
import type { Playlist } from "../types/playlist";

let mockDb = [...playlists];

export const PlaylistService = {
  getPlaylists: async (): Promise<Playlist[]> => {
    return apiClient.get("/playlists", [...mockDb]);
  },

  createPlaylist: async (data: Omit<Playlist, "id">): Promise<Playlist> => {
    const newItem: Playlist = {
      ...data,
      id: `PL-NEW-${Date.now()}`
    };
    mockDb.unshift(newItem);
    return apiClient.post("/playlists", newItem);
  },

  updatePlaylist: async (id: string, data: Partial<Playlist>): Promise<Playlist> => {
    const index = mockDb.findIndex(p => p.id === id);
    if (index === -1) throw new Error("Playlist not found");
    mockDb[index] = { ...mockDb[index], ...data };
    return apiClient.put(`/playlists/${id}`, mockDb[index]);
  },

  deletePlaylist: async (id: string): Promise<void> => {
    mockDb = mockDb.filter(p => p.id !== id);
    return apiClient.delete(`/playlists/${id}`);
  }
};

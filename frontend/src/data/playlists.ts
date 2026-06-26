import type { Playlist } from "../types/playlist";

const now = Date.now();
const MINUTE = 60_000;
const HOUR = 3_600_000;

export const playlists: Playlist[] = [
  {
    id: "PL-001",
    name: "Lobby Welcome Loop",
    description: "Main loop playing in the reception area.",
    items: [
      { id: "item-1", mediaId: "MED-003", duration: 15 },
      { id: "item-2", mediaId: "MED-002", duration: 15 },
    ],
    assignedDeviceIds: ["TV-001", "TV-005"],
    totalDuration: 30,
    status: "Published",
    updatedAt: now - 2 * HOUR,
  },
  {
    id: "PL-002",
    name: "Cafeteria Lunch Specials",
    description: "Daily specials and announcements.",
    items: [
      { id: "item-3", mediaId: "MED-008", duration: 60 },
      { id: "item-4", mediaId: "MED-004", duration: 30 },
    ],
    assignedDeviceIds: ["TV-004"],
    totalDuration: 90,
    status: "Published",
    updatedAt: now - 24 * HOUR,
  },
  {
    id: "PL-003",
    name: "Holiday Promo Sequence",
    description: "Upcoming holiday promotional materials.",
    items: [
      { id: "item-5", mediaId: "MED-001", duration: 10 },
      { id: "item-6", mediaId: "MED-007", duration: 653 },
    ],
    assignedDeviceIds: [],
    totalDuration: 663,
    status: "Draft",
    updatedAt: now - 15 * MINUTE,
  },
  {
    id: "PL-004",
    name: "Store Main Display",
    description: "Product showcases for the main floor.",
    items: [
      { id: "item-7", mediaId: "MED-005", duration: 596 },
      { id: "item-8", mediaId: "MED-006", duration: 20 },
    ],
    assignedDeviceIds: ["TV-002", "TV-006", "TV-007"],
    totalDuration: 616,
    status: "Published",
    updatedAt: now - 2 * 24 * HOUR,
  },
  {
    id: "PL-005",
    name: "Fire Drill Emergency Override",
    description: "Static emergency overlay for all screens.",
    items: [
      { id: "item-9", mediaId: "MED-003", duration: 3600 },
    ],
    assignedDeviceIds: [],
    totalDuration: 3600,
    status: "Archived",
    updatedAt: now - 30 * 24 * HOUR,
  },
];

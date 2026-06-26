import type { Schedule } from "../types/schedule";

export const schedules: Schedule[] = [
  {
    id: "SCH-001",
    name: "Morning Greeting",
    playlistId: "PL-001", // Lobby Welcome Loop
    deviceIds: ["TV-001", "TV-005"],
    startDate: "2024-01-01",
    endDate: "2024-12-31",
    startTime: "08:00",
    endTime: "11:00",
    repeat: "Weekdays",
    priority: "Normal",
    status: "Active",
  },
  {
    id: "SCH-002",
    name: "Lunch Menu Special",
    playlistId: "PL-002", // Cafeteria Lunch Specials
    deviceIds: ["TV-004"],
    startDate: "2024-01-01",
    endDate: "2024-12-31",
    startTime: "11:00",
    endTime: "14:00",
    repeat: "Daily",
    priority: "Normal",
    status: "Active",
  },
  {
    id: "SCH-003",
    name: "Fire Drill Test",
    playlistId: "PL-005", // Fire Drill Emergency Override
    deviceIds: ["TV-001", "TV-002", "TV-004", "TV-005", "TV-006", "TV-007"],
    startDate: "2024-10-15",
    endDate: "2024-10-15",
    startTime: "10:00",
    endTime: "10:15",
    repeat: "Once",
    priority: "Emergency",
    status: "Draft",
  },
  {
    id: "SCH-004",
    name: "Store Promo Prime Time",
    playlistId: "PL-004", // Store Main Display
    deviceIds: ["TV-002", "TV-006", "TV-007"],
    startDate: "2024-06-01",
    endDate: "2024-08-31",
    startTime: "16:00",
    endTime: "20:00",
    repeat: "Weekends",
    priority: "High",
    status: "Paused",
  },
  {
    id: "SCH-005",
    name: "Winter Holiday Campaign",
    playlistId: "PL-003", // Holiday Promo Sequence
    deviceIds: ["TV-002", "TV-006"],
    startDate: "2023-11-20",
    endDate: "2023-12-31",
    startTime: "00:00",
    endTime: "23:59",
    repeat: "Daily",
    priority: "Normal",
    status: "Expired",
  },
];

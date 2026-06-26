export type DeviceStatus = "Online" | "Offline" | "Idle";

export interface Device {
  id: string;
  name: string;
  location: string;
  resolution: string;
  status: DeviceStatus;
  lastSeen: string;
  lastSeenMs: number;
  ipAddress?: string;
  storage?: string;
}
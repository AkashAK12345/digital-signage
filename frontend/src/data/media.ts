import type { MediaItem } from "../types/media";

const now = Date.now();
const MINUTE = 60_000;
const HOUR = 3_600_000;
const DAY = 24 * HOUR;

export const mediaItems: MediaItem[] = [
  {
    id: "MED-001",
    name: "Summer Promo Banner",
    type: "Image",
    category: "Promotion",
    thumbnail: "/media/promo-banner.svg",
    originalFile: "/media/promo-banner.svg",
    size: 2 * 1024 * 1024,
    uploadedAt: now - 2 * DAY,
    uploadedBy: "John Doe",
    dimensions: "1920x1080",
  },
  {
    id: "MED-002",
    name: "Corporate Branding Video",
    type: "Video",
    category: "Branding",
    thumbnail: "/media/branding-thumb.svg",
    originalFile: "/media/branding-thumb.svg", // Using SVG for mock since we don't have local mp4
    size: 15.5 * 1024 * 1024,
    duration: 15,
    uploadedAt: now - 5 * DAY,
    uploadedBy: "Alice Smith",
    dimensions: "1920x1080",
  },
  {
    id: "MED-003",
    name: "Fire Drill Alert",
    type: "Image",
    category: "Emergency",
    thumbnail: "/media/emergency.svg",
    originalFile: "/media/emergency.svg",
    size: 1.2 * 1024 * 1024,
    uploadedAt: now - 1 * HOUR,
    uploadedBy: "Admin",
    dimensions: "1920x1080",
  },
  {
    id: "MED-004",
    name: "Q3 Earnings Report",
    type: "Image",
    category: "Announcement",
    thumbnail: "/media/promo-banner.svg",
    originalFile: "/media/promo-banner.svg",
    size: 500 * 1024,
    uploadedAt: now - 15 * MINUTE,
    uploadedBy: "Bob Johnson",
    dimensions: "1920x1080",
  },
  {
    id: "MED-005",
    name: "Product Showcase",
    type: "Video",
    category: "Advertisement",
    thumbnail: "/media/branding-thumb.svg",
    originalFile: "/media/branding-thumb.svg",
    size: 158 * 1024 * 1024,
    duration: 596,
    uploadedAt: now - 10 * DAY,
    uploadedBy: "Marketing Team",
    dimensions: "1920x1080",
  },
  {
    id: "MED-006",
    name: "Store Layout Map",
    type: "Image",
    category: "Announcement",
    thumbnail: "/media/promo-banner.svg",
    originalFile: "/media/promo-banner.svg",
    size: 3.4 * 1024 * 1024,
    uploadedAt: now - 30 * MINUTE,
    uploadedBy: "Alice Smith",
    dimensions: "1920x1080",
  },
];

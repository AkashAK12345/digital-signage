import DashboardRoundedIcon from "@mui/icons-material/DashboardRounded";
import TvRoundedIcon from "@mui/icons-material/TvRounded";
import ImageRoundedIcon from "@mui/icons-material/ImageRounded";
import PlaylistPlayRoundedIcon from "@mui/icons-material/PlaylistPlayRounded";
import EventRoundedIcon from "@mui/icons-material/EventRounded";
import AnalyticsRoundedIcon from "@mui/icons-material/AnalyticsRounded";
import PeopleRoundedIcon from "@mui/icons-material/PeopleRounded";
import SettingsRoundedIcon from "@mui/icons-material/SettingsRounded";
import FactCheckRoundedIcon from "@mui/icons-material/FactCheckRounded";

import type { SvgIconComponent } from "@mui/icons-material";

export interface NavigationItem {
  title: string;
  path: string;
  icon: SvgIconComponent;
  section: "main" | "management";
}

export const navigationItems: NavigationItem[] = [
  {
    title: "Dashboard",
    path: "/",
    icon: DashboardRoundedIcon,
    section: "main",
  },
  {
    title: "Devices",
    path: "/devices",
    icon: TvRoundedIcon,
    section: "main",
  },
  {
    title: "Media",
    path: "/media",
    icon: ImageRoundedIcon,
    section: "main",
  },
  {
    title: "Playlists",
    path: "/playlists",
    icon: PlaylistPlayRoundedIcon,
    section: "main",
  },
  {
    title: "Schedule",
    path: "/schedule",
    icon: EventRoundedIcon,
    section: "main",
  },
  {
    title: "Analytics",
    path: "/analytics",
    icon: AnalyticsRoundedIcon,
    section: "main",
  },

  {
    title: "Users",
    path: "/users",
    icon: PeopleRoundedIcon,
    section: "management",
  },
  {
    title: "Settings",
    path: "/settings",
    icon: SettingsRoundedIcon,
    section: "management",
  },
  {
    title: "Audit Logs",
    path: "/audit",
    icon: FactCheckRoundedIcon,
    section: "management",
  },
];
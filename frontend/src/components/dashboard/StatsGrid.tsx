import Grid from "@mui/material/Grid";

import TvRoundedIcon from "@mui/icons-material/TvRounded";
import CheckCircleRoundedIcon from "@mui/icons-material/CheckCircleRounded";
import PlaylistPlayRoundedIcon from "@mui/icons-material/PlaylistPlayRounded";
import ImageRoundedIcon from "@mui/icons-material/ImageRounded";

import StatCard from "./StatCard";

export default function StatsGrid() {
  return (
    <Grid container spacing={3}>
      <Grid sx={{ width: { xs: "100%", sm: "48%", lg: "24%" } }}>
        <StatCard
          title="Total Devices"
          value={128}
          icon={TvRoundedIcon}
          color="#6C4CF1"
          trend="+12%"
          trendColor="#22C55E"
        />
      </Grid>

      <Grid sx={{ width: { xs: "100%", sm: "48%", lg: "24%" } }}>
        <StatCard
          title="Active Devices"
          value={96}
          icon={CheckCircleRoundedIcon}
          color="#22C55E"
          trend="+8%"
          trendColor="#22C55E"
        />
      </Grid>

      <Grid sx={{ width: { xs: "100%", sm: "48%", lg: "24%" } }}>
        <StatCard
          title="Playlists"
          value={42}
          icon={PlaylistPlayRoundedIcon}
          color="#F59E0B"
          trend="+5%"
          trendColor="#22C55E"
        />
      </Grid>

      <Grid sx={{ width: { xs: "100%", sm: "48%", lg: "24%" } }}>
        <StatCard
          title="Media Files"
          value={1245}
          icon={ImageRoundedIcon}
          color="#3B82F6"
          trend="+18%"
          trendColor="#22C55E"
        />
      </Grid>
    </Grid>
  );
}
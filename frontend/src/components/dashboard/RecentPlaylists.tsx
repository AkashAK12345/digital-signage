import {
  Avatar,
  Box,
  Chip,
  Divider,
  Typography,
} from "@mui/material";

import PlaylistPlayRoundedIcon from "@mui/icons-material/PlaylistPlayRounded";

import DashboardCard from "../common/DashboardCard";

const playlists = [
  {
    name: "Morning Ads",
    screens: "12 Screens",
    status: "Active",
  },
  {
    name: "Weekend Offers",
    screens: "8 Screens",
    status: "Active",
  },
  {
    name: "Festival Campaign",
    screens: "5 Screens",
    status: "Inactive",
  },
];

export default function RecentPlaylists() {
  return (
    <DashboardCard>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 3,
        }}
      >
        <Typography
          sx={{
            fontSize: 20,
            fontWeight: 700,
          }}
        >
          Recent Playlists
        </Typography>

        <Typography
          sx={{
            color: "#6C4CF1",
            fontWeight: 600,
            cursor: "pointer",
          }}
        >
          View All
        </Typography>
      </Box>

      {playlists.map((playlist, index) => (
        <Box key={playlist.name}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              py: 1.5,
            }}
          >
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                gap: 2,
              }}
            >
              <Avatar
                variant="rounded"
                sx={{
                  width: 50,
                  height: 50,
                  bgcolor: "#EEF2FF",
                  color: "#6C4CF1",
                }}
              >
                <PlaylistPlayRoundedIcon />
              </Avatar>

              <Box>
                <Typography
                  sx={{
                    fontWeight: 700,
                  }}
                >
                  {playlist.name}
                </Typography>

                <Typography
                  sx={{
                    color: "#94A3B8",
                    fontSize: 13,
                    mt: 0.3,
                  }}
                >
                  {playlist.screens}
                </Typography>
              </Box>
            </Box>

            <Chip
              label={playlist.status}
              color={playlist.status === "Active" ? "success" : "default"}
              sx={{
                borderRadius: "8px",
                fontWeight: 600,
              }}
            />
          </Box>

          {index !== playlists.length - 1 && <Divider />}
        </Box>
      ))}
    </DashboardCard>
  );
}
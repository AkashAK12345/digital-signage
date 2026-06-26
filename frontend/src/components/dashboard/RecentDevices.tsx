import {
  Avatar,
  Box,
  Chip,
  Divider,
  Typography,
} from "@mui/material";

import TvRoundedIcon from "@mui/icons-material/TvRounded";
import DashboardCard from "../common/DashboardCard";

const devices = [
  {
    name: "Lobby Display",
    location: "Reception",
    status: "Online",
  },
  {
    name: "Store TV",
    location: "Floor 1",
    status: "Online",
  },
  {
    name: "Meeting Room",
    location: "Conference",
    status: "Offline",
  },
];

export default function RecentDevices() {
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
          Recent Devices
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

      {devices.map((device, index) => (
        <Box key={device.name}>
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
                sx={{
                  bgcolor: "#EEF2FF",
                  color: "#6C4CF1",
                  width: 50,
                  height: 50,
                }}
              >
                <TvRoundedIcon />
              </Avatar>

              <Box>
                <Typography
                  sx={{
                    fontWeight: 700,
                  }}
                >
                  {device.name}
                </Typography>

                <Typography
                  sx={{
                    color: "#94A3B8",
                    fontSize: 13,
                    mt: 0.3,
                  }}
                >
                  {device.location}
                </Typography>
              </Box>
            </Box>

            <Chip
              label={device.status}
              color={device.status === "Online" ? "success" : "error"}
              sx={{
                borderRadius: "8px",
                fontWeight: 600,
              }}
            />
          </Box>

          {index !== devices.length - 1 && (
            <Divider />
          )}
        </Box>
      ))}
    </DashboardCard>
  );
}
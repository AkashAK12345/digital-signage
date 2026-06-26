import {
  Box,
  Button,
  CircularProgress,
  LinearProgress,
  Typography,
} from "@mui/material";

import DashboardCard from "../common/DashboardCard";

export default function StorageCard() {
  const used = 62;

  return (
    <DashboardCard>
      <Typography
        sx={{
          fontSize: 20,
          fontWeight: 700,
          mb: 3,
        }}
      >
        Storage Usage
      </Typography>

      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 3,
        }}
      >
        <Box
          sx={{
            position: "relative",
            display: "inline-flex",
          }}
        >
          <CircularProgress
            variant="determinate"
            value={100}
            size={160}
            thickness={4}
            sx={{
              color: "#EEF2F7",
            }}
          />

          <CircularProgress
            variant="determinate"
            value={used}
            size={160}
            thickness={4}
            sx={{
              color: "#6C4CF1",
              position: "absolute",
              left: 0,
            }}
          />

          <Box
            sx={{
              position: "absolute",
              inset: 0,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              flexDirection: "column",
            }}
          >
            <Typography
              sx={{
                fontSize: 34,
                fontWeight: 800,
              }}
            >
              {used}%
            </Typography>

            <Typography
              sx={{
                color: "#94A3B8",
              }}
            >
              Used
            </Typography>
          </Box>
        </Box>

        <Box sx={{ width: "100%" }}>
          <Typography
            sx={{
              display: "flex",
              justifyContent: "space-between",
              fontWeight: 600,
              mb: 1,
            }}
          >
            <span>Storage</span>
            <span>62.4 / 100 GB</span>
          </Typography>

          <LinearProgress
            variant="determinate"
            value={used}
            sx={{
              height: 8,
              borderRadius: 999,
              mb: 3,
              bgcolor: "#EEF2F7",
              "& .MuiLinearProgress-bar": {
                bgcolor: "#6C4CF1",
              },
            }}
          />

          <Typography
            sx={{
              display: "flex",
              justifyContent: "space-between",
              mb: 3,
            }}
          >
            <span>Media Files</span>
            <strong>1,245</strong>
          </Typography>

          <Button
            fullWidth
            variant="contained"
            sx={{
              height: 48,
              borderRadius: "14px",
              textTransform: "none",
              fontWeight: 700,
              background:
                "linear-gradient(135deg,#6C4CF1,#8B5CF6)",

              "&:hover": {
                background:
                  "linear-gradient(135deg,#5B3DF5,#7C4DFF)",
              },
            }}
          >
            Manage Storage
          </Button>
        </Box>
      </Box>
    </DashboardCard>
  );
}
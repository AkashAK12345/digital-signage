import {
  Avatar,
  Box,
  Button,
  IconButton,
  TextField,
  Typography,
} from "@mui/material";

import DownloadRoundedIcon from "@mui/icons-material/DownloadRounded";
import NotificationsNoneRoundedIcon from "@mui/icons-material/NotificationsNoneRounded";
import SearchRoundedIcon from "@mui/icons-material/SearchRounded";

export default function DashboardHeader() {
  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        flexWrap: "wrap",
        gap: 3,
        mb: 4,
      }}
    >
      <Box>
        <Typography
          sx={{
            fontSize: 32,
            fontWeight: 800,
            color: "#111827",
          }}
        >
          Dashboard
        </Typography>

        <Typography
          sx={{
            mt: 1,
            color: "#6B7280",
            fontSize: 15,
          }}
        >
          Welcome back, Akash 👋
        </Typography>
      </Box>

      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          gap: 2,
          flexWrap: "wrap",
        }}
      >
        <TextField
          size="small"
          placeholder="Search..."
          slotProps={{
            input: {
              startAdornment: <SearchRoundedIcon sx={{ mr: 1, color: "#94A3B8" }} />,
            },
          }}
          sx={{
            width: 260,
            "& .MuiOutlinedInput-root": {
              borderRadius: "14px",
              bgcolor: "#fff",
            },
          }}
        />

        <IconButton
          sx={{
            width: 46,
            height: 46,
            bgcolor: "#fff",
            border: "1px solid #E5E7EB",
          }}
        >
          <NotificationsNoneRoundedIcon />
        </IconButton>

        <Avatar
          sx={{
            width: 46,
            height: 46,
            bgcolor: "#6C4CF1",
            fontWeight: 700,
          }}
        >
          A
        </Avatar>

        <Button
          variant="contained"
          startIcon={<DownloadRoundedIcon />}
          sx={{
            height: 46,
            px: 3,
            borderRadius: "14px",
            textTransform: "none",
            fontWeight: 700,
            background: "linear-gradient(135deg,#6C4CF1,#8B5CF6)",

            "&:hover": {
              background: "linear-gradient(135deg,#5B3DF5,#7C4DFF)",
            },
          }}
        >
          Export Report
        </Button>
      </Box>
    </Box>
  );
}
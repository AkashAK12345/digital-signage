import {
  AppBar,
  Typography,
  Box,
  Avatar,
  Badge,
  IconButton,
  Paper,
} from "@mui/material";

import MenuRoundedIcon from "@mui/icons-material/MenuRounded";
import NotificationsRoundedIcon from "@mui/icons-material/NotificationsRounded";
import DarkModeRoundedIcon from "@mui/icons-material/DarkModeRounded";
import KeyboardArrowDownRoundedIcon from "@mui/icons-material/KeyboardArrowDownRounded";

import SearchBar from "./SearchBar";

export default function Navbar() {
  return (
    <AppBar
      position="sticky"
      elevation={0}
      sx={{
        bgcolor: "transparent",
        p: 2,
      }}
    >
      <Paper
        elevation={0}
        sx={{
          height: 72,
          display: "flex",
          alignItems: "center",
          px: 3,
          borderRadius: 4,
          border: "1px solid #E5E7EB",
          boxShadow: "0 8px 30px rgba(0,0,0,.05)",
        }}
      >
        <MenuRoundedIcon />

        <Typography
          sx={{
            ml: 3,
            fontWeight: 700,
            fontSize: 24,
          }}
        >
          Digital Signage CMS
        </Typography>

        <Box sx={{ flexGrow: 1 }} />

        <SearchBar />

        <Box sx={{ width: 24 }} />

        <IconButton>
          <Badge badgeContent={3} color="primary">
            <NotificationsRoundedIcon />
          </Badge>
        </IconButton>

        <IconButton sx={{ ml: 1 }}>
          <DarkModeRoundedIcon />
        </IconButton>

        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            ml: 3,
            gap: 1.5,
          }}
        >
          <Avatar
            sx={{
              bgcolor: "#6C4CF1",
            }}
          >
            A
          </Avatar>

          <Box>
            <Typography
              sx={{
                fontWeight: 700,
              }}
            >
              Akash
            </Typography>

            <Typography
              sx={{
                fontSize: 12,
                color: "#6B7280",
              }}
            >
              Administrator
            </Typography>
          </Box>

          <KeyboardArrowDownRoundedIcon />
        </Box>
      </Paper>
    </AppBar>
  );
}
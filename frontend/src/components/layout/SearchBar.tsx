import SearchRoundedIcon from "@mui/icons-material/SearchRounded";
import { InputBase, Paper } from "@mui/material";

export default function SearchBar() {
  return (
    <Paper
      elevation={0}
      sx={{
        display: "flex",
        alignItems: "center",
        px: 2,
        width: 360,
        height: 48,
        bgcolor: "#F8FAFC",
        border: "1px solid #E5E7EB",
        borderRadius: 50,

        "&:hover": {
          borderColor: "#6C4CF1",
        },
      }}
    >
      <SearchRoundedIcon
        sx={{
          color: "#94A3B8",
          mr: 1,
        }}
      />

      <InputBase
        fullWidth
        placeholder="Search devices, media..."
      />
    </Paper>
  );
}
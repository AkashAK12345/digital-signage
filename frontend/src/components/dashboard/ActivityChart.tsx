import {
  Box,
  Chip,
  Typography,
} from "@mui/material";
import { useState } from "react";

import DashboardCard from "../common/DashboardCard";

import {
  Area,
  AreaChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

const data = [
  { day: "Mon", plays: 250 },
  { day: "Tue", plays: 410 },
  { day: "Wed", plays: 320 },
  { day: "Thu", plays: 580 },
  { day: "Fri", plays: 670 },
  { day: "Sat", plays: 620 },
  { day: "Sun", plays: 810 },
];

export default function ActivityChart() {
  const [range] = useState("7D");

  return (
    <DashboardCard>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 4,
        }}
      >
        <Box>
          <Typography
            sx={{
              fontSize: 22,
              fontWeight: 700,
            }}
          >
            Playback Activity
          </Typography>

          <Typography
            sx={{
              color: "#6B7280",
              mt: 0.5,
            }}
          >
            Advertisement plays during the last week
          </Typography>
        </Box>

        <Chip
          label={range}
          sx={{
            bgcolor: "#EEF2FF",
            color: "#6C4CF1",
            fontWeight: 700,
            borderRadius: "10px",
          }}
        />
      </Box>

      <Box sx={{ height: 340 }}>
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={data}>
            <defs>
              <linearGradient id="gradientFill" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#6C4CF1" stopOpacity={0.35} />
                <stop offset="100%" stopColor="#6C4CF1" stopOpacity={0} />
              </linearGradient>
            </defs>

            <CartesianGrid
              stroke="#F1F5F9"
              vertical={false}
              strokeDasharray="3 3"
            />

            <XAxis
              dataKey="day"
              tickLine={false}
              axisLine={false}
            />

            <YAxis
              tickLine={false}
              axisLine={false}
            />

            <Tooltip
              contentStyle={{
                borderRadius: 14,
                border: "none",
                boxShadow: "0 12px 30px rgba(0,0,0,.12)",
              }}
            />

            <Area
              type="monotone"
              dataKey="plays"
              stroke="#6C4CF1"
              strokeWidth={4}
              fill="url(#gradientFill)"
            />
          </AreaChart>
        </ResponsiveContainer>
      </Box>
    </DashboardCard>
  );
}
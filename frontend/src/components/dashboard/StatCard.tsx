import { Box, Typography } from "@mui/material";
import type { SvgIconComponent } from "@mui/icons-material";

import TrendingUpRoundedIcon from "@mui/icons-material/TrendingUpRounded";

import {
  Area,
  AreaChart,
  ResponsiveContainer,
} from "recharts";

import DashboardCard from "../common/DashboardCard";

interface Props {
  title: string;
  value: number;
  color: string;
  icon: SvgIconComponent;
  trend: string;
  trendColor: string;
}

const chartData = [
  { value: 30 },
  { value: 45 },
  { value: 42 },
  { value: 70 },
  { value: 60 },
  { value: 82 },
];

export default function StatCard({
  title,
  value,
  color,
  icon: Icon,
  trend,
  trendColor,
}: Props) {
  return (
    <DashboardCard>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
        }}
      >
        <Box>
          <Box
            sx={{
              width: 58,
              height: 58,
              borderRadius: 4,
              background: color,
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              color: "#fff",
              mb: 2,
              boxShadow: `0 10px 25px ${color}40`,
            }}
          >
            <Icon sx={{ fontSize: 30 }} />
          </Box>

          <Typography
            sx={{
              color: "#64748B",
              fontSize: 14,
            }}
          >
            {title}
          </Typography>

          <Typography
            variant="h4"
            sx={{
              fontWeight: 700,
              mt: 1,
            }}
          >
            {value.toLocaleString()}
          </Typography>

          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              mt: 2,
            }}
          >
            <TrendingUpRoundedIcon
              sx={{
                color: trendColor,
                fontSize: 18,
                mr: .5,
              }}
            />

            <Typography
              sx={{
                color: trendColor,
                fontWeight: 700,
                fontSize: 13,
                mr: 1,
              }}
            >
              {trend}
            </Typography>

            <Typography
              sx={{
                color: "#94A3B8",
                fontSize: 13,
              }}
            >
              this week
            </Typography>
          </Box>
        </Box>

        <Box
          sx={{
            width: 90,
            height: 70,
          }}
        >
          <ResponsiveContainer width="100%" height="100%">
            <AreaChart data={chartData}>
              <Area
                dataKey="value"
                stroke={color}
                strokeWidth={3}
                fill={color}
                fillOpacity={0.15}
                type="monotone"
              />
            </AreaChart>
          </ResponsiveContainer>
        </Box>
      </Box>
    </DashboardCard>
  );
}
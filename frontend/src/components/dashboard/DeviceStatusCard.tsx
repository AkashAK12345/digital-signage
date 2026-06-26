import {
  Box,
  Divider,
  Typography,
} from "@mui/material";

import DashboardCard from "../common/DashboardCard";

import {
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
} from "recharts";

const data = [
  { name: "Online", value: 96, color: "#22C55E" },
  { name: "Idle", value: 18, color: "#F59E0B" },
  { name: "Offline", value: 14, color: "#EF4444" },
];

export default function DeviceStatusCard() {
  return (
    <DashboardCard>
      <Typography
        sx={{
          fontSize: 22,
          fontWeight: 700,
          mb: 3,
        }}
      >
        Device Status
      </Typography>

      <Box
        sx={{
          height: 260,
        }}
      >
        <ResponsiveContainer width="100%" height="100%">
          <PieChart>
            <Pie
              data={data}
              innerRadius={68}
              outerRadius={90}
              paddingAngle={5}
              stroke="none"
              dataKey="value"
            >
              {data.map((item) => (
                <Cell
                  key={item.name}
                  fill={item.color}
                />
              ))}
            </Pie>

            <text
              x="50%"
              y="46%"
              textAnchor="middle"
              fontSize="32"
              fontWeight="700"
            >
              128
            </text>

            <text
              x="50%"
              y="58%"
              textAnchor="middle"
              fontSize="14"
              fill="#94A3B8"
            >
              Devices
            </text>
          </PieChart>
        </ResponsiveContainer>
      </Box>

      <Divider sx={{ my: 2 }} />

      {data.map((item) => (
        <Box
          key={item.name}
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            py: 1.2,
          }}
        >
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              gap: 1.5,
            }}
          >
            <Box
              sx={{
                width: 10,
                height: 10,
                borderRadius: "50%",
                bgcolor: item.color,
              }}
            />

            <Typography
              sx={{
                color: "#475569",
                fontWeight: 500,
              }}
            >
              {item.name}
            </Typography>
          </Box>

          <Typography
            sx={{
              fontWeight: 700,
            }}
          >
            {item.value}
          </Typography>
        </Box>
      ))}
    </DashboardCard>
  );
}
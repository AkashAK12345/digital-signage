import { Typography } from "@mui/material";
import CountUp from "react-countup";

interface Props {
  value: number;
  loading?: boolean;
}

export default function AnimatedCounter({ value, loading = false }: Props) {
  if (loading) {
    return (
      <Typography
        sx={{
          fontSize: 32,
          fontWeight: 800,
          color: "#111827",
          mt: 0.5,
          letterSpacing: "-0.02em",
        }}
      >
        —
      </Typography>
    );
  }

  return (
    <Typography
      component="div"
      sx={{
        fontSize: 32,
        fontWeight: 800,
        color: "#111827",
        mt: 0.5,
        letterSpacing: "-0.02em",
        fontVariantNumeric: "tabular-nums",
      }}
    >
      <CountUp end={value} duration={1.2} separator="," preserveValue />
    </Typography>
  );
}

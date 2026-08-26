import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Static export: the backend serves the build output as plain static files at /.
  output: "export",
};

export default nextConfig;

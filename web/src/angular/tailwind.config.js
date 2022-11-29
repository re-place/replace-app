/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: "#68ac30",
          50: "#f3fbea",
          100: "#e4f5d2",
          200: "#cbebab",
          300: "#a9dd79",
          400: "#8acb50",
          500: "#68ac30",
          600: "#518c24",
          700: "#3f6c1f",
          800: "#34561e",
          900: "#2e4a1d",
        },
        secondary: "#f5f5f5",
      },
    },
  },
  plugins: [
    require("@tailwindcss/forms"),
  ],
}

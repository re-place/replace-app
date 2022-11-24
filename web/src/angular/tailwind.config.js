/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/**/*.{html,ts}',
  ],
  theme: {
    extend: {
        colors: {
            'secondary': '#f5f5f5',
        },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
  ],
}

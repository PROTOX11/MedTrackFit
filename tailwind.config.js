/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html", // Thymeleaf templates
    "./src/main/resources/static/**/*.{html,js}", // Static assets
    "./node_modules/flowbite/**/*.js", // Flowbite components
    "./frontend/src/**/*.{js,jsx,ts,tsx}" // React source files
  ],
  theme: {
    extend: {
      backgroundImage: {
        "home-bg": "url('/images/bgimg.jpg')",
        "topright": "url('/images/topright.png')",
        "why": "url('/images/why.png')",
        "how": "url('/images/how.jpg')",
      },
    },
  },
  darkMode: "class",
  plugins: [
    require("flowbite/plugin") // Add Flowbite plugin
  ],
};
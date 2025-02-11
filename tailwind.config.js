/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.{html,js}"],
  theme: {
    extend: {
      backgroundImage: {
        'home-bg': "url('/images/bg-img.jpg')",
      },
    },
  },
  plugins: [],
  darmode: "selector",
};


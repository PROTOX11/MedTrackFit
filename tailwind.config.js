/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.{html,js}"],
  theme: {
    extend: {
      backgroundImage: {
        'home-bg': "url('/images/bgimg.jpg')",
        'topright': "url('/images/topright.png')",
        'why': "url('/images/why.png')",
        'how': "url('/images/how.jpg')",
      },
    },
  },
  plugins: [],
  darmode: "selector",
};


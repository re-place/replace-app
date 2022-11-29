module.exports = {
  trailingComma: "es5",
  tabWidth: 4,
  semi: false,
  singleQuote: false,
  printWidth: 130,
  overrides: [
    {
      files: ["*.js", "*.json", "*.yml"],
      options: {
        tabWidth: 2,
      },
    },
  ],
}

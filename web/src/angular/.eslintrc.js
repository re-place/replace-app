module.exports = {
    root: true,
    ignorePatterns: [
        "projects/**/*",
        "build/**/*",
        "dist/**/*",
    ],
    overrides: [
        {
            files: [
                "./*.js",
            ],
            env: {
                node: true,
            },
            extends: [
                "eslint:recommended",
            ],
            rules: {
                "no-console": "off",
                "indent": [
                    "warn",
                    4,
                ],
                "linebreak-style": [
                    "error",
                    "unix",
                ],
                "quotes": [
                    "warn",
                    "double",
                ],
                "semi": [
                    "warn",
                    "never",
                ],
                "comma-dangle": [
                    "warn",
                    {
                        arrays: "always-multiline",
                        objects: "always-multiline",
                        imports: "always-multiline",
                        exports: "always-multiline",
                        functions: "always-multiline",
                    },
                ],
                "no-multiple-empty-lines": [
                    "error",
                    {
                        max: 2,
                        maxEOF: 1,
                    },
                ],
                "no-var": "error",
                "object-shorthand": ["error", "always", { avoidQuotes: true }],
                "prefer-const": "error",
                "quote-props": ["error", "consistent-as-needed"],
            },
        },
        {
            files: [
                "*.ts",
            ],
            env: {
                browser: true,
            },
            parserOptions: {
                project: [
                    "tsconfig.json",
                ],
                tsconfigRootDir: __dirname,
                createDefaultProgram: true,
            },
            extends: [
                "eslint:recommended",
                "plugin:import/recommended", // https://github.com/import-js/eslint-plugin-import
                "plugin:@typescript-eslint/recommended",
                "plugin:@angular-eslint/recommended",
                "plugin:@angular-eslint/template/process-inline-templates",
            ],
            settings: {
                "import/internal-regex": "^(@/)|(src/)",
            },
            rules: {
                "@angular-eslint/directive-selector": [
                    "error",
                    {
                        type: "attribute",
                        style: "camelCase",
                    },
                ],
                "@angular-eslint/component-selector": [
                    "error",
                    {
                        type: "element",
                        style: "kebab-case",
                    },
                ],
                "indent": [
                    "warn",
                    4,
                ],
                "linebreak-style": [
                    "error",
                    "unix",
                ],
                "quotes": [
                    "warn",
                    "double",
                ],
                "semi": [
                    "warn",
                    "never",
                ],
                "comma-dangle": [
                    "warn",
                    {
                        arrays: "always-multiline",
                        objects: "always-multiline",
                        imports: "always-multiline",
                        exports: "always-multiline",
                        functions: "always-multiline",
                    },
                ],

                "no-console": "warn",
                "no-debugger": "warn",
                "no-var": "error",
                "object-shorthand": ["error", "always", { avoidQuotes: true }],
                "prefer-const": "error",
                "quote-props": ["error", "consistent-as-needed"],

                "@angular-eslint/no-empty-lifecycle-method": "warn",
                "@angular-eslint/no-input-rename": "warn",
                "eol-last": [
                    "error",
                    "always",
                ],
                "no-multiple-empty-lines": [
                    "error",
                    {
                        max: 2,
                        maxEOF: 1,
                    },
                ],
                "max-len": [
                    "warn",
                    {
                        code: 130,
                        comments: 130,
                        tabWidth: 4,
                    },
                ],

                "@typescript-eslint/prefer-nullish-coalescing": "warn",
                "@typescript-eslint/prefer-readonly": "warn",
                "@typescript-eslint/prefer-string-starts-ends-with": "error",
                "@typescript-eslint/strict-boolean-expressions": "warn",

                "@typescript-eslint/ban-ts-comment": ["error", {
                    "ts-expect-error": "allow-with-description",
                    "ts-ignore": false,
                    "minimumDescriptionLength": 3,
                }],
                "@typescript-eslint/explicit-function-return-type": "off",
                "@typescript-eslint/prefer-literal-enum-member": "error",
                "@typescript-eslint/prefer-optional-chain": "warn",
                "@typescript-eslint/prefer-ts-expect-error": "error",

                "import/order": [
                    "warn",
                    {
                        "groups": [
                            ["builtin", "external"],
                            ["internal", "parent", "sibling", "index"],
                            ["object"],
                            ["type"],
                        ],
                        "newlines-between": "always",
                        "alphabetize": {
                            order: "asc",
                            caseInsensitive: true,
                        },
                    },
                ],
                "import/no-unresolved": "off",
            },
        },
        {
            files: [
                "*.html",
            ],
            extends: [
                "plugin:@angular-eslint/template/recommended",
            ],
            rules: {},
        },
    ],
}

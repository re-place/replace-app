module.exports = {
  "root": true,
  "ignorePatterns": [
    "projects/**/*",
    "build/**/*",
    "dist/**/*",
  ],
  "overrides": [
    {
      "files": [
        "*.ts"
      ],
      "parserOptions": {
        "project": [
          "tsconfig.json"
        ],
        "tsconfigRootDir": __dirname,
        "createDefaultProgram": true
      },
      "extends": [
        "plugin:@angular-eslint/recommended",
        "plugin:@angular-eslint/template/process-inline-templates"
      ],
      "rules": {
        "@angular-eslint/directive-selector": [
          "error",
          {
            "type": "attribute",
            "style": "camelCase"
          }
        ],
        "@angular-eslint/component-selector": [
          "error",
          {
            "type": "element",
            "style": "kebab-case"
          }
        ],
        "indent": [
          "warn",
          4
        ],
        "linebreak-style": [
          "error",
          "unix"
        ],
        "quotes": [
          "warn",
          "double"
        ],
        "semi": [
          "warn",
          "never"
        ],
        "comma-dangle": [
          "warn",
          {
            "arrays": "always-multiline",
            "objects": "always-multiline",
            "imports": "always-multiline",
            "exports": "always-multiline",
            "functions": "always-multiline"
          }
        ],
        "@angular-eslint/no-empty-lifecycle-method": "warn",
        "@angular-eslint/no-input-rename": "warn",
        "eol-last": [
          "error",
          "always"
        ],
        "no-multiple-empty-lines": [
          "error",
          {
            "max": 2,
            "maxEOF": 1
          }
        ],
        "max-len": [
          "warn",
          {
            "code": 130,
            "comments": 130,
            "tabWidth": 4
          }
        ],
        "sort-imports": [
          "warn",
          {
            "ignoreCase": false,
            "ignoreDeclarationSort": false,
            "ignoreMemberSort": false,
            "memberSyntaxSortOrder": [
              "none",
              "all",
              "single",
              "multiple"
            ],
            "allowSeparatedGroups": true
          }
        ]
      }
    },
    {
      "files": [
        "*.html"
      ],
      "extends": [
        "plugin:@angular-eslint/template/recommended"
      ],
      "rules": {}
    }
  ]
}

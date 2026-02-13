/* eslint-env node */
module.exports = {
  root: true,
  extends: [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/eslint-config-typescript'
  ],
  parserOptions: {
    ecmaVersion: 'latest'
  },
  rules: {
    '@typescript-eslint/no-explicit-any': 'error',
    'vue/multi-word-component-names': 'off'
  }
}

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  base: '/', // Базовый URL для деплоя (корень приложения)
  build: {
    outDir: '../src/main/resources/static', // Выходная директория для статики
    emptyOutDir: true, // Очищать папку перед сборкой
    assetsDir: 'assets', // Папка для ассетов (CSS, JS)
  },
})

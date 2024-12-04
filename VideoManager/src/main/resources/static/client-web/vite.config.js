import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'https://localhost:8443', // Адрес вашего бэкэнд-сервера
        changeOrigin: true, // Меняет Origin на адрес target
        secure: false, // Отключение проверки SSL (если HTTPS с самоподписанным сертификатом)
        rewrite: (path) => path, // Путь передается без изменений
      },
    },
  },
})

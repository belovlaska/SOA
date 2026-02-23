## Build & run React client (static build on nginx)

# Stage 1: Build React app
FROM node:18-alpine AS build
WORKDIR /app

# Копируем package.json (уже в контексте ./client)
COPY package*.json ./

# Устанавливаем зависимости
RUN npm install

# Копируем исходный код (все файлы из ./client)
COPY . .

# Собираем приложение
RUN npm run build

# Stage 2: Serve with nginx
FROM nginx:alpine

# Копируем собранное приложение
COPY --from=build /app/build /usr/share/nginx/html

# Копируем конфигурацию nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
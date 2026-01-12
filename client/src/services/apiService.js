import axios from 'axios';

const API_BASE_URL = 'https://localhost:13223/routes';

// Отключить проверку SSL для самоподписанного сертификата
const instance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: false,
  httpsAgent: {
      rejectUnauthorized: false,
  },
});

// Перехватчик для добавления токена (если нужен)
instance.interceptors.request.use(config => {
  return config;
}, error => {
  return Promise.reject(error);
});

// Перехватчик ошибок
instance.interceptors.response.use(
  response => response,
  error => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

// GET запросы
export const getRoutes = (params = {}) => {
  return instance.get('', { params });
};

export const getRouteById = (id) => {
  return instance.get(`/${id}`);
};

export const getDistinctDistances = () => {
  return instance.get('/distancedistinct');
};

export const countRoutesByDistanceLessThan = (distance) => {
  return instance.get(`/distancelt/${distance}`);
};

// POST запросы
export const createRoute = (data) => {
  return instance.post('', data);
};

// PUT запросы
export const updateRoute = (id, data) => {
  return instance.put(`/${id}`, data);
};

// DELETE запросы
export const deleteRoute = (id) => {
  return instance.delete(`/${id}`);
};

export const deleteRoutesByDistance = (distance) => {
  return instance.delete(`/distance/${distance}`);
};

export default instance;

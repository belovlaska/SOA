import axios from 'axios';

// HTTP вместо HTTPS
const API_BASE_URL = ''; // Проксируется через nginx

const instance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Перехватчик для логирования запросов
instance.interceptors.request.use(config => {
  console.log('API Request:', {
    url: config.url,
    method: config.method,
    params: config.params,
    data: config.data
  });
  return config;
}, error => {
  console.error('Request Error:', error);
  return Promise.reject(error);
});

// Перехватчик ошибок
instance.interceptors.response.use(
  response => {
    console.log('API Response:', {
      status: response.status,
      data: response.data
    });
    return response;
  },
  error => {
    console.error('API Error:', {
      status: error.response?.status,
      data: error.response?.data,
      message: error.message
    });
    return Promise.reject(error);
  }
);

// GET запросы
export const getRoutes = (params = {}) => {
  console.log('getRoutes called with params:', params);

  // Очищаем параметры от undefined и null
  const cleanParams = {};
  const paramMap = {
      'id': 'id',
      'name': 'name',
      'namelike': 'namelike',
      'distance': 'distance',
      'fromName': 'fromName',
      'fromNamelike': 'fromNamelike',
      'toName': 'toName',
      'toNamelike': 'toNamelike',
      'distance_gt': 'distancegt',
      'distance_gte': 'distancegte',
      'distance_lt': 'distancelt',
      'distance_lte': 'distancelte',
      'creationDate_before': 'creationDatebefore',
      'creationDate_after': 'creationDateafter',
  };
  Object.keys(params).forEach(key => {
      if (params[key] !== undefined && params[key] !== null && params[key] !== '') {
          const backendKey = paramMap[key] || key;

          // Числовые параметры
          if (key.includes('distance') || key === 'id') {
              const numValue = parseFloat(params[key]);
              if (!isNaN(numValue)) {
                  cleanParams[backendKey] = numValue;
              }
          }
          // Даты — преобразуем в YYYY-MM-DD формат для LocalDate.parse()
          else if (key === 'creationDate_before' || key === 'creationDate_after') {
              const date = new Date(params[key]);
              cleanParams[backendKey] = date.toISOString().split('T')[0];
          }
          // Строковые параметры
          else {
              cleanParams[backendKey] = params[key];
          }
      }
      });


  console.log('Clean params for API:', cleanParams);
  return instance.get('/routes', { params: cleanParams });
};

export const getRouteById = (id) => {
  return instance.get(`/routes/${id}`);
};

export const getDistinctDistances = () => {
  // OpenAPI: GET /routes/distance/distinct
  return instance.get('/routes/distance/distinct');
};

export const countRoutesByDistanceLessThan = (distance) => {
  // OpenAPI: GET /routes/distance/lt/{value}
  return instance.get(`/routes/distance/lt/${distance}`);
};

// POST запросы
export const createRoute = (data) => {
  return instance.post('/routes', data);
};

// PUT запросы
export const updateRoute = (id, data) => {
  return instance.put(`/routes/${id}`, data);
};

// DELETE запросы
export const deleteRoute = (id) => {
  return instance.delete(`/routes/${id}`);
};

export const deleteRoutesByDistance = (distance) => {
  return instance.delete(`/routes/distance/${distance}`);
};

export default instance;
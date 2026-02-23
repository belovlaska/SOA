import axios from 'axios';

// HTTP вместо HTTPS
const NAVIGATOR_BASE_URL = '/api/navigator';

const navigatorInstance = axios.create({
  baseURL: NAVIGATOR_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

navigatorInstance.interceptors.response.use(
  response => response,
  error => {
    console.error('Navigator API Error:', error);
    return Promise.reject(error);
  }
);

export const getNavigatorRoutes = (fromId, toId, orderBy) => {
  // OpenAPI: GET /navigator/routes/{id-from}/{id-to}/{order-by}
  return navigatorInstance.get(`/routes/${fromId}/${toId}/${orderBy}`);
};

export const addNavigatorRoute = (fromId, toId, distance) => {
  // OpenAPI: POST /navigator/route/add/{id-from}/{id-to}/{distance}
  return navigatorInstance.post(`/route/add/${fromId}/${toId}/${distance}`)
      .catch(error => {
                  if (error.response?.status === 409) {
                      console.warn('Route may have been created despite 409:', error.response.data);
                      return { data: null, status: 200 };
                  }
                  throw error;
              });
};

export default navigatorInstance;

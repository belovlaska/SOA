import axios from 'axios';

const NAVIGATOR_BASE_URL = 'https://localhost:13224/navigator';

const navigatorInstance = axios.create({
  baseURL: NAVIGATOR_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  httpsAgent: {
     rejectUnauthorized: false,
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
  return navigatorInstance.get(`/routes/${fromId}/${toId}/${orderBy}`);
};

export const addNavigatorRoute = (fromId, toId, distance) => {
  return navigatorInstance.post(`/routeadd/${fromId}/${toId}/${distance}`);
};

export default navigatorInstance;

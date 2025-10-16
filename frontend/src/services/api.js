import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add auth token to requests
api.interceptors.request.use(
  (config) => {
    console.log('API Request:', config.method?.toUpperCase(), config.url, config.data);
    const user = localStorage.getItem('user');
    if (user) {
      const userData = JSON.parse(user);
      if (userData.token) {
        config.headers.Authorization = `Bearer ${userData.token}`;
      }
    }
    return config;
  },
  (error) => {
    console.error('API Request Error:', error);
    return Promise.reject(error);
  }
);

// Add response interceptor
api.interceptors.response.use(
  (response) => {
    console.log('API Response:', response.status, response.data);
    return response;
  },
  (error) => {
    console.error('API Response Error:', error.response?.status, error.response?.data);
    return Promise.reject(error);
  }
);

// Auth service
export const authService = {
  register: (userData) => api.post('/auth/register', userData),
  login: (credentials) => api.post('/auth/login', credentials),
  getProfile: (userId) => api.get(`/auth/profile/${userId}`),
};

// Donation service
export const donationService = {
  create: (donationData) => {
    console.log('API: Creating donation with data:', donationData);
    console.log('API: Using base URL:', API_BASE_URL);
    return api.post('/donations', donationData);
  },
  getAll: () => api.get('/donations'),
  getById: (id) => api.get(`/donations/${id}`),
  getByDonor: (donorId) => api.get(`/donations/donor/${donorId}`),
  getAvailable: () => api.get('/donations/available'),
  getByStatus: (status) => api.get(`/donations/status/${status}`),
  assignToNgo: (donationId, ngoId) => api.put(`/donations/${donationId}/assign-ngo/${ngoId}`),
  markAsPickedUp: (donationId) => api.put(`/donations/${donationId}/pickup`),
  markAsDelivered: (donationId) => api.put(`/donations/${donationId}/deliver`),
  delete: (id) => api.delete(`/donations/${id}`),
};

// Order service
export const orderService = {
  create: (orderData) => api.post('/orders', orderData),
  getAll: () => api.get('/orders'),
  getById: (id) => api.get(`/orders/${id}`),
  getByNgo: (ngoId) => api.get(`/orders/ngo/${ngoId}`),
};

// Food request service
export const requestService = {
  create: (requestData) => api.post('/requests', requestData),
  getAll: () => api.get('/requests'),
  getById: (id) => api.get(`/requests/${id}`),
  getByNgo: (ngoId) => api.get(`/requests/ngo/${ngoId}`),
  getActive: () => api.get('/requests/active'),
  getByStatus: (status) => api.get(`/requests/status/${status}`),
  markAsMatched: (requestId) => api.put(`/requests/${requestId}/match`),
  markAsFulfilled: (requestId) => api.put(`/requests/${requestId}/fulfill`),
  cancel: (requestId) => api.put(`/requests/${requestId}/cancel`),
  delete: (id) => api.delete(`/requests/${id}`),
};

// Stats service
export const statsService = {
  getDashboardStats: () => api.get('/stats/dashboard'),
  getImpactStats: () => api.get('/stats/impact'),
};

export default api;
import axios from 'axios';
import authHeader from './auth-header';

const API_AUTH_URL = import.meta.env.VITE_BACKEND_API_URL + "auth/";

class UserService {
  getPublicContent() {
    return axios.get(API_AUTH_URL + 'all');
  }

  getUserBoard() {
    return axios.get(API_AUTH_URL + 'user', { 
        headers: authHeader() 
    });
  }

  getAdminBoard() {
    return axios.get(API_AUTH_URL + 'admin', { 
        headers: authHeader()
    });
  }
}

export default new UserService();

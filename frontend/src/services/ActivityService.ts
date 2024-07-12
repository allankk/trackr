import axios from 'axios';
import authHeader from './auth-header';

const API_AUTH_URL = "http://localhost:8098/api/";

class ActivityService {
  getAuthHello() {
    console.log('getting auth hello');
    return axios.get(API_AUTH_URL + 'authhello', { 
        headers: authHeader() 
    });
  }
}

export default new ActivityService();

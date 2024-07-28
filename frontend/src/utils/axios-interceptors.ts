import axios from 'axios';
import router from '@/router/index'; 

axios.interceptors.response.use(
  response => response,  
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('user');
      router.push('/login');
      error.message = 'Unauthorized, please login';
    }

    return Promise.reject(error); 
  }
);
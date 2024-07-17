import axios from 'axios';
import authHeader from './auth-header';

const API_AUTH_URL = "http://localhost:8098/api/";

interface ActivityType {
  name: string,
  description: string,
  metrics: Array<number>
}

class ActivityService {
  getAuthHello() {
    console.log('getting auth hello');
    return axios.get(API_AUTH_URL + 'authhello', { 
        headers: authHeader() 
    });
  }

  getAllActivities() {
    console.log('getting all activities')
    return axios.get(API_AUTH_URL + 'activitytype/all', { 
        headers: authHeader() 
    });
  }

  getAllMetrics() {
    console.log('getting all metrics')
    return axios.get(API_AUTH_URL + 'metrics/all', { 
        headers: authHeader() 
    });
  }

  addActivityType(activityType: ActivityType) {
    return axios.post(API_AUTH_URL + 'activitytype/add', activityType, { 
        headers: authHeader() 
    });
  }

  deleteActivityType(id: number) {
    return axios.delete(API_AUTH_URL + 'activitytype/delete/' + id, { 
        headers: authHeader() 
    });
  }
}

export default new ActivityService();

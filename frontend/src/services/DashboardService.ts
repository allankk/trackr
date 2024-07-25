import axios from 'axios';
import authHeader from './auth-header';

const API_AUTH_URL = "http://localhost:8098/api/";

class DashboardService {
  getSessionDates() {
    return axios.get(API_AUTH_URL + 'dashboard/sessiondates', { 
        headers: authHeader() 
    });
  }

  getSessionByDate(date : Date) {
    return axios.get(API_AUTH_URL + 'activitysession/date/' + date, { 
        headers: authHeader() 
    });
  }
}

export default new DashboardService();

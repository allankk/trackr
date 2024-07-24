import axios from 'axios';
import authHeader from './auth-header';

const API_AUTH_URL = "http://localhost:8098/api/";

interface Session {
  id?: number,
  date: Date,
  notes: string,
  activities: {
    id: number,
    metrics: {
      id: number,
      unitId: number,
      value: number,
    }[]
  }[]
}

interface FilterRequest {
  sortOrder?: string,
  startDate?: Date,
  endDate?: Date,
  activityTypes?: Array<number>,
}

class ActivityGroupService {
  getAllSessions() {
    return axios.get(API_AUTH_URL + 'activitysession/all', { 
        headers: authHeader() 
    });
  }

  getFilteredSessions(filterRequest : FilterRequest) {
    return axios.post(API_AUTH_URL + 'activitysession/filtered', filterRequest, {
      headers: authHeader()
    });
  }

  addSession(session: Session) {
    return axios.post(API_AUTH_URL + 'activitysession/add', session, { 
        headers: authHeader() 
    });
  }

  getSession(id: number) {
    return axios.get(API_AUTH_URL + 'activitysession/' + id, { 
        headers: authHeader() 
    });
  }

  updateSession(id: number, session: Session) {
    console.log('topost:')
    console.log(session);
    return axios.post(API_AUTH_URL + 'activitysession/edit/' + id, session, { 
        headers: authHeader() 
    });
  }

  deleteSession(id: number) {
    return axios.delete(API_AUTH_URL + 'activitysession/delete/' + id, { 
        headers: authHeader() 
    });
  }
}

export default new ActivityGroupService();

import axios from 'axios';
import authHeader from './auth-header';

const API_AUTH_URL = "http://localhost:8098/api/";

interface Session {
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

class ActivityGroupService {
  getAllSessions() {
    return axios.get(API_AUTH_URL + 'activitysession/all', { 
        headers: authHeader() 
    });
  }

  addSession(session: Session) {
    return axios.post(API_AUTH_URL + 'activitysession/add', session, { 
        headers: authHeader() 
    });
  }

  //deleteActivityGroup(id: number) {
  //  return axios.delete(API_AUTH_URL + 'activitygroup/delete/' + id, { 
  //      headers: authHeader() 
  //  });
  //}

  //updateActivityGroup(id: number, activityGroup: ActivityGroup) {
  //  return axios.post(API_AUTH_URL + 'activitygroup/edit/' + id, activityGroup, { 
  //      headers: authHeader() 
  //  });
  //}
}

export default new ActivityGroupService();

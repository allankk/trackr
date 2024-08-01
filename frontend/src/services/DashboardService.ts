import axios from "axios";
import authHeader from "./auth-header";

const API_AUTH_URL = process.env.VUE_APP_BACKEND_API_URL;

class DashboardService {
  getSessionDates() {
    return axios.get(API_AUTH_URL + "dashboard/sessiondates", {
      headers: authHeader(),
    });
  }

  getSessionByDate(date: Date) {
    return axios.get(API_AUTH_URL + "activitysession/date/" + date, {
      headers: authHeader(),
    });
  }

  getActivityResults(activityTypeId: number, metricId: number, unitId: number) {
    return axios.get(API_AUTH_URL + "dashboard/results/", {
      params: {
        activityTypeId: activityTypeId,
        metricId: metricId,
        unitId: unitId,
      },
      headers: authHeader(),
    });
  }
}

export default new DashboardService();

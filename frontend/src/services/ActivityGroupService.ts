import axios from "axios";
import authHeader from "./auth-header";

const API_AUTH_URL = process.env.VUE_APP_BACKEND_API_URL;

interface ActivityGroup {
  name: string;
  description: string;
  activityTypes: Array<number>;
}

class ActivityGroupService {
  getAllActivityGroups() {
    return axios.get(API_AUTH_URL + "activitygroup/all", {
      headers: authHeader(),
    });
  }

  addActivityGroup(activityGroup: ActivityGroup) {
    return axios.post(API_AUTH_URL + "activitygroup/add", activityGroup, {
      headers: authHeader(),
    });
  }

  deleteActivityGroup(id: number) {
    return axios.delete(API_AUTH_URL + "activitygroup/delete/" + id, {
      headers: authHeader(),
    });
  }

  updateActivityGroup(id: number, activityGroup: ActivityGroup) {
    return axios.post(
      API_AUTH_URL + "activitygroup/edit/" + id,
      activityGroup,
      {
        headers: authHeader(),
      }
    );
  }
}

export default new ActivityGroupService();

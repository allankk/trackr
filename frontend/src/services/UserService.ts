import axios from "axios";
import authHeader from "./auth-header";

const API_AUTH_URL = process.env.VUE_APP_BACKEND_API_URL + "user/";

class UserService {
  getPublicContent() {
    return axios.get(API_AUTH_URL + "all");
  }

  getUserBoard() {
    return axios.get(API_AUTH_URL + "user", {
      headers: authHeader(),
    });
  }

  getAdminBoard() {
    return axios.get(API_AUTH_URL + "admin", {
      headers: authHeader(),
    });
  }
}

export default new UserService();

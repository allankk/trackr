import axios from "axios";

const API_AUTH_URL = process.env.VUE_APP_BACKEND_API_URL + "auth/";

class AuthService {
  login(email: string, password: string) {
    return axios
      .post(API_AUTH_URL + "login", {
        email,
        password,
      })
      .then((response) => {
        if (response.data.accessToken) {
          localStorage.setItem("user", JSON.stringify(response.data));
        }

        return response.data;
      });
  }

  logout() {
    localStorage.removeItem("user");
  }

  register(email: string, password: string) {
    return axios.post(API_AUTH_URL + "register", {
      email,
      password,
    });
  }
}

export default new AuthService();

import store from '@/store/index';
import { isTokenExpired } from '@/services/auth-utils';

export default function authHeader(): { Authorization?: string } {
  const storedUser = localStorage.getItem("user");

  let user: { accessToken?: string } | null = null;
  if (storedUser) {
    try {
      user = JSON.parse(storedUser);
    } catch (e) {
      console.error("Error parsing stored user:", e);
    }
  }

  if (user && user.accessToken && !isTokenExpired(user.accessToken)) {
    return { Authorization: "Bearer: " + user.accessToken };
  } else {
    localStorage.removeItem("user");
    store.dispatch('auth/logout');
    return {};
  }
}

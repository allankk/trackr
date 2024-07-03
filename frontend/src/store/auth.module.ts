import AuthService from '../services/AuthService';
import { Commit } from "vuex";

interface AuthState {
  status: {
    loggedIn: boolean;
  };
  user: any;
}

const user = JSON.parse(localStorage.getItem('user') as string);
const initialState = user
  ? { status: { loggedIn: true }, user }
  : { status: { loggedIn: false }, user: null };

export const auth = {
  namespaced: true,
  state: initialState,
  actions: {
    login( { commit }: { commit: Commit }, user: any) {
      return AuthService.login(user.email, user.password).then(
        user => {
          commit('loginSuccess', user);
          return Promise.resolve(user);
        },
        error => {
          commit('loginFailure');
          return Promise.reject(error);
        }
      );
    },
    logout({ commit }: { commit: Commit }, user: any) {
      AuthService.logout();
      commit('logout');
    },
    register({ commit }: { commit: Commit }, user: any) {
      return AuthService.register(user.email, user.password).then(
        response => {
          commit('registerSuccess');
          return Promise.resolve(response.data);
        },
        error => {
          commit('registerFailure');
          return Promise.reject(error);
        }
      );
    }
  },
  mutations: {
    loginSuccess(state: AuthState, user: any) {
      state.status.loggedIn = true;
      state.user = user;
    },
    loginFailure(state: AuthState) {
      state.status.loggedIn = false;
      state.user = null;
    },
    logout(state: AuthState) {
      state.status.loggedIn = false;
      state.user = null;
    },
    registerSuccess(state: AuthState) {
      state.status.loggedIn = false;
    },
    registerFailure(state: AuthState) {
      state.status.loggedIn = false;
    }
  }
};

import { createStore } from "vuex";

export default createStore({
  state: {
    currentNav: localStorage.getItem("current-nav"),
  },
  getters: {
    getNav: (state) => state.currentNav,
  },
  mutations: {
    updateNav(state, newNav) {
      state.currentNav = newNav;
    },
  },
});

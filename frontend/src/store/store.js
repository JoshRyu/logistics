import { createStore } from "vuex";

export default createStore({
  state: {
    currentNav: localStorage.getItem("current-nav"),
    searchHistory: {
      material: {
        searchTarget: "PRODUCT_NAME",
        compareType: "E",
        searchValue: "",
        currentPage: 0,
      },
      product: {
        searchTarget: "PRODUCT_NAME",
        compareType: "E",
        searchValue: "",
        currentPage: 0,
      },
    },
  },
  getters: {
    getNav: (state) => state.currentNav,
    getHistory: (state) => state.searchHistory,
  },
  mutations: {
    updateNav(state, newNav) {
      state.currentNav = newNav;
    },
    updateSearchHistory(state, params) {
      if (params.target == "material") {
        state.searchHistory.material = params.history;
      }

      if (params.target == "product") {
        state.searchHistory.product = params.history;
      }
    },
  },
});

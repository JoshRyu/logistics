<template>
  <v-layout ref="app" class="rounded rounded-md">
    <!-- HEADER START -->
    <v-app-bar color="#ffe2a7" name="app-bar">
      <!-- navication bar setting -->
      <v-app-bar-nav-icon
        @click.stop="data.drawer = !data.drawer"
      ></v-app-bar-nav-icon>
      <!-- ESS font Setting -->
      <v-app-bar-title>메이드G - 재고 관리 시스템</v-app-bar-title>

      <v-spacer></v-spacer>
      <h4>{{ data.username }}</h4>
      <v-btn @click="logout" prepend-icon="mdi-logout" class="ml-2 mr-10">
        Logout
      </v-btn>
    </v-app-bar>
    <!-- HEADER END -->

    <!-- 좌측 메뉴바 START -->
    <v-navigation-drawer v-model="data.drawer" permanent color="#fdf6e9">
      <v-list v-model:opened="data.openedGroups">
        <!-- 사용자 :: userList -->
        <v-list-item
          v-if="isAdmin"
          :prepend-icon="navElements.data.user.icon"
          :title="navElements.data.user.title"
          @click="
            navClick(
              'user',
              navElements.data.user.title,
              navElements.data.user.route
            )
          "
          :class="selectedNav == navElements.data.user.title ? 'navColor' : ''"
        ></v-list-item>

        <v-list-group value="product">
          <template v-slot:activator="{ props }">
            <v-list-item
              prepend-icon="mdi-basket"
              v-bind="props"
              title="제품"
            ></v-list-item>
          </template>
          <v-list-item
            v-for="item in navElements.data.product"
            :key="item.idx"
            :value="item.title"
            :title="item.title"
            :prepend-icon="item.icon"
            @click="navClick('product', item.title, item.route)"
            :class="selectedNav == item.title ? 'navColor' : ''"
          ></v-list-item>
        </v-list-group>

        <v-list-group v-if="isAdmin" value="store">
          <template v-slot:activator="{ props }">
            <v-list-item
              prepend-icon="mdi-store"
              v-bind="props"
              title="매장"
            ></v-list-item>
          </template>
          <v-list-item
            v-for="item in navElements.data.store"
            :key="item.idx"
            :value="item.title"
            :title="item.title"
            :prepend-icon="item.icon"
            @click="navClick('store', item.title, item.route)"
            :class="selectedNav == item.title ? 'navColor' : ''"
          ></v-list-item>
        </v-list-group>
      </v-list>
    </v-navigation-drawer>
    <!-- 좌측 메뉴바 END -->

    <!-- 컨텐츠 영역 START -->
    <v-main
      class="d-flex align-center justify-center"
      style="min-height: 300px"
    >
      <router-view />
    </v-main>
    <!-- 컨텐츠 영역 END -->

    <!-- FOOTER START -->
    <v-footer app name="footer">
      <div class="text-right w-100">
        {{ new Date().getFullYear() }} - <strong>메이드G</strong>
      </div></v-footer
    >
    <!-- FOOTER END -->
  </v-layout>
</template>

<script setup>
import { computed, reactive } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

const router = useRouter();
const store = useStore();

const data = reactive({
  drawer: true,
  openedGroups: [localStorage.getItem("current-group")],
  userRole: localStorage.getItem("role"),
  navClass: "navColor",
});

const isAdmin = computed(() => {
  return data.userRole == "ADMIN";
});

const selectedNav = computed(() => {
  return store.getters.getNav;
});

const navClick = (group, title, route) => {
  store.commit("updateNav", title);
  localStorage.setItem("current-nav", title);
  localStorage.setItem("current-group", group);
  router.push({ path: route });
};

const selectedMenu = () => {
  return { navColor: true };
};

/** 화면 콤보박스 셋팅 */
const navElements = reactive({
  data: {
    user: { idx: 0, title: "사용자", icon: "mdi-account", route: "/user/list" },
    product: [
      {
        idx: 0,
        title: "제품 조회",
        icon: "mdi-magnify",
        route: "/product/list",
      },
      {
        idx: 1,
        title: "제품 관리",
        icon: "mdi-basket-plus-outline",
        route: "/product/management",
      },
      {
        idx: 2,
        title: "제품 카테고리",
        icon: "mdi-view-list",
        route: "/product/category",
      },
      {
        idx: 3,
        title: "제품 통계",
        icon: "mdi-chart-bar",
        route: "/product/statistics",
      },
    ],
    store: [
      {
        idx: 0,
        title: "매장 현황",
        icon: "mdi-store-outline",
        route: "/store/status",
      },
      {
        idx: 1,
        title: "매장 등록",
        icon: "mdi-store-plus",
        route: "/store/register",
      },
      {
        idx: 2,
        title: "매장 설정",
        icon: "mdi-store-cog",
        route: "/store/config",
      },
    ],
  },
});

const logout = () => {
  localStorage.removeItem("apollo-token");
  localStorage.removeItem("username");
  router.push({ path: "/login" });
};
</script>

<style scoped>
.v-list-group__items .v-list-item {
  padding-inline-start: 50px !important;
}

.navColor {
  background-color: rgb(197, 197, 197) !important;
}
</style>

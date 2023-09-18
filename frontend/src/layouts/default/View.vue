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
      <v-list>
        <!-- 사용자 :: userList -->
        <v-list-item
          v-if="isAdmin"
          prepend-icon="mdi-account"
          title="사용자"
          @click="this.$router.push({ path: navElements.user.route })"
        ></v-list-item>

        <v-list-group value="Product">
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
            @click="this.$router.push({ path: item.route })"
          ></v-list-item>
        </v-list-group>

        <v-list-group value="Store" v-if="isAdmin">
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
            @click="this.$router.push({ path: item.route })"
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
import { useRouter } from "vue-router";

const router = useRouter();

const data = reactive({
  drawer: true,
  group: null,
  userRole: localStorage.getItem("role"),
});

const isAdmin = computed(() => {
  return data.userRole == "ADMIN";
});

/** 화면 콤보박스 셋팅 */
const navElements = reactive({
  /** 사용자 */
  user: { route: "/user/list" },
  /** 제품 관리 */
  data: {
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
</style>

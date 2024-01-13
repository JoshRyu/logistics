// Composables
import { createRouter, createWebHistory } from "vue-router";
import { requireAuth } from "@/auth/auth.js";

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/login/Login.vue"),
  },
  {
    path: "/home",
    name: "Home",
    component: () => import("@/views/Home.vue"),
    beforeEnter: requireAuth,
    meta: {
      role: ["ADMIN", "USER"], // Adjust roles as needed
    },
  },
  {
    path: "/user/list",
    name: "UserList",
    component: () => import("@/views/user/UserList.vue"),
    beforeEnter: requireAuth,
    meta: {
      role: ["ADMIN"],
    },
  },
  {
    path: "/product/list",
    name: "ProductList",
    component: () => import("@/views/product/ProductList.vue"),
    beforeEnter: requireAuth,
    meta: {
      role: ["ADMIN", "USER"],
    },
  },
  {
    path: "/product/management",
    name: "ProductManagement",
    component: () => import("@/views/product/ProductManagement.vue"),
    beforeEnter: requireAuth,
    meta: {
      role: ["ADMIN", "USER"],
    },
  },
  {
    path: "/product/management/:id",
    name: "ProductManagementWithId",
    component: () => import("@/views/product/ProductManagement.vue"),
    beforeEnter: requireAuth,
    meta: {
      role: ["ADMIN", "USER"],
    },
  },
  {
    path: "/product/category",
    name: "ProductCategory",
    component: () => import("@/views/product/ProductCategory.vue"),
    beforeEnter: requireAuth,
    meta: {
      role: ["ADMIN", "USER"],
    },
  },
  {
    path: "/product/statistics",
    name: "ProductStatistics",
    component: () => import("@/views/product/ProductStatistics.vue"),
    beforeEnter: requireAuth,
    meta: {
      role: ["ADMIN", "USER"],
    },
  },
  {
    path: "/store/status",
    name: "StoreStatus",
    component: () => import("@/views/store/StoreStatus.vue"),
    beforeEnter: requireAuth,
    meta: {
      role: ["ADMIN"],
    },
  },
  {
    path: "/error/403",
    name: "403",
    component: () => import("@/views/error/403.vue"),
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("@/views/error/404.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.path === "/login" || to.path === "/") {
    next();
  } else {
    requireAuth(to, from, next);
  }
});

export default router;

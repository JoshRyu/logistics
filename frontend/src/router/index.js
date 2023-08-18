// Composables
import { createRouter, createWebHistory } from "vue-router";
import { requireAuth } from "@/auth/auth.js";

const routes = [
  {
    path: "/",
    redirect: "/login",
    component: () => import("@/layouts/default/Default.vue"),
    children: [
      {
        path: "/home",
        name: "Home",
        component: () => import("@/views/Home.vue"),
      },
      {
        path: "/user",
        redirect: "/user/list",
        children: [
          {
            path: "list",
            name: "UserList",
            component: () => import("@/views/user/UserList.vue"),
          },
        ],
        beforeEnter: requireAuth,
        // 접근 제한이 필요한 페이지에, 아래 내용을 넣어 특정한 유저만 접근할 수 있게 한다.
        meta: {
          role: ["admin"],
        },
      },
    ],
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/login/Login.vue"),
  },
  {
    path: "/error",
    name: "Error",
    children: [
      {
        path: "403",
        name: "403",
        component: () => import("@/views/error/403.vue"),
      },
      {
        path: "/:pathMatch(.*)*",
        component: () => import("@/views/error/404.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;

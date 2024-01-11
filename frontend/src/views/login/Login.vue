<template>
  <div class="d-flex align-center justify-center" style="height: 100vh">
    <v-sheet width="500" class="mx-auto bg_y">
      <v-img class="mx-auto" width="500" src="@/assets/logo.png"></v-img>
      <v-form fast-fail @submit.prevent="executeLogin(data.credentials)">
        <v-text-field
          class="mb-3 override-class"
          variant="outlined"
          v-model="data.credentials.username"
          :rules="[isValid(data.credentials.username, 'username')]"
          label="아이디"
          required
          autocomplete="username"
        ></v-text-field>

        <v-text-field
          class="mb-3 override-class"
          variant="outlined"
          v-model="data.credentials.password"
          :rules="[isValid(data.credentials.password, 'password')]"
          label="패스워드"
          type="password"
          required
          autocomplete="current-password"
        ></v-text-field>

        <v-btn type="submit" color="grey-darken-4" block class="mt-2"
          >로그인</v-btn
        >
      </v-form>
    </v-sheet>
  </div>
</template>

<script setup>
import { computed, reactive } from "vue";
import { login } from "@/controller/login.js";
import { useRouter } from "vue-router";

const router = useRouter();

const data = reactive({
  credentials: {
    username: "",
    password: "",
  },
  validation: {
    username: false,
    password: false,
  },
});

const validLogin = computed(() => {
  return data.validation.username && data.validation.password;
});

const isValid = (v, k) => {
  if (!v) {
    data.validation[k] = false;
    return "필수 입력사항입니다.";
  }
  data.validation[k] = true;
  return true;
};

const executeLogin = async (params) => {
  if (validLogin.value) {
    try {
      const response = await login({
        userName: params.username,
        password: params.password,
      });

      handleLoginResponse(response);
    } catch (error) {
      console.log("Login error:", error);
      alert("잘못된 사용자 정보입니다. 다시 확인해주세요.");
    }
  }
};

const handleLoginResponse = (response) => {
  let loginInfo = response.loginInfo;

  if (loginInfo) {
    localStorage.setItem("access-token", loginInfo.accessToken);
    localStorage.setItem("refresh-token", loginInfo.refreshToken);
    localStorage.setItem("username", loginInfo.userName);
    localStorage.setItem("role", loginInfo.role);
    if (loginInfo.role == "ADMIN") {
      router.push({ path: "/product" });
    } else {
      router.push({ path: "/product" });
    }
  }
};
</script>

<style scoped>
* {
  background-color: #fbebca;
}

.bg_y {
  background-color: #fbebca;
}

.override-class :deep(.v-input__control) {
  display: flex;
  grid-area: control;
  background-color: white;
}
</style>

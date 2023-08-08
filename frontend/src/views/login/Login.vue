<template>
  <div class="d-flex align-center justify-center" style="height: 100vh">
    <v-sheet width="400" class="mx-auto">
      <v-img
        class="mx-auto mb-10"
        width="300"
        src="@/assets/Logo_Blue.png"
      ></v-img>
      <v-form fast-fail @submit.prevent="executeLogin(data.credentials)">
        <v-text-field
          class="mb-3"
          variant="outlined"
          v-model="data.credentials.id"
          :rules="[isValid(data.credentials.id, 'id')]"
          label="Username"
          required
          autocomplete="username"
        ></v-text-field>

        <v-text-field
          class="mb-3"
          variant="outlined"
          v-model="data.credentials.password"
          :rules="[isValid(data.credentials.password, 'password')]"
          label="Password"
          type="password"
          required
          autocomplete="current-password"
        ></v-text-field>

        <v-select
          v-model="data.credentials.stationCode"
          :rules="[isValid(data.credentials.stationCode, 'stationCode')]"
          required
          label="Station Code"
          :items="data.stationList"
          :no-data-text="
            data.stationList.length < 1 ? '오류: AIMS 상태를 확인해주세요' : ''
          "
        >
        </v-select>
        <v-btn type="submit" color="indigo" block class="mt-2">Sign in</v-btn>
      </v-form>
    </v-sheet>
  </div>
</template>

<script setup>
import { computed, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";

onMounted(() => {});

const data = reactive({
  stationList: [],
  credentials: {
    id: "",
    password: "",
    stationCode: "",
  },
  validation: {
    id: false,
    password: false,
    stationCode: false,
  },
});

const validLogin = computed(() => {
  return (
    data.validation.id &&
    data.validation.password &&
    data.validation.stationCode
  );
});

const isValid = (v, k) => {
  if (!v) {
    data.validation[k] = false;
    return "필수 입력사항입니다.";
  }
  data.validation[k] = true;
  return true;
};

const executeLogin = (params) => {
  if (validLogin.value) {
  }
};

const handleLoginResponse = (response) => {
  let token = response.login.token;
  let username = response.login.id;
  if (token) {
    localStorage.setItem("apollo-token", token);
    localStorage.setItem("username", username);
    router.push({ path: "/assign" });
  }
};
</script>

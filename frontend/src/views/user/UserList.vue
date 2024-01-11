<template>
  <div class="d-flex align-center justify-center ma-10 w-100">
    <v-card color="#ffe2a7" class="w-50">
      <v-card-title class="pb-6">
        <v-row>
          <v-col cols="2">사용자 리스트</v-col>
          <v-col cols="6"></v-col>
          <v-col cols="4"
            ><v-btn
              prepend-icon="mdi-account-plus"
              size="small"
              color="white"
              class="float-right"
              @click="registerItem"
            >
              사용자 생성
            </v-btn></v-col
          >
        </v-row>
        <v-text-field
          v-model="data.searchValue"
          append-icon="mdi-magnify"
          label="검색"
          single-line
          hide-details
          variant="underlined"
        ></v-text-field>
      </v-card-title>

      <v-data-table
        :search="data.searchValue"
        :headers="data.headers"
        :items="data.items"
        items-per-page="5"
        items-per-page-text=""
        :items-per-page-options="[
          { value: 5, title: '5' },
          { value: 10, title: '10' },
        ]"
        no-data-text="등록된 사용자가 존재하지 않습니다."
      >
        <template v-slot:top>
          <v-spacer></v-spacer>
          <v-dialog v-model="data.dialog" max-width="500px">
            <v-form fast-fail @submit.prevent="submitForm" ref="editForm">
              <v-card>
                <v-card-title>
                  <span>{{ formTitle }}</span>
                </v-card-title>
                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-text-field
                        class="override-class"
                        :disabled="data.editedIndex != -1"
                        v-model="data.editedItem.username"
                        autocomplete="id"
                        label="아이디"
                        :rules="[idRule]"
                        required
                      ></v-text-field>
                    </v-row>
                    <v-row>
                      <v-text-field
                        class="override-class mb-1"
                        v-model="data.editedItem.newPassword"
                        :label="
                          data.editedIndex == -1
                            ? '비밀번호'
                            : '새로운 비밀번호'
                        "
                        autocomplete="new-password"
                        :rules="[passwordRule]"
                        required
                        clearable
                        type="password"
                      ></v-text-field>
                    </v-row>
                    <v-row>
                      <v-text-field
                        class="override-class mb-1"
                        v-model="data.editedItem.confirmPassword"
                        label="비밀번호 확인"
                        autocomplete="confirm-password"
                        :rules="[confirmPasswordRule]"
                        required
                        clearable
                        type="password"
                      ></v-text-field>
                    </v-row>
                    <v-row>
                      <v-select
                        class="override-class"
                        v-model="data.editedItem.role"
                        :items="data.availableRoles"
                        label="사용자 권한"
                      ></v-select>
                    </v-row>
                  </v-container>
                </v-card-text>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn
                    color="blue-darken-1"
                    variant="text"
                    @click="closeEditPopup"
                  >
                    Cancel
                  </v-btn>
                  <v-btn type="submit" color="blue-darken-1" variant="text">
                    Save
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-form>
          </v-dialog>
          <v-dialog v-model="data.dialogDelete" max-width="500px">
            <v-card>
              <v-spacer />
              <div class="text-center">
                <v-card-title>정말 사용자를 삭제하시겠습니까?</v-card-title>
              </div>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  color="blue-darken-1"
                  variant="text"
                  @click="deleteItemConfirm"
                  >OK</v-btn
                >
                <v-btn
                  color="blue-darken-1"
                  variant="text"
                  @click="closeDeletePopup"
                  >Cancel</v-btn
                >
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </template>
        <!-- eslint-disable-next-line -->
        <template v-slot:item.actions="{ item }">
          <v-icon size="small" class="me-2" @click="editItem(item.raw)">
            mdi-pencil
          </v-icon>

          <v-icon
            v-if="item.raw.id != 'admin' && item.raw.id != 'manager'"
            size="small"
            @click="deleteItem(item.raw)"
          >
            mdi-delete
          </v-icon>
        </template>
      </v-data-table>
    </v-card>
  </div>
</template>

<script setup>
import { computed, nextTick, reactive, onMounted } from "vue";
import {
  createUser,
  readUsers,
  updateUser,
  deleteUser,
} from "@/controller/user.js";

const userList = async () => {
  try {
    const response = await readUsers();

    data.items = response.user;
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => {
  userList();
});

const data = reactive({
  dialog: false,
  dialogDelete: false,
  headers: [
    {
      title: "시퀀스",
      align: "start",
      sortable: false,
      key: "id",
    },
    {
      title: "아이디",
      sortable: false,
      key: "username",
    },
    {
      title: "권한",
      sortable: false,
      key: "role",
    },
    { title: "수정/삭제", key: "actions", sortable: false },
  ],
  items: [],
  editedIndex: -1,
  editedItem: {
    username: "",
    newPassword: "",
    confirmPassword: "",
    role: "USER",
  },
  defaultItem: {
    username: "",
    newPassword: "",
    confirmPassword: "",
    role: "USER",
  },
  searchValue: "",
  validation: {
    id: false,
    password: false,
    confirmPassword: false,
  },
  availableRoles: ["ADMIN", "USER"],
});

const formTitle = computed(() => {
  return data.editedIndex === -1 ? "새로운 사용자 등록" : "사용자 정보 수정";
});

const isValid = computed(() => {
  return (
    data.validation.id &&
    data.validation.password &&
    data.validation.confirmPassword
  );
});

const idRule = (v) => {
  let validity = /^[a-zA-Z0-9]*$/.test(v);

  if (!v) {
    data.validation.id = false;
    return "아이디는 필수 입력사항입니다.";
  }

  if (!validity) {
    data.validation.id = false;
    return "아이디는 영문+숫자만 입력 가능합니다.";
  }

  if (v && v.length < 5) {
    data.validation.id = false;
    return "최소 5자 이상 입력해주세요";
  }

  if (v && v.length > 12) {
    data.validation.id = false;
    return "ID는 최대 12자까지 등록하실 수 있습니다.";
  }

  data.validation.id = true;
  return true;
};

const pattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{8,20}$/;

const passwordRule = (v) => {
  if (!v) {
    data.validation.password = false;
    return "비밀번호는 필수 입력사항입니다.";
  }
  if (v && v.length < 5) {
    data.validation.password = false;
    return "최소 5자 이상 입력해주세요";
  }

  if (v && v.length > 30) {
    data.validation.password = false;
    return "비밀번호는 최대 30자까지 설정하실 수 있습니다.";
  }

  if (v && !pattern.test(v)) {
    data.validation.password = false;
    return "비밀번호는 문자, 숫자, 특수문자를 하나 이상 포함하며 8자 이상이어야 합니다.";
  }

  data.validation.password = true;
  return true;
};

const confirmPasswordRule = (v) => {
  if (!v) {
    data.validation.confirmPassword = false;
    return "비밀번호는 필수 입력사항입니다.";
  }
  if (v !== data.editedItem.newPassword) {
    data.validation.confirmPassword = false;
    return "비밀번호는 동일해야 합니다.";
  }

  if (v && !pattern.test(v)) {
    data.validation.confirmPassword = false;
    return "비밀번호는 문자, 숫자, 특수문자를 하나 이상 포함하며 8자 이상이어야 합니다.";
  }

  data.validation.confirmPassword = true;
  return true;
};

const editItem = (item) => {
  data.editedIndex = data.items.indexOf(item);
  data.editedItem = Object.assign({}, item);
  data.dialog = true;
};

const registerItem = () => {
  data.editedItem = Object.assign({}, data.defaultItem);
  data.dialog = true;
};

const deleteItem = (item) => {
  data.editedIndex = data.items.indexOf(item);
  data.editedItem = Object.assign({}, item);
  data.dialogDelete = true;
};

const deleteItemConfirm = () => {
  executeDeleteUser();
  closeDeletePopup();
};

const closeEditPopup = () => {
  data.dialog = false;
  nextTick(() => {
    data.editedItem = Object.assign({}, data.defaultItem);
    data.editedIndex = -1;
  });
};

const closeDeletePopup = () => {
  data.dialogDelete = false;
  nextTick(() => {
    data.editedItem = Object.assign({}, data.defaultItem);
    data.editedIndex = -1;
  });
};

const submitForm = () => {
  if (isValid.value) {
    if (data.editedIndex == -1) {
      executeCreateUser();
    } else {
      executeEditUser();
    }
  }
};

const executeCreateUser = async () => {
  try {
    await createUser({
      username: data.editedItem.username,
      password: data.editedItem.newPassword,
      role: data.editedItem.role,
    });
    alert("성공적으로 사용자를 등록 하였습니다.");
    userList();
    closeEditPopup();
  } catch (e) {
    console.error(e);

    if (e.response.data.status == 409) {
      alert("이미 등록된 사용자입니다. 다른 ID를 시도해주세요.");
    } else {
      alert("사용자를 등록하지 못했습니다.");
    }
  }
};

const executeEditUser = async () => {
  try {
    await updateUser(data.editedItem.id, {
      password: data.editedItem.newPassword,
      role: data.editedItem.role,
    });
    alert("성공적으로 사용자 정보를 업데이트 하였습니다.");
    userList();
    closeEditPopup();
  } catch (e) {
    console.error(e.message);
    alert("사용자 정보를 업데이트 하지 못했습니다.");
  }
};

const executeDeleteUser = async () => {
  try {
    await deleteUser(data.editedItem.id);
    alert("성공적으로 사용자를 삭제 하였습니다.");
    userList();
    closeDeletePopup();
  } catch (e) {
    console.error(e);
    if (e.response.data.status == 400) {
      alert("마지막 ADMIN 사용자는 삭제할 수 없습니다.");
    } else {
      alert("사용자를 삭제하지 못했습니다.");
    }
  }
};
</script>

<style scoped>
.override-class :deep(.v-input__control) {
  display: flex;
  grid-area: control;
  background-color: #fdf6e9;
}
</style>

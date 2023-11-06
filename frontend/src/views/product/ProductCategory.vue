<template>
  <v-container style="max-width: 85%">
    <h1 class="mb-10">제품 카테고리 페이지</h1>
    <v-row>
      <v-icon class="ma-5" size="x-large" @click="addCategory"
        >mdi-plus-circle</v-icon
      >
      <h2 class="ma-5">새 카테고리</h2>
      <v-spacer></v-spacer>
      <v-btn
        size="x-large"
        variant="outlined"
        class="ma-3"
        @click="resetCategory"
        >취소</v-btn
      >
      <v-btn
        size="x-large"
        variant="flat"
        color="grey-darken-3"
        class="ma-3"
        @click="registerCategory"
        >저장</v-btn
      >
      <v-divider class="border-opacity-100" color="black"></v-divider>
    </v-row>
    <v-row>
      <v-col cols="4">
        <v-hover v-for="category in data.categoryList">
          <template v-slot:default="{ isHovering, props }">
            <v-row
              v-bind="props"
              :class="
                category.isClicked
                  ? data.onClick
                  : isHovering
                  ? data.onMouse
                  : 'borderSolid ma-3'
              "
              @click="selectCategory(category)"
            >
              <v-col cols="1">
                <v-icon v-if="isHovering">mdi-format-list-bulleted</v-icon>
              </v-col>
              <v-spacer></v-spacer>
              <v-col cols="4" align="center"
                ><h3>{{ category.name }}</h3></v-col
              >
              <v-spacer></v-spacer>
              <v-col cols="1">
                <v-icon v-if="isHovering" @click="deleteCategory(category)"
                  >mdi-close-circle-outline</v-icon
                >
              </v-col>
            </v-row>
          </template>
        </v-hover>
      </v-col>
      <v-divider class="border-opacity-100" color="black" vertical></v-divider>
      <v-col cols="8">
        <h3 class="ma-3">카테고리 명 *</h3>
        <v-row>
          <v-col cols="4" class="ml-3">
            <v-text-field
              v-model="data.selectedCategory.name"
              hide-details
              variant="outlined"
              clear-icon="mdi-close-circle"
              clearable
              type="text"
            ></v-text-field>
          </v-col>
        </v-row>
        <h3 class="ma-3">상위 카테고리 명 (선택)</h3>
        <v-row>
          <v-col cols="4" class="ml-3">
            <v-autocomplete
              v-model="data.selectedCategory.parentCategory"
              :items="categoryNameList"
              hide-details
              variant="outlined"
              clear-icon="mdi-close-circle"
              clearable
              no-data-text="카테고리를 먼저 생성해주세요"
              type="text"
            />
          </v-col>
        </v-row>
        <h3 class="ma-3">카테고리 설명</h3>
        <v-row>
          <v-col cols="6" class="ml-3">
            <v-textarea
              v-model="data.selectedCategory.description"
              hide-details
              variant="outlined"
              type="text"
            ></v-textarea>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
  <YesNoModal
    :propObj="data.modalProps"
    @confirmed="handleConfirmation"
    @canceled="handleCancellation"
  />
</template>

<script setup>
import { reactive, computed, onMounted } from "vue";
import { createCategory, getCategoryList } from "@/controller/category.js";
import YesNoModal from "../components/YesNoModal.vue";

const resetCategory = () => {
  if (data.newCategoryEnabled) {
    data.newCategoryEnabled = false;
    data.categoryList.shift();
  }
};

const registerCategory = async () => {
  try {
    await createCategory({
      categoryName: data.selectedCategory.name,
      description: data.selectedCategory.description,
      parentCategoryName: data.selectedCategory.parentCategory,
    });
    alert("성공적으로 카테고리를 등록 하였습니다.");
    categoryList();
  } catch (e) {
    console.error(e);

    if (e.response.data.status == 409) {
      alert("이미 카테고리 입니다. 다른 명칭을 시도해주세요.");
    } else {
      alert("카테고리를 등록하지 못했습니다.");
    }
  }
};

const categoryList = async () => {
  try {
    const response = await getCategoryList();

    data.categoryList = response;
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => {
  categoryList();
});

const data = reactive({
  categoryList: [
    {
      name: "뜨게 가방",
      parentCategory: "",
      description: "뜨게 가방 설명",
      isClicked: false,
    },
    {
      name: "뜨게 케이스",
      parentCategory: "",
      description: "뜨게 케이스 설명",
      isClicked: false,
    },
  ],
  onMouse: "borderSolid bgColor ma-3",
  onClick: "borderSolid bgColorSelected ma-1",
  selectedCategory: {
    name: "",
    parentCategory: "",
    description: "",
  },
  newCategoryEnabled: false,
  modalProps: {
    enabled: false,
    title: "",
    message: "",
    confirmOnly: false,
  },
});

const categoryNameList = computed(() => {
  return data.categoryList
    .map((category) => {
      if (data.selectedCategory.name !== category.name) {
        return category.name;
      }
      return undefined;
    })
    .filter((name) => name !== undefined);
});

const addCategory = () => {
  if (!data.newCategoryEnabled) {
    data.newCategoryEnabled = true;
    data.categoryList.unshift({
      name: "새 카테고리",
      parentCategory: "",
      description: "",
      isClicked: false,
    });
    selectCategory(data.categoryList[0]);
  } else {
    data.modalProps = {
      enabled: true,
      title: "오류",
      message: "이미 생성중인 카테고리가 있습니다.",
      confirmOnly: true,
    };
  }
};

const deleteCategory = (selectedCategory) => {
  data.modalProps = {
    enabled: true,
    title: "확인 필요",
    message: `정말 "#${selectedCategory.name}" 카테고리를 삭제하시겠습니까?`,
    confirmOnly: false,
  };
};

const handleConfirmation = () => {
  data.modalProps.enabled = false;
  // Handle the confirmation action here
};

const handleCancellation = () => {
  data.modalProps.enabled = false;
  // Handle the cancellation action here
};

const selectCategory = (selectedCategory) => {
  data.categoryList.forEach((category) => {
    category.isClicked = category === selectedCategory;
  });
  data.selectedCategory = {
    name: selectedCategory.name,
    parentCategory: selectedCategory.parentCategory,
    description: selectedCategory.description,
  };
};
</script>
<style scoped>
.borderSolid {
  border: 1px solid rgb(118, 118, 118);
}

.bgColor {
  color: black;
  background-color: #fff8e1;
}

.bgColorSelected {
  color: black;
  background-color: #ffe082;
}
</style>

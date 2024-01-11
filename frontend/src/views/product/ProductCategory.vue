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
        @click="
          !clickedCategory.categoryCode ? registerCategory() : updateCategory()
        "
        >{{
          !clickedCategory.categoryCode ? "카테고리 저장" : "카테고리 업데이트"
        }}</v-btn
      >
      <v-divider class="border-opacity-100" color="black"></v-divider>
    </v-row>
    <v-row>
      <v-col cols="4.5">
        <v-container style="max-height: 500px; overflow-y: auto">
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
                <v-col cols="8" align="center"
                  ><h3>{{ category.name }}</h3></v-col
                >
                <v-spacer></v-spacer>
                <v-col cols="1">
                  <v-icon
                    v-if="isHovering && category.categoryCode"
                    @click="removeCategory(category)"
                    >mdi-close-circle-outline</v-icon
                  >
                </v-col>
              </v-row>
            </template>
          </v-hover>
        </v-container>
      </v-col>
      <v-divider class="border-opacity-100" color="black" vertical></v-divider>
      <v-col cols="7">
        <h3 class="ma-3">카테고리 명 *</h3>
        <v-row>
          <v-col cols="4" class="ml-3">
            <v-text-field
              v-model="clickedCategory.name"
              hide-details
              variant="outlined"
              clear-icon="mdi-close-circle"
              clearable
              type="text"
            ></v-text-field>
          </v-col>
        </v-row>
        <!-- 현재 Recursive Error가 있어서 사용중지. -->
        <!-- <h3 class="ma-3">상위 카테고리 명 (선택)</h3>
        <v-row>
          <v-col cols="4" class="ml-3">
            <v-autocomplete
              v-model="clickedCategory.parentCategory.name"
              :items="parentCategoryList"
              hide-details
              variant="outlined"
              clear-icon="mdi-close-circle"
              clearable
              no-data-text="카테고리를 먼저 생성해주세요"
              type="text"
            />
          </v-col>
        </v-row> -->
        <h3 class="ma-3">카테고리 설명</h3>
        <v-row>
          <v-col cols="6" class="ml-3">
            <v-textarea
              v-model="clickedCategory.description"
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
import {
  createCategory,
  patchCategory,
  deleteCategory,
  getCategoryList,
} from "@/controller/category.js";
import YesNoModal from "../components/YesNoModal.vue";

onMounted(() => {
  retrieveCategoryList();
});

const resetCategory = () => {
  if (data.newCategoryEnabled) {
    data.newCategoryEnabled = false;
    data.categoryList.shift();
  }
};

const registerCategory = async () => {
  const payload = {
    name: clickedCategory.value.name,
    description: clickedCategory.value.description,
  };
  // parentCategoryCode: clickedCategory.value.parentCategory
  //   ? clickedCategory.value.parentCategory.categoryCode
  //   : null,
  try {
    await createCategory(payload);
    alert("성공적으로 카테고리를 등록 하였습니다.");
    data.newCategoryEnabled = false;
    retrieveCategoryList();
  } catch (e) {
    console.error(e);

    if (e.response.data.status == 409) {
      alert("이미 존재하는 카테고리 이름 입니다. 다른 명칭을 시도해주세요.");
    } else {
      alert("카테고리를 등록하지 못했습니다.");
    }
  }
};

const updateCategory = async () => {
  const categoryId = clickedCategory.value.categoryCode;
  const payload = {
    name: clickedCategory.value.name,
    description: clickedCategory.value.description,
    parentCategoryCode:
      clickedCategory.value.parentCategory.name != ""
        ? getCategoryCode(clickedCategory.value.parentCategory.name)
        : null,
  };
  console.log(clickedCategory.value);
  console.log(payload);

  try {
    await patchCategory(categoryId, payload);
    alert("성공적으로 카테고리를 업데이트 하였습니다.");
    retrieveCategoryList();
  } catch (e) {
    console.error(e);

    if (e.response.data.status == 409) {
      alert("이미 존재하는 카테고리 이름 입니다. 다른 명칭을 시도해주세요.");
    } else {
      alert("카테고리를 업데이트하지 못했습니다.");
    }
  }
};

const retrieveCategoryList = async () => {
  try {
    const response = await getCategoryList();

    // Transform the response to handle null parentCategory
    data.categoryList = response.map((item) => {
      // If parentCategory is null, replace it with {"name": null}
      if (item.parentCategory === null) {
        return { ...item, parentCategory: { name: "" } };
      }

      // Return the item as is if parentCategory is not null
      return item;
    });
    data.newCategoryEnabled = false;
  } catch (error) {
    console.error(error);
  }
};

const data = reactive({
  categoryToDelete: null,
  categoryList: [],
  onMouse: "borderSolid bgColor ma-3",
  onClick: "borderSolid bgColorSelected ma-1",
  newCategoryEnabled: false,
  modalProps: {
    enabled: false,
    title: "",
    message: "",
    confirmOnly: false,
  },
});

const parentCategoryList = computed(() => {
  return data.categoryList
    .map((category) => {
      if (clickedCategory.name !== category.name) {
        return category.name;
      }
      return undefined;
    })
    .filter((name) => name !== "새 카테고리" && name !== undefined);
});

const clickedCategory = computed(() => {
  const found = data.categoryList.find((item) => item.isClicked === true);

  if (found) {
    // Create a new object with the same properties as the found item
    return {
      ...found,
    };
  } else {
    return { name: "", description: "", parentCategory: "", isClicked: false };
  }
});

const addCategory = () => {
  if (!data.newCategoryEnabled) {
    data.newCategoryEnabled = true;
    data.categoryList.unshift({
      name: "새 카테고리",
      parentCategory: { name: "" },
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

const removeCategory = (selectedCategory) => {
  data.categoryToDelete = selectedCategory;
  data.modalProps = {
    enabled: true,
    title: "확인 필요",
    message: `정말 "#${selectedCategory.name}" 카테고리를 삭제하시겠습니까?`,
    confirmOnly: false,
  };
};

const handleConfirmation = async () => {
  try {
    if (data.categoryToDelete) {
      await deleteCategory(data.categoryToDelete.categoryCode); // Use the stored category
      data.categoryToDelete = null; // Reset after deletion
      alert("성공적으로 카테고리를 삭제하였습니다.");
      retrieveCategoryList();
    }
    data.modalProps.enabled = false;
  } catch (error) {
    console.error(error);
  }
};

const handleCancellation = () => {
  data.modalProps.enabled = false;
  // Handle the cancellation action here
};

const selectCategory = (selectedCategory) => {
  data.categoryList.forEach((category) => {
    category.isClicked = category === selectedCategory;
  });
};

const getCategoryCode = (categoryName) => {
  const item = data.categoryList.find((item) => item.name === categoryName);
  return item ? item.categoryCode : null;
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

<template>
  <v-container style="max-width: 85%">
    <h1 class="mb-5">제품/재료 관리 페이지</h1>
    <v-row dense>
      <v-col cols="3">
        <v-card class="border-solid">
          <v-card-title>
            <span> 이미지 </span>
          </v-card-title>
          <v-img class="ma-3" :src="imageUrl" />
        </v-card>
      </v-col>
      <v-col cols="9">
        <v-card class="border-solid">
          <v-card-title>
            <span> 기본정보 </span>
          </v-card-title>
          <v-row>
            <v-col cols="3" class="ml-3">
              <v-text-field
                disabled=""
                v-model="data.product.id"
                label="제품/재료 코드 - 자동 생성"
                hide-details
                placeholder="코드"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                type="text"
              ></v-text-field>
            </v-col>
            <v-col cols="3" class="ml-3">
              <v-text-field
                v-model="data.product.barcode"
                label="바코드"
                hide-details
                placeholder="바코드"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                type="text"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="4" class="ml-3">
              <v-text-field
                v-model="data.product.name"
                :error-messages="data.formErrors.name"
                @input="resetError('name')"
                label="제품명*"
                placeholder="제품명*"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                type="text"
              ></v-text-field>
            </v-col>
            <v-col cols="3">
              <v-autocomplete
                v-model="data.product.type"
                :items="data.productType"
                :error-messages="data.formErrors.type"
                @update:modelValue="resetError('type')"
                label="제품/재료*"
                placeholder="제품/재료*"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                type="text"
              >
              </v-autocomplete>
            </v-col>
            <!-- 현재 Recursive Error가 있어서 사용중지. -->
            <!-- <v-col cols="3">
              <v-autocomplete
                v-model="data.product.categoryCode"
                label="제품 상위 카테고리"
                hide-details
                placeholder="제품 상위 카테고리"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                no-data-text="카테고리를 먼저 생성해주세요"
                type="text"
              >
              </v-autocomplete>
            </v-col> -->
            <v-col cols="3">
              <v-autocomplete
                v-model="data.product.categoryCode"
                :items="categoryNames"
                label="카테고리*"
                :error-messages="data.formErrors.categoryCode"
                @update:modelValue="resetError('categoryCode')"
                placeholder="카테고리*"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                no-data-text="해당하는 카테고리가 없습니다."
                type="text"
              >
              </v-autocomplete>
            </v-col>
            <v-spacer></v-spacer>
          </v-row>
          <v-row>
            <v-col cols="3" class="ml-3">
              <v-text-field
                type="number"
                v-model="data.product.price"
                label="판매가*"
                placeholder="판매가*"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                step="100"
                min="0"
              ></v-text-field>
            </v-col>
            <v-col cols="3">
              <v-text-field
                type="number"
                v-model="data.product.stock"
                label="재고*"
                placeholder="재고*"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                min="0"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="11" class="ma-3">
              <v-textarea
                hide-details
                v-model="data.product.description"
                label="제품 설명"
                placeholder="제품 설명"
                variant="outlined"
                type="text"
              >
              </v-textarea>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="3" class="ml-5">
              <v-file-input
                v-model="data.product.img"
                prepend-icon="mdi-camera"
                label="제품 이미지 업로드"
                variant="solo-filled"
                accept="image/*"
              ></v-file-input>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-spacer></v-spacer>
      <v-btn size="x-large" class="mr-3" color="grey-darken-3" @click="submit">
        저장
      </v-btn>
    </v-row>
  </v-container>

  <YesNoModal
    :propObj="data.modalProps"
    @confirmed="handleConfirmation"
    @canceled="handleCancellation"
  />
</template>

<script setup>
import { reactive, onMounted, computed } from "vue";
import {
  createProduct,
  patchProduct,
  getProductById,
} from "@/controller/product.js";
import { getCategoryList } from "@/controller/category.js";
import { useRoute } from "vue-router";
import YesNoModal from "../components/YesNoModal.vue";
import noImageSrc from "@/assets/images/No_Image_Available.jpg";

const route = useRoute();

onMounted(() => {
  getProduct();
  categoryList();
});

// Validation Computed Properties
const isValidName = computed(() => {
  return data.product.name && data.product.name.trim().length > 0;
});
const isValidType = computed(() =>
  data.productType.includes(data.product.type)
);

const validateForm = () => {
  data.formErrors = {};
  if (!data.product.categoryCode) {
    data.formErrors.categoryCode = "카테고리를 선택해주세요.";
  } else if (!categoryNames.value.includes(data.product.categoryCode)) {
    data.formErrors.categoryCode = "유효하지 않은 카테고리입니다.";
  }

  if (!isValidName.value) {
    data.formErrors.name = "이름을 입력해주세요.";
  }

  if (!isValidType.value) {
    data.formErrors.type = "제품/재료를 선택해주세요.";
  }

  return Object.keys(data.formErrors).length === 0;
};

const resetError = (fieldName) => {
  if (!data.formSubmitted) {
    data.formErrors[fieldName] = "";
  }
};

const imageUrl = computed(() => {
  return data.product.img && data.product.img.length > 0
    ? URL.createObjectURL(data.product.img[0])
    : data.noImgSrc;
});

const getProduct = async () => {
  try {
    if (!Object.keys(route.params).length == 0) {
      const response = await getProductById(route.params.id);
      data.product = response;
      data.isNewProduct = false;
    } else {
      data.product = {
        img: null,
        id: "",
        barcode: "",
        name: "-",
        categoryCode: null,
        price: 0,
        stock: 0,
        type: "PRODUCT",
        description: "-",
      };
      data.isNewProduct = true;
    }
  } catch (error) {
    console.error(error);
  }
};

const submit = () => {
  data.formSubmitted = true;
  if (!validateForm()) {
    data.modalProps = {
      enabled: true,
      title: "오류",
      message: "제품명, 제품/재료, 카테고리는 필수 값입니다.",
      confirmOnly: true,
    };
    data.formSubmitted = false;
    return;
  }

  if (data.isNewProduct) {
    registerProduct();
  } else {
    patchProduct();
  }
};

const registerProduct = async () => {
  const formData = new FormData();
  formData.append("name", data.product.name);
  formData.append("type", data.product.type);
  formData.append("categoryCode", getCategoryCode(data.product.categoryCode));
  formData.append("price", data.product.price);
  formData.append("stock", data.product.stock);
  formData.append("barcode", data.product.barcode);
  formData.append("description", data.product.description);

  if (data.product.img) {
    formData.append("img", data.product.img[0]);
  }

  try {
    await createProduct(formData);

    data.modalProps = {
      enabled: true,
      title: "성공",
      message:
        data.product.type == "PRODUCT"
          ? "제품 등록에 성공하였습니다!"
          : "재료 등록에 성공하였습니다!",
      confirmOnly: true,
    };
  } catch (error) {
    data.modalProps = {
      enabled: true,
      title: "오류",
      message: error.response.data.message,
      confirmOnly: true,
    };
  }
};

// TODO: ProductList 페이지 끝난 후, 거기서 EDIT으로 넘어왔을 때 업데이트 필요.
const updateProduct = async () => {
  try {
    const response = await patchProduct(route.params.id);
  } catch (error) {
    console.error(error);
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

const categoryNames = computed(() => data.categoryList.map((x) => x.name));

const getCategoryCode = (categoryName) => {
  const item = data.categoryList.find((item) => item.name === categoryName);
  return item ? item.categoryCode : null;
};

const handleConfirmation = () => {
  data.modalProps.enabled = false;
};

const handleCancellation = () => {
  data.modalProps.enabled = false;
};

// TODO: 카테고리 Validation 로직, 제품/재료 영어-한글 벨류 Computation 추가, 뭔가 이상한 판매가 재고 관리..
const data = reactive({
  formErrors: {},
  formSubmitted: false,
  noImgSrc: noImageSrc,
  product: {
    img: null,
    id: "-",
    barcode: "",
    name: "",
    categoryCode: "",
    price: 0,
    stock: 0,
    type: "",
    description: "-",
  },
  productType: ["PRODUCT", "MATERIAL"],
  categoryList: [],
  isNewProduct: false,
  userInteracted: {
    categoryCode: false,
  },
  modalProps: {
    enabled: false,
    title: "",
    message: "",
    confirmOnly: false,
  },
});
</script>
<style scoped>
.border-solid {
  border: 1px solid rgb(118, 118, 118);
}
</style>

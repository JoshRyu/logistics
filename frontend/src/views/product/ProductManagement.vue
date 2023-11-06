<template>
  <v-container style="max-width: 85%">
    <h1 class="mb-5">제품 관리 페이지</h1>
    <v-row dense>
      <v-col cols="3">
        <v-card class="borderSolid">
          <v-card-title>
            <span> 제품 이미지 </span>
          </v-card-title>
          <v-img
            class="ma-3"
            :src="data.product.img ? data.product.img : data.noImgSrc"
          >
          </v-img>
        </v-card>
      </v-col>
      <v-col cols="9">
        <v-card class="borderSolid">
          <v-card-title>
            <span> 기본정보 </span>
          </v-card-title>
          <v-row>
            <v-col cols="3" class="ml-3">
              <v-text-field
                disabled=""
                v-model="data.product.id"
                label="제품코드 - 자동으로 생성"
                hide-details
                placeholder="제품코드"
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
                placeholder="제품 바코드"
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
                label="제품명"
                hide-details
                placeholder="제품명"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                type="text"
              ></v-text-field>
            </v-col>
            <v-col cols="3">
              <v-autocomplete
                v-model="data.product.parentCategory"
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
            </v-col>
            <v-col cols="3">
              <v-autocomplete
                :disabled="data.product.parentCategory == null"
                v-model="data.product.childCategory"
                label="제품 하위 카테고리"
                hide-details
                placeholder="제품 하위 카테고리"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                no-data-text="카테고리를 먼저 생성해주세요"
                type="text"
              >
              </v-autocomplete>
            </v-col>
            <v-spacer></v-spacer>
          </v-row>
          <v-row>
            <v-col cols="3" class="ml-3">
              <v-text-field
                v-model="data.product.price"
                label="제품 판매가"
                hide-details
                placeholder="제품 판매가"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                type="text"
              ></v-text-field>
            </v-col>
            <v-col cols="2">
              <v-text-field
                v-model="data.product.stock"
                label="제품 재고"
                hide-details
                placeholder="제품 재고"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                type="text"
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
                prepend-icon="mdi-camera"
                label="제품 이미지 업로드"
                variant="solo-filled"
              ></v-file-input>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-spacer></v-spacer>
      <v-btn size="x-large" class="mr-3" color="grey-darken-3"> 저장 </v-btn>
    </v-row>
  </v-container>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import { getProductById } from "@/controller/product.js";
import { getCategoryList } from "@/controller/category.js";
import { useRoute } from "vue-router";

const route = useRoute();

const getProduct = async () => {
  try {
    if (route.params) {
      const response = await getProductById(route.params.id);
      data.product = response;
    } else {
      data.product = {
        img: null,
        id: "",
        barcode: "",
        name: "",
        parentCategory: "",
        childCategory: "",
        price: 0,
        stock: 0,
        description: "-",
      };
    }
  } catch (error) {
    console.error(error);
  }
};

const categoryList = async () => {
  try {
    const response = await getCategoryList();

    data.categoryList = response.map((x) => x.name);
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => {
  getProduct();
  categoryList();
});

const data = reactive({
  noImgSrc: "/src/assets/images/No_Image_Available.jpg",
  product: {
    img: null,
    id: "",
    barcode: "",
    name: "",
    parentCategory: "",
    childCategory: "",
    price: 0,
    stock: 0,
    description: "-",
  },
});
</script>
<style scoped>
.borderSolid {
  border: 1px solid rgb(118, 118, 118);
}
</style>

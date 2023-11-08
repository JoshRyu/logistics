<template>
  <v-container style="max-width: 85%" class="mt-5">
    <v-row>
      <v-card-title class="text-h4">
        <span>제품 검색</span>
      </v-card-title>

      <v-toolbar color="white" class="mb-5">
        <v-row>
          <v-col cols="2" class="mt-2">
            <v-autocomplete
              v-model="data.selectedCategory"
              class="ml-4"
              label="카테고리 선택"
              variant="outlined"
              :items="data.categoryList"
              no-data-text="검색된 카테고리가 없습니다"
              hide-details
              clearable
            ></v-autocomplete>
          </v-col>
          <v-col cols="4" class="mt-2">
            <v-text-field
              v-model="data.searchValue"
              label="검색어"
              hide-details
              placeholder="검색어"
              variant="outlined"
              clear-icon="mdi-close-circle"
              clearable
              type="text"
            ></v-text-field>
          </v-col>
          <v-col cols="2" class="mt-2">
            <v-btn size="x-large" variant="outlined">검색</v-btn>
          </v-col>
          <v-col cols="4"> </v-col>
        </v-row>
      </v-toolbar>
    </v-row>
    <v-row dense>
      <v-col
        v-for="card in data.cards"
        :key="card.name"
        :cols="data.cards.length >= 4 ? card.flex : 4"
      >
        <v-card class="borderSolid">
          <v-card-title class="text-h5">
            <span v-text="card.name"></span>
          </v-card-title>
          <v-card-subtitle class="text-h6 mb-1">
            <span
              v-if="card.category"
              class="float-right text-red"
              v-text="'#' + card.category.name"
            ></span>
          </v-card-subtitle>
          <v-img v-if="data.enableImg" :src="card.img" class="align-end" cover>
          </v-img>

          <v-card-text class="text-body-1 text-grey" v-if="card.description">
            <span v-text="card.description"></span>
          </v-card-text>

          <v-card-text class="text-right text-h6" v-if="card.price">
            <span v-text="'판매가: ' + card.price"></span>
            <v-spacer></v-spacer>
            <span v-text="'재고: ' + card.stock + '개'"></span>
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              size="large"
              color="surface-variant"
              variant="text"
              icon="mdi-pencil-circle"
              @click="routeTo(card.productCode)"
            ></v-btn>
            <v-btn
              size="large"
              color="surface-variant"
              variant="text"
              icon="mdi-delete-circle"
              @click="deleteProduct(card.productCode, card.name)"
            ></v-btn>
          </v-card-actions>
        </v-card>
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
import { reactive, onMounted } from "vue";
import { getProductList } from "@/controller/product.js";
import { getCategoryList } from "@/controller/category.js";
import { useRouter } from "vue-router";
import YesNoModal from "../components/YesNoModal.vue";

const router = useRouter();

const productList = async () => {
  try {
    const response = await getProductList();

    data.cards = response;
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
  productList();
  categoryList();
});

const routeTo = (path) => {
  router.push({ path: `/product/management/${path}` });
};

const deleteProduct = (productId, productName) => {
  data.modalProps = {
    enabled: true,
    title: "확인 필요",
    message: `정말 "${productName}" 제품을 삭제하시겠습니까?`,
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

const data = reactive({
  enableImg: true,
  selectedCategory: "",
  searchValue: "",
  categoryList: [],
  cards: [],
  modalProps: {
    enabled: false,
    title: "",
    message: "",
    confirmOnly: false,
  },
});
</script>
<style scoped>
.borderSolid {
  border: 1px solid rgb(118, 118, 118);
  /* box-shadow: 1px 1px 2px 2px rgb(118, 118, 118); */
}
</style>

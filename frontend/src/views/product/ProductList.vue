<template>
  <v-container style="max-width: 85%" class="mt-5">
    <v-row>
      <v-col cols="2">
        <v-card-title class="text-h4">
          <span>제품 검색</span>
        </v-card-title>
      </v-col>
      <v-col cols="2">
        <v-switch
          v-model="data.enableDense"
          hide-details
          inset
          :label="data.enableDense ? '작게보기' : '크게보기'"
        ></v-switch>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
    <v-row class="mt-4 mb-4">
      <v-toolbar color="white">
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
        :cols="getColSize(card)"
      >
        <v-card :class="getCardClass">
          <v-card-title>
            <span
              v-if="card.category"
              :class="getSubtitleSize"
              v-text="'#' + card.category.name"
            ></span>
            <span v-text="card.name" :class="getTitleSize"></span>
          </v-card-title>
          <v-img
            :src="
              card.img ? 'data:image/jpeg;base64,' + card.img : data.noImgSrc
            "
            v-if="data.enableDense"
            class="align-end"
            cover
            height="250px"
          >
          </v-img>

          <v-card-text
            :class="
              data.enableDense ? 'bg-card-text-content' : 'sm-card-text-content'
            "
          >
            <span
              v-text="
                card.description == null || card.description.length == 0
                  ? '등록된 설명이 없습니다.'
                  : card.description
              "
            ></span>
          </v-card-text>

          <v-container>
            <div class="d-flex flex-column">
              <span
                :class="boldClass"
                v-text="'판매가: ' + formatPrice(card.price) + '원'"
              ></span>
              <span
                :class="boldClass"
                v-text="'재고: ' + card.stock + '개'"
              ></span>
            </div>
            <div class="d-flex justify-end">
              <v-btn
                :class="btnWidthClass"
                size="large"
                color="surface-variant"
                variant="text"
                icon="mdi-pencil-circle"
              ></v-btn>
              <v-btn
                :class="btnWidthClass"
                size="large"
                color="surface-variant"
                variant="text"
                icon="mdi-delete-circle"
              ></v-btn>
            </div>
          </v-container>
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
import { reactive, onMounted, computed } from "vue";
import { getProductList } from "@/controller/product.js";
import { getCategoryList } from "@/controller/category.js";
import { useRouter } from "vue-router";
import YesNoModal from "../components/YesNoModal.vue";
import noImageSrc from "@/assets/images/No_Image_Available.jpg";

const router = useRouter();

const getColSize = (card) => (data.enableDense ? 3 : 2);
const getCardClass = computed(() =>
  data.enableDense
    ? "border-solid bg-card-height"
    : "border-solid sm-card-height"
);
const getTitleSize = computed(() =>
  data.enableDense ? "bg-title" : "sm-title"
);
const getSubtitleSize = computed(() =>
  data.enableDense ? "bg-subtitle float-right" : "sm-subtitle float-right"
);

const boldClass = computed(() =>
  data.enableDense ? "bg-bold align-self-end" : "sm-bold align-self-end"
);
const btnWidthClass = computed(() =>
  data.enableDense ? "bg-btn-width" : "sm-btn-width"
);

const formatPrice = (price) => {
  return price.toLocaleString();
};

const productList = async () => {
  try {
    const response = await getProductList();

    data.cards = response.content;
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
  noImgSrc: noImageSrc,
  currentStore: "",
  enableDense: true,
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
.border-solid {
  border: 1px solid rgb(118, 118, 118);
  /* box-shadow: 1px 1px 2px 2px rgb(118, 118, 118); */
}

.bg-title {
  font-size: 20px;
  font-weight: bold;
}

.sm-title {
  font-size: 15px;
  font-weight: bold;
}

.bg-subtitle {
  font-size: 15px;
  color: red;
  font-weight: bold;
}

.sm-subtitle {
  font-size: 12px;
  color: red;
  font-weight: bold;
}

.bg-card-height {
  height: 525px;
}

.bg-card-text-content {
  height: 100px;
  font-size: 16px;
  max-height: 150px;
  overflow-y: auto;
}

.bg-bold {
  font-weight: bold;
  font-size: 18px;
}

.bg-btn-width {
  width: 35px;
}

.sm-card-height {
  height: 260px; /* Example height, adjust as needed */
}

.sm-card-text-content {
  font-size: 13px;
  max-height: 100px;
  height: 100px;
  overflow-y: auto;
}

.sm-bold {
  font-weight: bold;
  font-size: 14px;
}

.sm-btn-width {
  width: auto;
}
</style>

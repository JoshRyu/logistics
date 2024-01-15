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
          v-model="data.biggerContents"
          hide-details
          inset
          :label="data.biggerContents ? '작게보기' : '크게보기'"
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
      <v-col v-for="card in data.cards" :key="card.name" :cols="getColSize()">
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
            v-if="data.biggerContents"
            class="align-end"
            cover
            height="250px"
          >
          </v-img>

          <v-card-text
            :class="
              data.biggerContents
                ? 'bg-card-text-content'
                : 'sm-card-text-content'
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
                @click="routeTo(card.productCode)"
              ></v-btn>
              <v-btn
                :class="btnWidthClass"
                size="large"
                color="surface-variant"
                variant="text"
                icon="mdi-delete-circle"
                @click="removeProduct(card.productCode, card.name)"
              ></v-btn>
            </div>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-spacer />
      <v-col cols="4"
        ><v-pagination
          v-model="data.currentPage"
          :length="data.lastPage"
          @click="productList('PRODUCT', data.currentPage - 1)"
        ></v-pagination
      ></v-col>
      <v-spacer />
    </v-row>
  </v-container>

  <YesNoModal
    :propObj="data.modalProps"
    @confirmed="handleConfirmation"
    @canceled="handleCancellation"
  />
</template>

<script setup>
import { reactive, onMounted, computed, watch } from "vue";
import { getProductList, deleteProduct } from "@/controller/product.js";
import { getCategoryList } from "@/controller/category.js";
import { useRouter } from "vue-router";
import YesNoModal from "../components/YesNoModal.vue";
import noImageSrc from "@/assets/images/No_Image_Available.jpg";

const router = useRouter();

const data = reactive({
  targetProductId: "",
  noImgSrc: noImageSrc,
  currentStore: "",
  biggerContents: true,
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
  currentPage: 1,
  lastPage: 1,
});

const getColSize = () => (data.biggerContents ? 3 : 2);
const getCardClass = computed(() =>
  data.biggerContents
    ? "border-solid bg-card-height"
    : "border-solid sm-card-height"
);
const getTitleSize = computed(() =>
  data.biggerContents ? "bg-title" : "sm-title"
);
const getSubtitleSize = computed(() =>
  data.biggerContents ? "bg-subtitle float-right" : "sm-subtitle float-right"
);

const boldClass = computed(() =>
  data.biggerContents ? "bg-bold align-self-end" : "sm-bold align-self-end"
);
const btnWidthClass = computed(() =>
  data.biggerContents ? "bg-btn-width" : "sm-btn-width"
);

const formatPrice = (price) => {
  return price.toLocaleString();
};

watch(
  () => data.biggerContents,
  (newVal, oldVal) => {
    if (newVal !== oldVal) {
      productList("PRODUCT", 0);
    }
  }
);

const productList = async (type, page) => {
  try {
    const response = await getProductList({
      type: type,
      page: page,
      size: data.biggerContents ? 8 : 12,
    });

    data.cards = response.content;
    data.lastPage = response.pageable.totalPages;
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
  productList("PRODUCT", 0);
  categoryList();
});

const routeTo = (path) => {
  router.push({ path: `/product/management/${path}` });
};

const removeProduct = (productId, productName) => {
  data.modalProps = {
    enabled: true,
    title: "확인 필요",
    message: `정말 "${productName}" 제품을 삭제하시겠습니까?`,
    confirmOnly: false,
  };
  data.targetProductId = productId;
};

const handleConfirmation = async () => {
  // Handle the confirmation action here
  if (data.targetProductId != "") {
    try {
      await deleteProduct(data.targetProductId);

      data.modalProps = {
        enabled: true,
        title: "성공",
        message: `제품이 성공적으로 삭제되었습니다.`,
        confirmOnly: true,
      };

      data.targetProductId = "";
      productList("PRODUCT", 0);
    } catch (error) {
      data.modalProps = {
        enabled: true,
        title: "실패",
        message: error.response.data.message,
        confirmOnly: true,
      };

      console.error(error);
    }
  } else {
    data.modalProps.enabled = false;
  }
};

const handleCancellation = () => {
  data.modalProps.enabled = false;
  data.targetProductId = "";
  // Handle the cancellation action here
};
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
  width: 40px;
  height: 40px;
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

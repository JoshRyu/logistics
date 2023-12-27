<template>
  <v-container style="max-width: 85%" class="mt-5">
    <v-row>
      <v-card-title class="text-h4">
        <span>매장: {{ data.currentStore }}</span>
      </v-card-title>
    </v-row>
    <v-row class="ml-1 mb-1">
      <v-chip-group
        filter
        v-model="data.currentStore"
        selected-class="text-amber-darken-4"
      >
        <v-chip v-for="store in data.storeList" :value="store" mandatory>
          {{ store }}</v-chip
        >
      </v-chip-group>
    </v-row>
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
        :key="card.title"
        :cols="getColSize(card)"
      >
        <v-card :class="getCardClass">
          <v-card-title>
            <span
              v-if="card.category"
              :class="getSubtitleSize"
              v-text="'#' + card.category"
            ></span>
            <span v-text="card.title" :class="getTitleSize"></span>
          </v-card-title>
          <v-img
            :src="card.src"
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
            v-if="card.memo"
          >
            <span v-text="card.memo"></span>
          </v-card-text>

          <v-container>
            <div class="d-flex flex-column">
              <span :class="boldClass" v-text="'판매가: ' + card.price"></span>
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
</template>

<script setup>
import { reactive, computed } from "vue";

const getColSize = (card) => (data.enableDense ? card.flex : 2);
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

const data = reactive({
  storeList: ["store1", "store2", "store3"],
  currentStore: "",
  enableDense: true,
  selectedCategory: "",
  searchValue: "",
  categoryList: ["뜨게 가방", "뜨게 케이스", "뜨게 지갑"],
  cards: [
    {
      title: "라탄 가방단지 토트백",
      category: "뜨게 가방",
      src: "/src/assets/images/고급라탄가방단지토드백.jpg",
      memo: "라탄 가방단지 토트백에 대한 내용입니다.",
      price: "80,000원",
      stock: 1,
      flex: 3,
    },
    {
      title: "과일파우치",
      category: "뜨게 가방",
      src: "/src/assets/images/과일파우치.jpg",
      memo: "과일파우치에 대한 내용입니다.",
      price: "13,000원",
      stock: 5,
      flex: 3,
    },
    {
      title: "몽글퍼토트백",
      category: "뜨게 가방",
      src: "/src/assets/images/몽글퍼토트백.jpg",
      memo: "몽글퍼토트백에 대한 내용입니다.",
      price: "20,000원",
      stock: 2,
      flex: 3,
    },
    {
      title: "새틴카드지갑",
      category: "뜨게 지갑",
      src: "/src/assets/images/새틴카드지갑.jpg",
      memo: "새틴카드지갑에 대한 내용입니다.",
      price: "17,000원",
      stock: 5,
      flex: 3,
    },
    {
      title: "토토로케이스",
      category: "뜨게 케이스",
      src: "/src/assets/images/토토로케이스.jpg",
      memo: "토토로케이스에 대한 내용입니다.",
      price: "20,000원",
      stock: 1,
      flex: 3,
    },
    {
      title: "파스텔파우치",
      category: "뜨게 가방",
      src: "/src/assets/images/파스텔파우치.jpg",
      memo: "파스텔파우치에 대한 내용입니다.asdasdaskdalskdskaldfhauilsdhfiashdfiulashdiufhasoidfhoipsadhfiashdfiasbgiuvdbgasidlvchbzxlkjcvhioasdhvuiahsidfhasidfhisalodhgfiasdugf",
      price: "18,000원",
      stock: 10,
      flex: 3,
    },
  ],
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
  height: 550px; /* Example height, adjust as needed */
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

<template>
  <v-card class="ma-5">
    <v-container fluid>
      <v-card dense>
        <v-card-title class="text-h5">
          <span>제품 검색</span>
        </v-card-title>

        <v-toolbar color="white" class="mb-5">
          <v-row>
            <v-col cols="2">
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
            <v-col cols="4">
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
            <v-col cols="2">
              <v-btn size="x-large" variant="outlined">검색</v-btn>
            </v-col>
            <v-col cols="4"> </v-col>
          </v-row>
        </v-toolbar>
      </v-card>
    </v-container>
    <v-container fluid>
      <v-row dense>
        <v-col
          v-for="card in data.cards"
          :key="card.title"
          :cols="data.cards.length >= 4 ? card.flex : 12"
        >
          <v-card class="borderSolid">
            <v-card-title class="text-h4">
              <span v-text="card.title"></span>
            </v-card-title>
            <v-card-subtitle class="text-h6 mb-1">
              <span
                v-if="card.category"
                class="float-right text-red"
                v-text="'#' + card.category"
              ></span>
            </v-card-subtitle>
            <v-img
              v-if="data.enableImg"
              :src="card.src"
              class="align-end"
              cover
            >
            </v-img>

            <v-card-text class="text-body-1 text-grey" v-if="card.memo">
              <span v-text="card.memo"></span>
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
              ></v-btn>
              <v-btn
                size="large"
                color="surface-variant"
                variant="text"
                icon="mdi-delete-circle"
              ></v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script setup>
import { reactive } from "vue";

const data = reactive({
  enableImg: true,
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
      category: "뜨게 가방",
      title: "파스텔파우치",
      src: "/src/assets/images/파스텔파우치.jpg",
      memo: "파스텔파우치에 대한 내용입니다.",
      price: "18,000원",
      stock: 10,
      flex: 3,
    },
  ],
});
</script>
<style scoped>
.borderSolid {
  border: 1px solid rgb(118, 118, 118);
}
</style>
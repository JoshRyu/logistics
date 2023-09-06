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
            :src="data.information.img ? data.information.img : data.noImgSrc"
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
                v-model="data.information.id"
                label="제품코드"
                hide-details
                placeholder="제품코드"
                variant="outlined"
                clear-icon="mdi-close-circle"
                clearable
                type="text"
              ></v-text-field>
            </v-col>
            <v-col cols="4">
              <v-text-field
                v-model="data.information.name"
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
                v-model="data.information.category"
                label="제품 카테고리"
                hide-details
                placeholder="제품 카테고리"
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
                v-model="data.information.price"
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
                v-model="data.information.stock"
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
                v-model="data.information.description"
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
import { reactive } from "vue";

const data = reactive({
  noImgSrc: "/src/assets/images/No_Image_Available.jpg",
  information: {
    img: null,
    id: "",
    name: "",
    category: "",
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

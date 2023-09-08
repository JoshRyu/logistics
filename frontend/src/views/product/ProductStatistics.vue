<template>
  <v-container style="max-width: 85%">
    <h1 class="mb-5">제품 관리 페이지</h1>
    <v-row>
      <v-col cols="3">
        <v-autocomplete
          v-model="data.selectedType"
          :items="data.statisticsType"
          hide-details
          variant="outlined"
          clear-icon="mdi-close-circle"
          clearable
          no-data-text="해당하는 통계가 없습니다."
          type="text"
        />
      </v-col>
    </v-row>
    <v-row v-if="data.enabledChart[0]">
      <v-col cols="8">
        <v-card class="borderSolid">
          <BarChart :chart-data="chartData"></BarChart>
        </v-card>
      </v-col>
      <v-col cols="4">
        <v-card class="borderSolid">
          <v-card-title>조회 조건 선택</v-card-title>
          <v-divider></v-divider>
          <v-card-text>상품 선택</v-card-text>
          <v-autocomplete
            v-model="data.selectedProductList"
            :items="data.productList"
            class="ml-4"
            label="카테고리 선택"
            variant="outlined"
            no-data-text="검색된 카테고리가 없습니다"
            multiple
            hide-details
            chips
            closable-chips
            clearable
          ></v-autocomplete>
          <v-card-text>기간 선택</v-card-text>
          <v-row>
            <v-col cols="6">
              <v-text-field
                v-model="data.startDate"
                label="시작일"
                type="date"
              ></v-text-field>
            </v-col>
            <v-col cols="6">
              <v-text-field
                v-model="data.endDate"
                label="종료일"
                type="date"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <v-row v-if="data.enabledChart[1]">
      <v-col cols="8">
        <v-card class="borderSolid">
          <LineChart :chart-data="chartData" />
        </v-card>
      </v-col>
      <v-col cols="4">
        <v-card class="borderSolid"></v-card>
      </v-col>
    </v-row>
    <v-row v-if="data.enabledChart[2] || data.enabledChart[3]">
      <v-col cols="8">
        <v-card class="borderSolid">
          <PieChart />
        </v-card>
      </v-col>
      <v-col cols="4">
        <v-card class="borderSolid"></v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { reactive, watchEffect, computed } from "vue";
import BarChart from "../components/Barchart.vue";
import LineChart from "../components/LineChart.vue";
import PieChart from "../components/PieChart.vue";

const data = reactive({
  startDate: new Date().toISOString().split("T")[0],
  endDate: new Date().toISOString().split("T")[0],
  selectedType: "상품 별 판매량",
  selectedProductList: [
    "라탄 가방단지 토트백",
    "과일파우치",
    "몽글퍼토트백",
    "새틴카드지갑",
    "토토로케이스",
    "파스텔파우치",
  ],
  productList: [
    "라탄 가방단지 토트백",
    "과일파우치",
    "몽글퍼토트백",
    "새틴카드지갑",
    "토토로케이스",
    "파스텔파우치",
  ],
  categoryList: ["뜨게 가방", "뜨게 지갑", "뜨게 케이스"],
  statisticsType: [
    "상품 별 판매량",
    "상품 별 재고",
    "총 매출",
    "카테고리 별 제품 수",
  ],
  enabledChart: [false, false, false, false],
});

watchEffect(() => {
  if (data.selectedType === "상품 별 판매량") {
    data.enabledChart = [true, false, false, false];
  }

  if (data.selectedType === "상품 별 재고") {
    data.enabledChart = [false, true, false, false];
  }

  if (data.selectedType === "총 매출") {
    data.enabledChart = [false, false, true, false];
  }

  if (data.selectedType === "카테고리 별 제품 수") {
    data.enabledChart = [false, false, false, true];
  }
});

const chartData = computed(() => {
  let datasets = [];

  data.selectedProductList.forEach((product) => {
    let standardObj = {
      label: "",
      backgroundColor: "black",
      pointBackgroundColor: "white",
      borderWidth: 0,
      fill: true,
      tension: 0.1,
      barPercentage: 0.7,
      data: [
        Math.floor(Math.random() * 100) + 1,
        Math.floor(Math.random() * 100) + 1,
        Math.floor(Math.random() * 100) + 1,
      ],
    };
    standardObj.label = product;
    standardObj.backgroundColor = getRandomHexColor();
    datasets.push(standardObj);
  });

  return {
    labels: ["January", "February", "March"],
    datasets: datasets,
  };
});

const getRandomHexColor = () => {
  const letters = "0123456789ABCDEF";
  let color = "#";
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
};
</script>
<style scoped>
.borderSolid {
  border: 1px solid rgb(118, 118, 118);
  padding: 5%;
}
</style>

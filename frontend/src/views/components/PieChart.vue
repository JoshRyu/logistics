<template>
  <Pie
    :height="540"
    id="my-chart-id"
    :options="data.chartOptions"
    :data="data.chartData"
  />
</template>
<script setup>
import { Pie } from "vue-chartjs";
import { reactive } from "vue";
import "@/plugins/chartconfig.js";

const data = reactive({
  chartData: {
    labels: ["January", "February", "March"],
    datasets: [{ data: [40, 20, 12] }],
  },
  chartOptions: {
    responsive: true,
    maintainAspectRatio: false,
  },
});
</script>

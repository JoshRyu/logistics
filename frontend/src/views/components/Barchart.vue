<template>
  <Bar :height="200" :options="chartOptions" :data="props.chartData" />
</template>
<script setup>
import { Bar } from "vue-chartjs";
import { computed } from "vue";
import "@/plugins/chartconfig.js";

const props = defineProps({
  chartData: Object,
});

let chartOptions = computed(() => {
  return {
    responsive: true,
  };
});
</script>

<template>
  <v-dialog v-model="data.dialog" max-width="450">
    <v-card>
      <v-card-title class="text-center">{{ propObj.title }}</v-card-title>
      <v-card-text class="text-center">
        {{ propObj.message }}
      </v-card-text>
      <v-card-actions class="justify-center">
        <v-btn color="primary" @click="confirm">확인</v-btn>
        <v-btn v-if="!propObj.confirmOnly" color="error" @click="cancel"
          >취소</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { reactive, defineProps, defineEmits, watchEffect } from "vue";

const props = defineProps({
  propObj: Object,
});

const emit = defineEmits(["confirmed", "canceled"]);

const data = reactive({ dialog: false });

watchEffect(() => {
  data.dialog = props.propObj.enabled;
});

const confirm = () => {
  emit("confirmed");
};

const cancel = () => {
  emit("canceled");
};
</script>

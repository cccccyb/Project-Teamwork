<template>
  <svg aria-hidden="true" :style="styles">
    <use :xlink:href="iconName" :fill="color" />
  </svg>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = defineProps({
  iconClass: {
    type: String,
    required: true,
  },
  color: {
    type: String,
    default: "#08bebe",
  },
  width: {
    type: String,
    default: "20px",
  },
  height: {
    type: String,
    default: "20px",
  },
});

const styles = computed(() => {
  return `width: ${props.width}; height:${props.height}`;
});

const iconName = computed(() => {
  return `#icon-${props.iconClass}`;
});
</script>


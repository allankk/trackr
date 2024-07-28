<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const msg = ref('');

onMounted(() => {
  fetch("/api/hello")
    .then((response) => response.text())
    .then((data) => {
      msg.value = data;
    });
});
</script>

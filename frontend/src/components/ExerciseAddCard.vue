<template>
  <div class="exercise-form">
    <h3>New Exercise</h3>
    <div>
      {{ content }}
    </div>
  </div>
</template>

<script setup>
import ActivityService from '@/services/ActivityService';
import { onMounted, ref } from 'vue';

const content = ref('')

onMounted(() => {
      ActivityService.getAuthHello().then(
      (response) => {
        console.log('response suc');
        content.value = response.data;
      },
      (error) => {
        console.log('error');
        content.value =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();
      }
    )

      fetch("/api/hello")
      .then((response) => response.text())
      .then((data) => {
        console.log('this is data' + data)
      });
});
</script>

<style scoped>
</style>

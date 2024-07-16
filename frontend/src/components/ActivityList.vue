<template>
  <div class="activity-list">
    <div v-for="activity in activityTypes" :key="activity.id" class="my-2">
        <div class="card flex row justify-between py-2">
            <h5 class="text-left">{{ activity.name }}</h5>
            <div class="grid metrics">
                <h6 class="font-bold">Units</h6>
                <div class="metrics-content flex flex-row">
                    <div v-for="metric in activity.metrics" :key="metric.id" class="flex flex-col mx-4">
                        <span class="font-bold">{{ metric.name }}</span>
                        <span>Unit: {{ metric.unit }}</span>
                    </div>
                </div>
            </div>
            <div v-if="activity.isDefault" class="font-bold">default</div>
            <div v-else></div>
        </div>
    </div>
  </div>
</template>

<script setup>
import ActivityService from '@/services/ActivityService';
import { onMounted, ref } from 'vue';

const activityTypes = ref('')

onMounted(() => {
      ActivityService.getAllActivities().then(
      (response) => {
        console.log(response.data);
        activityTypes.value = response.data;
      },
      (error) => {
        console.log('error');
        console.log(error.toString());
      }
    )
});
</script>

<style scoped>
</style>

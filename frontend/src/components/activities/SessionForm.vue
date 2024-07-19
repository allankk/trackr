<template>
    <div>
      <h1>Add Workout</h1>
      <form @submit.prevent="submitForm">
        <div>
          <label for="date">Date:</label>
          <Calendar v-model="date" :default-date="new Date()" />
        </div>
        <div>
          <label for="notes">Notes:</label>
          <textarea v-model="notes" id="notes"></textarea>
        </div>
        <div>
          <label for="activityGroup">Activity Group:</label>
          <select v-model="selectedActivityGroup">
            <option v-for="group in activityGroups" :key="group.id" :value="group.id">{{ group.name }}</option>
          </select>
          <button type="button" @click="addActivityGroup">Add Group</button>
        </div>
        <div>
          <label for="activity">Activity:</label>
          <select v-model="selectedActivity">
            <option v-for="activity in activityTypes" :key="activity.id" :value="activity.id">{{ activity.name }}</option>
          </select>
          <button type="button" @click="addActivity">Add Activity</button>
        </div>
        <div v-for="(activity, index) in selectedActivities" :key="index">
          <h3>{{ activity.name }}</h3>
          <div v-for="metric in activity.metrics" :key="metric.id">
            <label :for="'metric-' + metric.id">{{ metric.name }} ({{ metric.unit }}):</label>
            <input :id="'metric-' + metric.id" v-model="activity.metricValues[metric.id]" />
          </div>
        </div>
        <button type="submit">Submit</button>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import Calendar from 'primevue/calendar';
  import ActivityService from '@/services/ActivityService';
  import ActivityGroupService from '@/services/ActivityGroupService';
  
  const date = ref(new Date());
  const notes = ref('');
  const activityTypes = ref([]);
  const activityGroups = ref([]);
  const selectedActivityGroup = ref(null);
  const selectedActivity = ref(null);
  const selectedActivities = ref([]);
  
  onMounted(() => {
    ActivityService.getAllActivities().then(
      (response) => {
        activityTypes.value = response.data;
      },
      (error) => {
        console.error('Error fetching activity types:', error);
      }
    );
  
    ActivityGroupService.getAllActivityGroups().then(
      (response) => {
        activityGroups.value = response.data;
      },
      (error) => {
        console.error('Error fetching activity groups:', error);
      }
    );
  });
  
  const addActivityGroup = () => {
    const group = activityGroups.value.find(g => g.id === selectedActivityGroup.value);
    if (group) {
      group.activityTypes.forEach(activity => {
        const existingActivity = selectedActivities.value.find(a => a.id === activity.id);
        if (!existingActivity) {
          selectedActivities.value.push({ ...activity, metricValues: {} });
        }
      });
    }
  };
  
  const addActivity = () => {
    const activity = activityTypes.value.find(a => a.id === selectedActivity.value);
    if (activity && !selectedActivities.value.find(a => a.id === activity.id)) {
      selectedActivities.value.push({ ...activity, metricValues: {} });
    }
  };
  
  const submitForm = () => {
    const workout = {
      date: date.value,
      notes: notes.value,
      activities: selectedActivities.value.map(activity => ({
        id: activity.id,
        metrics: activity.metricValues
      }))
    };
    console.log('Workout:', workout);
    // Send workout data to the server...
  };
  </script>
  
  <style scoped>
  /* Add your styles here */
  </style>
  
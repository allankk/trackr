<template>
    <div class="max-w-4xl mx-auto">
      <form @submit.prevent="submitForm" class="mt-10">
        <div class="card mx-auto">
          <div class="mx-auto w-full md:w-3/4 flex flex-col justify-between items-center">
            <label for="calendar" class="w-full text-lg text-left mb-2">Date</label>
            <DatePicker class="shadow-none w-full px-0 py-0" v-model="calendarValue" name="calendar" :showIcon="true" :showButtonBar="true" dateFormat="dd.mm.yy"/>
          </div>
          <div class="mx-auto w-full md:w-3/4 flex flex-col justify-between items-center mt-4 mb-8">
            <label for="notes" class="w-full text-lg text-left mb-2">Notes</label>
            <Textarea class="w-full py-2" placeholder="Notes" :autoResize="true" rows="3" cols="30" />
          </div>
          <Divider/>
          <h5>Activities</h5>
          <div class="my-6">
            <Button icon="pi pi-plus" class="" rounded aria-label="Add Activity" @click="openActivityModal()" />
          </div>
          <div v-if="addedActivities.length" class="activity-card-container p-2">
            <div v-for="(activity, index) in addedActivities" :key="index" class="p-2">
              <SessionActivityCard 
                :activity="activity" 
                @remove-activity="handleRemoveActivity"
              />
            </div>
          </div>
          <Button rounded>Save</Button>
          <Dialog class="absolute h-full md:h-auto top-0 md:top-24 w-screen md:w-1/2 2xl:w-1/4" header="add activity to session" v-model:visible="activityModalVisible" :modal="true">
            <SessionAddActivityCard 
              :activityGroups="activityGroups"
              :activityTypes="activityTypes"
              @closeModal="closeActivityModal"
              @add-activity-group="handleAddActivityGroup"
              @add-activities="handleAddActivities"
            />
          </Dialog>
        </div>
        <button type="submit">Submit</button>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import DatePicker from 'primevue/datepicker';
  import Button from 'primevue/button';
  import Divider from 'primevue/divider';
  import Textarea from 'primevue/textarea';
  import Dialog from 'primevue/dialog';
  import SessionAddActivityCard from '@/components/activities/SessionAddActivityCard.vue';
  import SessionActivityCard from '@/components/activities/SessionActivityCard.vue';
  import ActivityService from '@/services/ActivityService';
  import ActivityGroupService from '@/services/ActivityGroupService';

  const activityModalVisible = ref(false);
  
  const calendarValue = ref(new Date());
  const notes = ref('');
  const activityTypes = ref([]);
  const activityGroups = ref([]);
  const addedActivities = ref([]);
  
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

  const openActivityModal = () => {
    activityModalVisible.value = true;
  }

  const closeActivityModal = () => {
    activityModalVisible.value = false;
  }

  const handleAddActivityGroup = (selectedActivityGroups) => {
    selectedActivityGroups.activityTypes.forEach((activity) => {
      const existingActivity = addedActivities.value.find(x => x.id === activity.id);

      if (!existingActivity) {
        addedActivities.value.push({ ...activity, metricValues: {} })
      }
    });

    closeActivityModal();
  };

  const handleAddActivities = (selectedActivities) => {
    selectedActivities.forEach(activity => {
      const existingActivity = addedActivities.value.find(x => x.id === activity.id);

      if (!existingActivity) {
        addedActivities.value.push({ ...activity, metricValues: {} })
      }
    })
    closeActivityModal();
  };

  const handleRemoveActivity = (activity) => {
    addedActivities.value = addedActivities.value.filter(x => x.id !== activity.id);
  }
  
  const submitForm = () => {
    const session = {
      date: calendarValue.value,
      notes: notes.value,
      activities: addedActivities.value.map(activity => ({
        id: activity.id,
        metrics: activity.metricValues
      }))
    };

    console.log('session save');
    console.log(session);
    //const workout = {
    //  date: calendarValue.value,
    //  notes: notes.value,
    //  activities: addedActivities.value.map(activity => ({
    //    id: activity.id,
    //    metrics: activity.metricValues
    //  }))
    //};
    //console.log('Workout:', workout);
  };
  </script>
  
  <style scoped>
  
  .activity-card-container {
      background-color: var(--surface-ground);
  }
  </style>
  
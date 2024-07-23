<template>
  <div class="max-w-4xl mx-auto">
    <form @submit.prevent="submitForm" class="mt-10">
      <div class="card mx-auto">
        <div class="mx-auto w-full md:w-3/4 flex flex-col justify-between items-center relative">
          <label for="calendar" class="w-full text-lg text-left mb-2">Date</label>
          <DatePicker class="shadow-none w-full px-0 py-0" v-model="calendarValue" name="calendar" :showIcon="true" dateFormat="dd.mm.yy"/>
          <span v-if="formErrors.calendarValue" class="text-red-500 text-sm text-bold absolute right-0 -bottom-6 font-bold">{{ formErrors.calendarValue }}</span>
        </div>
        <div class="mx-auto w-full md:w-3/4 flex flex-col justify-between items-center mt-4 mb-8">
          <label for="notes" class="w-full text-lg text-left mb-2">Notes</label>
          <Textarea v-model="notes" class="w-full py-2" placeholder="Notes" :autoResize="true" rows="3" cols="30" />
        </div>
        <Divider/>
        <h5>Activities</h5>
        <span v-if="formErrors.activities" class="text-red-500 text-sm font-bold">{{ formErrors.activities }}</span>
        <div class="my-6">
          <Button icon="pi pi-plus" class="" rounded aria-label="Add Activity" @click="openActivityModal()" />
        </div>
        <div v-if="addedActivities.length" class="activity-card-container p-2 mb-10">
          <div v-for="(activity, index) in addedActivities" :key="index" class="p-2">
            <SessionActivityCard 
              :activity="activity" 
              @remove-activity="handleRemoveActivity"
            />
          </div>
        </div>
        <Divider  />
        <div class="p-fluid w-full md:w-1/2 mt-8 mx-auto">
          <Button @click="submitForm" :disabled="isSubmitDisabled">Save</Button>
        </div>
        <Dialog class="absolute h-full md:h-auto top-0 md:top-24 w-screen md:w-1/2 2xl:w-1/4" header="add activity to session" v-model:visible="activityModalVisible" :modal="true" :draggable="false">
          <SessionAddActivityCard 
            :activityGroups="activityGroups"
            :activityTypes="activityTypes"
            @closeModal="closeActivityModal"
            @add-activity-group="handleAddActivityGroup"
            @add-activities="handleAddActivities"
          />
        </Dialog>
      </div>
    </form>
  </div>
</template>
  
<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from "vue-router";
import DatePicker from 'primevue/datepicker';
import Button from 'primevue/button';
import Divider from 'primevue/divider';
import Textarea from 'primevue/textarea';
import Dialog from 'primevue/dialog';
import SessionAddActivityCard from '@/components/activities/SessionAddActivityCard.vue';
import SessionActivityCard from '@/components/activities/SessionActivityCard.vue';
import ActivityService from '@/services/ActivityService';
import ActivityGroupService from '@/services/ActivityGroupService';
import ActivitySessionService from '@/services/ActivitySessionService';

const router = useRouter();
const route = useRoute();

const activityModalVisible = ref(false);
const calendarValue = ref(new Date());
const notes = ref('');
const activityTypes = ref([]);
const activityGroups = ref([]);
const addedActivities = ref([]);
const isSubmitDisabled = computed(() => addedActivities.value.length === 0);
const formErrors = ref({ calendarValue: null })

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

  ActivitySessionService.getSession(route.params.id).then(
    (response) => {
      const session = response.data;

      // Map the initial values
      calendarValue.value = new Date(session.date);
      notes.value = session.notes;

      addedActivities.value = session.activityTypes.map(activity => ({
        ...activity,
        metrics: activity.metrics.map(metric => ({
          id: metric.id,
          name: metric.metricName,
          selectedUnit: { id: metric.selectedUnitId, unit: metric.selectedUnitName },
          value: metric.value,
          units: metric.units
        }))
      }));
    },
    (error) => {
      console.error('Error fetching activitySession: ', error)
    }
  )
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
      addedActivities.value.push( activity )
    }
  });

  closeActivityModal();
};

const handleAddActivities = (selectedActivities) => {
  selectedActivities.forEach(activity => {
    const existingActivity = addedActivities.value.find(x => x.id === activity.id);

    if (!existingActivity) {
      addedActivities.value.push( activity )
    }
  })
  closeActivityModal();
};

const handleRemoveActivity = (activity) => {
  addedActivities.value = addedActivities.value.filter(x => x.id !== activity.id);
}
  
const submitForm = () => {
  if (!calendarValue.value) {
    formErrors.value.calendarValue = "Date is required";
    return;
  }

  if (addedActivities.value.length == 0) {
    formErrors.value.activities = "At least one activity is required";
    return;
  }

  const session = {
    date: calendarValue.value,
    notes: notes.value,
    activities: addedActivities.value.map(activity => ({
      id: activity.id,
      metrics: activity.metrics.map(metric => ({
        id: metric.id,
        unitId: metric.selectedUnit.id,
        value: metric.value
      }))
    }))
  };

  ActivitySessionService.updateSession(route.params.id, session).then(
    (response) => {
      if (response.status == 200) {
        router.push('/activity/sessions');
      }
    },
    (error) => {
      console.log('error');
      console.log(error);
    }
  );
};
</script>
  
<style scoped>
.activity-card-container {
    background-color: var(--surface-ground);
}
</style>
  
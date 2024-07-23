<template>
  <div v-if="groupedSessions.length" class="mx-auto max-w-5xl">
    <div v-for="group in groupedSessions" :key="group.date">
      <div class="text-xl font-bold text-left">{{ formatDate(group.date) }}</div>
      <div v-for="session in group.sessions" :key="session.id" class="session-container flex w-full flex-col p-4 my-4 border-2 rounded-lg shadow-md">
        <div class="flex justify-between">
          <div class="font-bold text-left">
            <span v-if="session.notes">{{ session.notes }}</span>
          </div>
          <div class="btn-container flex items-center ml-auto mb-2">
            <router-link :to="'/activity/sessions/edit/' + session.id">
              <Button icon="pi pi-pencil" severity="secondary" rounded outlined class="mx-2" />
            </router-link>
            <Button icon="pi pi-times" severity="secondary" rounded outlined class="mx-2" @click="openConfirmation(session)" />
          </div>
        </div>
        <div v-for="type in session.activityTypes" :key="type.id" class="activity-type grid grid-cols-3 rounded-lg w-full shadow-md justify-between items-center my-2 p-4 min-h-20 bg-white">
          <div class="text-lg col-span-3 md:col-span-1 font-bold text-left">{{ type.name }}</div>
          <div class="col-span-3 md:col-span-2">
            <div v-for="metric in type.metrics" :key="metric.id" class="text-left flex border-b-1">
              <div class="min-w-24 font-bold">{{ metric.metricName }}</div> 
              <span class="min-w-14 font-bold">{{ metric.value }}</span>
              <span>{{ metric.selectedUnitName }}s</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <Dialog header="Confirmation" v-model:visible="displayConfirmation" class="w-screen md:w-1/2 2xl:w-1/4" :modal="true">
        <div class="flex align-items-center justify-content-center">
            <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
            <span>Are you sure you want to delete this activity group?</span>
        </div>
        <template #footer>
            <Button label="Cancel" raised @click="closeConfirmation" class="mx-4" />
            <Button label="Delete" text raised @click="deleteSession" class="mr-4" />
        </template>
    </Dialog>
  </div>
  <div v-else>
    <p>No activities found.</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import ActivitySessionService from '@/services/ActivitySessionService';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';

const groupedSessions = ref([]);
const activeSession = ref(null);
const displayConfirmation = ref(false);

const formatDate = (dateString) => {
  const date = new Date(dateString);

  const dayFormatter = new Intl.DateTimeFormat('en-GB', { weekday: 'long' });

  const dayOfWeek = dayFormatter.format(date);

  return `${dateString} ${dayOfWeek}`;
};

const openConfirmation = (setActiveSession) => {
  activeSession.value = setActiveSession;

  console.log(activeSession.value);
  displayConfirmation.value = true;
}

const closeConfirmation = () => {
  displayConfirmation.value = false;
  activeSession.value = null;
}

const deleteSession = () => {
  ActivitySessionService.deleteSession(activeSession.value.id).then(
    (response) => {
      getAllSessions();
    },
    (error) => {
      console.log('error');
      console.log(error.toString());
    }
  )

  displayConfirmation.value = false;
  activeSession.value = null;
}

onMounted(() => {
  getAllSessions();
});

const getAllSessions = () => {
  ActivitySessionService.getAllSessions().then(
    (response) => {
      groupedSessions.value = response.data;
    },
    (error) => {
      console.log(error);
    }
  )
}
</script>

<style>
.btn-container button {
  background-color: white;
}

.btn-container button:hover {
  background-color: rgb(84, 95, 105);
}

.activity-type {
  background-color: #f8fafc;
}
</style>
<template>
  <div v-if="loading">
    <ProgressSpinner style="width: 50px; height: 50px" animationDuration=".5s"></ProgressSpinner>
  </div>
  <div v-else class="mx-auto max-w-5xl">
    <div class="flex flex-col md:flex-row justify-between mt-4 mb-10 items-center">
      <div class="w-full mb-4 md:flex-1 md:mr-4">
        <MultiSelect v-model="selectedActivityTypes" :options="activityTypes" option-label="name"
          placeholder="Select Activity Types" @change="fetchSessions"
          class="activity-select w-full md:min-w-60 md:max-w-60" />
      </div>
      <div class="w-full mb-4 flex-1 mx-4">
        <DatePicker v-model="dateRange" selection-mode="range" showButtonBar placeholder="Select Date Range"
          @clear-click="fetchSessions" @date-select="fetchSessions" dateFormat="dd.mm.yy"
          class="date-picker shadow-none w-full px-0 py-0 cursor-pointer text-center" />
      </div>
      <div class="w-full flex-1 mb-4 md:ml-4 flex items-center justify-center">
        <button @click="toggleSortOrder" class="sort-button border rounded-md bg-white w-full min-w-40 py-2 px-6">
          <span class="px-2">{{ sortOrder === 'asc' ? 'sort by oldest' : 'sort by newest' }}</span>
          <i :class="sortOrder === 'asc' ? 'pi pi-sort-up' : 'pi pi-sort-down'"></i>
        </button>
      </div>
    </div>
    <div v-if="groupedSessions.length">
      <div v-for="group in groupedSessions" :key="group.date" class="mb-16">
        <div class="text-xl font-bold text-left">{{ formatDate(group.date) }}</div>
        <div v-for="session in group.sessions" :key="session.id"
          class="session-container flex w-full flex-col p-4 my-4 border rounded-xl shadow-xl">
          <div class="flex justify-between">
            <div class="font-bold text-left">
              <span v-if="session.notes">{{ session.notes }}</span>
            </div>
            <div class="btn-container flex items-center ml-auto mb-2">
              <router-link :to="'/activity/sessions/edit/' + session.id">
                <Button icon="pi pi-pencil" severity="secondary" rounded outlined class="mx-2" />
              </router-link>
              <Button icon="pi pi-times" severity="secondary" rounded outlined class="mx-2"
                @click="openConfirmation(session)" />
            </div>
          </div>
          <div v-for="type in session.activityTypes" :key="type.id"
            class="activity-type grid grid-cols-3 rounded-lg w-full border-2 justify-between items-center my-2 p-4 min-h-20 bg-white">
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
      <Dialog header="Confirmation" v-model:visible="displayConfirmation" class="w-screen md:w-1/2 2xl:w-1/4"
        :modal="true">
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import ActivitySessionService from '@/services/ActivitySessionService';
import ActivityService from '@/services/ActivityService';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import MultiSelect from 'primevue/multiselect';
import DatePicker from 'primevue/datepicker';
import ProgressSpinner from 'primevue/progressspinner';

const groupedSessions = ref([]);
const activeSession = ref(null);
const displayConfirmation = ref(false);

const dateRange = ref(null);
const selectedActivityTypes = ref([]);
const activityTypes = ref([]);
const sortOrder = ref('desc');

const loading = ref(true);

const formatDate = (dateString) => {
  const date = new Date(dateString);

  const dayFormatter = new Intl.DateTimeFormat('en-GB', { weekday: 'long' });

  const dayOfWeek = dayFormatter.format(date);

  return `${dateString} ${dayOfWeek}`;
};

const openConfirmation = (setActiveSession) => {
  activeSession.value = setActiveSession;
  displayConfirmation.value = true;
}

const closeConfirmation = () => {
  displayConfirmation.value = false;
  activeSession.value = null;
}

const toggleSortOrder = () => {
  sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  sortSessions();
}

const deleteSession = () => {
  ActivitySessionService.deleteSession(activeSession.value.id).then(
    () => {
      fetchSessions();
    },
    (error) => {
      console.log('error');
      console.log(error.toString());
    }
  )

  displayConfirmation.value = false;
  activeSession.value = null;
}

// Fetch sessions based on filters
const fetchSessions = () => {
  loading.value = true;
  const startDate = dateRange.value?.[0]?.toISOString() || null;
  const endDate = dateRange.value?.[1]?.toISOString() || dateRange.value?.[0] || null;

  const activityTypeIds = selectedActivityTypes.value.map(type => type.id);

  const filterRequest = {
    sortOrder: sortOrder.value,
    startDate,
    endDate,
    activityTypes: activityTypeIds
  };

  ActivitySessionService.getFilteredSessions(filterRequest).then(
    (response) => {
      groupedSessions.value = response.data;
      sortSessions();
    },
    (error) => {
      console.log('error');
      console.log(error.toString());
    }
  ).finally(() => {
    loading.value = false;
  });
};

// Fetch activity types
const fetchActivityTypes = () => {
  ActivityService.getAllActivities().then(
    (response) => {
      activityTypes.value = response.data;
    },
    (error) => {
      console.log('error');
      console.log(error.toString());
    }
  );
};

const sortSessions = () => {
  const order = sortOrder.value === 'asc' ? 1 : -1;
  groupedSessions.value.sort((a, b) => {
    const dateA = new Date(a.date);
    const dateB = new Date(b.date);
    return (dateA - dateB) * order;
  });
};

onMounted(() => {
  fetchActivityTypes();
  fetchSessions();
});

</script>

<style>
.btn-container button {
  background-color: white;
}

.btn-container button:hover {
  background-color: rgb(84, 95, 105);
}

.session-container {
  background-color: var(--surface-ground);
}

.activity-type {
  background-color: #f8fafc;
}

.sort-button {
  border: 1px solid var(--p-inputtext-border-color);
  color: var(--p-multiselect-placeholder-color);
}

.sort-button:hover {
  border-color: var(--p-inputtext-hover-border-color);
}
</style>
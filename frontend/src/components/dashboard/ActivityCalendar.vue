<template>
  <div
    class="w-full lg:max-w-7xl p-0 md:p-4 flex flex-col md:flex-row mx-auto justify-between items-center md:items-start">
    <div class="activity-calendar rounded-md w-full xl:w-1/3 pt-1">
      <DatePicker v-model="selectedDate" inline :pt="calendarPT"
        class="w-full border-0 mt-12 shadow-lg min-w-72 bg-white" />
    </div>

    <SessionSummary  :sessions="sessionSummary" :selectedDate="selectedDate" :loading="loading"
      class="w-full xl:w-1/2 2xl:w-2/3 m-2 md:mr-0" />
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import DashboardService from '@/services/DashboardService';
import SessionSummary from '@/components/dashboard/SessionSummary.vue';
import DatePicker from 'primevue/datepicker';
import { useToast } from "primevue/usetoast";

const toast = useToast();

const selectedDate = ref(null);
const sessionDates = ref([]);
const sessionSummary = ref([]);

const loading = ref(true);

const calendarPT = {
  day: (date) => {
    const { day, month, year } = date.context.date;
    const isHighlighted = sessionDates.value.some(d =>
      d.getFullYear() === year &&
      d.getMonth() === month &&
      d.getDate() === day
    );

    return {
      class: isHighlighted ? 'highlighted-day' : ''
    };
  }
};

onMounted(() => {
  DashboardService.getSessionDates().then(
    (response) => {
      let latestDate = null;
      if (response.data.dates) {
        sessionDates.value = response.data.dates.map(d => {
          let date = new Date(d);

          if (date > latestDate) latestDate = date;
          return date;
        });

        selectedDate.value = latestDate;
      }
    },
    (error) => {
      toast.add({ severity: 'error', summary: 'Error', detail: error.message || 'Failed to retrieve session dates', life: 3000 });
    }
  );
})

watch(selectedDate, async (newDate) => {
  loading.value = true;
  if (newDate) {
    try {
      const response = await DashboardService.getSessionByDate(newDate.toISOString());
      sessionSummary.value = response.data;
      loading.value = false;

    } catch (error) {
      toast.add({ severity: 'error', summary: 'Error', detail: error.message || 'Failed to retrieve session', life: 3000 });
      sessionSummary.value = [];
    }
  } else {
    sessionSummary.value = [];
  }
  
});

</script>

<style>
.p-datepicker-panel.p-component {
  border: none;
}

.activity-calendar .highlighted-day {
  background: #10b981;
  font-weight: bold;
  color: white;
}

.activity-calendar .p-datepicker-day-cell .p-datepicker-day.highlighted-day:hover {
  background: #0ca270;
  font-weight: bold;
  color: white;
}
</style>

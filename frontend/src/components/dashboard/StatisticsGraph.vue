<template>
  <div class="chart-container w-full md:max-w-7xl p-4 mx-auto">
    <div class="bg-white p-4 w-full shadow-lg mx-auto">
      <div class="text-lg font-bold mb-4">Activity Statistics</div>
      <div class="w-full flex flex-col md:flex-row justify-center mb-4 md:flex-1 md:mr-4">
        <div class="flex flex-col my-2 text-left md:text-center">
          <div class="mb-2 italic">Activity</div>
          <Select v-model="selectedActivityType" :options="activityTypes" optionLabel="name"
            placeholder="Select Activity" class="md:max-w-60 min-w-40 mx-4 text-left" @change="getMetrics" />
        </div>
        <div class="flex flex-col my-2">
          <div class="mb-2 italic text-left md:text-center">Metric</div>
          <Select v-model="selectedMetric" :options="metrics" optionLabel="name" placeholder="Select Metric"
            class="md:max-w-60 min-w-40 mx-4 text-left" @change="getUnits" />
        </div>
        <div class="flex flex-col my-2">
          <div class="mb-2 italic text-left md:text-center">Unit</div>
          <Select v-model="selectedUnit" :options="units" optionLabel="name" placeholder="Select Unit"
            class="md:max-w-60 min-w-40 mx-4 text-left" @change="getActivityResults" />
        </div>
      </div>
      <Chart type="line" :data="lineData" :options="lineOptions" class="md:p-6 mx-auto" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import ActivityService from '@/services/ActivityService.ts';
import DashboardService from '@/services/DashboardService.ts';
import moment from 'moment';
import Select from 'primevue/select';
import Chart from 'primevue/chart';
import { useToast } from 'primevue/usetoast';

const toast = useToast();

const activityTypes = ref([]);
const selectedActivityType = ref(null);
const metrics = ref([]);
const selectedMetric = ref(null);
const units = ref([]);
const selectedUnit = ref(null);

const lineData = reactive({
  labels: [],
  datasets: []
});

const lineOptions = {
  plugins: {
    legend: {
      display: false,
    }
  },
}

const getMetrics = () => {
  units.value = [];
  selectedUnit.value = null;

  if (selectedActivityType.value) {
    metrics.value = selectedActivityType.value.metrics;
    selectedMetric.value = metrics.value[0];
    getUnits();
  }
}

const getUnits = () => {
  if (selectedMetric.value) {
    units.value = selectedMetric.value.units;
    selectedUnit.value = units.value[0];
    getActivityResults();
  }
}

const getActivityResults = async () => {
  DashboardService.getActivityResults(selectedActivityType.value.id, selectedMetric.value.id, selectedUnit.value.id).then(
    (response) => {
      lineData.labels = ['January', 'February', 'March']

      lineData.labels = response.data.dates.map(d => moment(new Date(d)).format('DD.MM.yyyy'));
      lineData.datasets = [
        {
          label: 'value',
          data: response.data.results,
          fill: false,
          backgroundColor: '#00bb7e',
          borderColor: '#00bb7e',
          tension: 0.3
        }
      ];
    },
    (error) => {
      toast.add({ severity: 'error', summary: 'Error', detail: error.message || 'Failed to retrieve sessions', life: 3000 });
    }
  )
}

onMounted(() => {
  ActivityService.getAllActivities().then(
    (response) => {
      activityTypes.value = response.data;
      selectedActivityType.value = activityTypes.value[0];
      getMetrics();
    },
    (error) => {
      toast.add({ severity: 'error', summary: 'Error', detail: error.message || 'Failed to retrieve activities', life: 3000 });
    }

  )

})
</script>

<style scoped>
.chart-container {
  min-height: 400px;
}

@media (min-width: 1024px) {
  .chart-container {
    min-height: 600px;
  }
}

@media (min-width: 1280px) {
  .chart-container {
    min-height: 800px;
  }
}
</style>

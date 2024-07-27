<template>
  <div class="activity-type mx-auto max-w-5xl bg-white">
    <div class="bg-white px-2 md:px-10 py-4 rounded-xl shadow-sm grid grid-cols-10 relative">
      <div class="flex text-left flex-col md:flex-row col-span-10 md:col-span-2">
        <span class="text-left font-bold text-xl my-2 md:my-auto mx-auto md:mx-0">{{ activity.name }}</span>
      </div>
      <div class="flex flex-col gap-y-4 px-2 col-span-10 md:col-span-6">
        <div v-for="metric in activity.metrics" :key="metric.id" class="flex flex-col items-center">
          <label :for="'metricvalue' + metric.id">{{ metric.name }}</label>
          <InputGroup :name="'metricvalue-' + metric.id"
            class="flex justify-center relative mt-2 max-h-10 w-60 max-w-60">
            <InputNumber :id="'metric-' + metric.id" v-model="metric.value" :useGrouping="false" :maxFractionDigits="2"
              class="w-full h-10 " />
            <InputGroupAddon class="absolute border-none p-0">
              <Select v-model="metric.selectedUnit" :options="metric.units" optionLabel="unit" placeholder="Select"
                class="max-h-10 max-w-24 border-none" />
            </InputGroupAddon>
          </InputGroup>
        </div>
      </div>
      <div class="flex items-center ml-auto col-span-2 absolute md:static right-2 top-4">
        <Button icon="pi pi-times" severity="secondary" rounded outlined class="mx-2"
          @click="openConfirmation(activity)" />
      </div>
    </div>
  </div>
  <div>
  </div>
  <Dialog header="Confirmation" v-model:visible="displayConfirmation" class="w-screen md:w-1/2 2xl:w-1/4" :modal="true">
    <div class="flex align-items-center justify-content-center">
      <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
      <span>Remove activity from session?</span>
    </div>
    <template #footer>
      <Button label="Cancel" raised @click="closeConfirmation" class="mx-4" />
      <Button label="Delete" text raised @click="removeActivity()" class="mr-4" />
    </template>
  </Dialog>
</template>

<script setup>
import { toRefs, ref } from 'vue';
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import Select from 'primevue/select';

const props = defineProps({
  activity: Object
});

const emit = defineEmits(['remove-activity']);
const { activity } = toRefs(props);
const displayConfirmation = ref(false);

activity.value.metrics.forEach(metric => {
  if (metric.selectedUnit && metric.selectedUnit.id) {
    metric.selectedUnit = metric.units.find(unit => unit.id === metric.selectedUnit.id) || metric.units[0];
  } else {
    metric.selectedUnit = metric.units[0];
  }

  if (!metric.selectedUnit) {
    metric.selectedUnit = metric.units[0];
  }

  if (metric.value == undefined) {
    metric.value = null;
  }
});

const openConfirmation = () => {
  displayConfirmation.value = true;

}

const closeConfirmation = () => {
  displayConfirmation.value = false;
}

const removeActivity = () => {
  emit('remove-activity', props.activity);
  displayConfirmation.value = false;
}
</script>

<style>
.activity-type .p-inputtext.p-component.p-inputnumber-input {
  text-align: center;
  padding: 0;
  border-radius: 0.5rem;
  padding-left: 1rem;
  padding-right: 6rem;
}

.activity-type .p-inputgroup {
  align-items: center;
  padding-left: 2rem;
  margin-right: 2rem;
}

.activity-type .p-inputgroup .p-inputgroupaddon {
  padding: 0;
  border: none;
  width: 5rem;
  position: absolute;
  right: 0.25rem;
  padding-right: 0.25rem;
  z-index: 5;
}

.activity-type .p-inputgroup .p-inputgroupaddon .p-select {
  border: none;
  max-height: 2.25rem;
  padding: 0;
  box-shadow: none;
}

.activity-type .p-inputgroup .p-inputgroupaddon .p-select .p-select-label {
  padding: 0;
  border-left: 1px solid var(--gray-300);
}

.activity-type .p-inputgroup .p-inputgroupaddon .p-select .p-select-dropdown {
  width: 1rem;
}

.activity-type .p-select-dropdown-icon {
  max-width: 0.75rem;
}
</style>
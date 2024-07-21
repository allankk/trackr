<template>
    <div class="activity-type mx-auto max-w-5xl bg-white">
        <div class="bg-white px-2 md:px-10 py-4 rounded-xl shadow-sm grid grid-cols-10 relative">
            <div class="flex text-left flex-col md:flex-row col-span-10 md:col-span-2">
              <span class="text-left font-bold text-xl my-2 md:my-auto mx-auto md:mx-0">{{ activity.name }}</span>
            </div>
            <div class="grid grid-cols-3 gap-y-4 px-2 col-span-10 md:col-span-6">
              <div v-for="metric in activity.metrics" :key="metric.id" class="flex flex-col col-span-3 md:col-span-1 gap-2 mx-4 items-center flex-1">
                <label :for="'metric-' + metric.id" class="text-nowrap">{{ metric.name }} ({{ metric.unit }})</label>
                <InputNumber :id="'metric-' + metric.id" v-model="activity.metricValues[metric.id]" :useGrouping="false" />
              </div>
            </div>
            <div class="flex items-center ml-auto col-span-2 absolute md:static right-2 top-4">
              <Button icon="pi pi-times" severity="secondary" rounded outlined class="mx-2" @click="openConfirmation(activity)" />
            </div>
        </div>
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

const props = defineProps({
  activity: Object
});

const emit = defineEmits(['remove-activity']);
const { activity } = toRefs(props);
const displayConfirmation = ref(false);

const openConfirmation = () => {
  displayConfirmation.value = true;
}

const closeConfirmation = () => {
  displayConfirmation.value = false;
}

const removeActivity = () => {
  emit('remove-activity', props.activity);
}
</script>

<style>
.activity-type .p-inputtext.p-component.p-inputnumber-input {
  text-align: center;
  max-width: 5rem;
}
</style>
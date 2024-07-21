<template>
    <div class="p-fluid">
        <SelectButton class="w-full" v-model="selectedOption" :options="selectOptions" optionLabel="name" />
    </div>
    <div v-if="selectedOption.value == 'add-activity'" class="p-fluid flex flex-col">
      <label for="activity" class="my-4 font-bold mx-auto">choose activities</label>
      <Listbox v-model="selectedActivities" :options="activityTypes" multiple optionLabel="name" class="w-full md:w-96 mx-auto" />
      <div class="md:px-32 mt-10 mb-4 flex flex-col justify-end gap-2">
        <Button type="submit" label="Add" class="" @click="$emit('add-activities', selectedActivities)"></Button>
        <Button type="button" label="Cancel" text severity="secondary" @click="$emit('closeModal')"></Button>
      </div>
    </div>
    <div v-else class="p-fluid flex flex-col">
      <label for="activityGroup" class="my-4 font-bold mx-auto">choose activity group</label>
      <Listbox v-model="selectedActivityGroup" :options="activityGroups" optionLabel="name" class="w-full md:w-96 mx-auto" />
      <div class="md:px-32 mt-10 mb-4 flex flex-col justify-end gap-2">
        <Button type="submit" label="Add" class="" @click="$emit('add-activity-group', selectedActivityGroup)"></Button>
        <Button class="" type="button" text label="Cancel" severity="secondary" @click="$emit('closeModal')"></Button>
      </div>
    </div>
</template>

<script setup>
import { ref, toRefs } from 'vue';
import SelectButton from 'primevue/selectbutton';
import Listbox from 'primevue/listbox';
import Button from 'primevue/button';

const props = defineProps({
  activityGroups: Array,
  activityTypes: Array,
});

const emit = defineEmits(['closeModal', 'add-activity-group', 'add-activities']);
const { activityGroups, activityTypes } = toRefs(props);

const selectOptions = ref([{ name: 'add activity', value: 'add-activity' }, { name: 'add from group', value: 'add-group' }]);
const selectedOption = ref(selectOptions.value[0]);
const selectedActivityGroup = ref(null);
const selectedActivities = ref([]);
</script>

<style>
.p-togglebutton.p-component {
    width: 50%;
}
</style>
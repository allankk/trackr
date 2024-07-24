<template>
  <div class="activity-list mx-auto max-w-5xl">
    <div v-for="activity in activityTypes" :key="activity.id" class="my-2">
        <div class="bg-white px-4 md:px-20 py-4 rounded-xl shadow-sm flex items-center flex-row justify-between">
            <div class="flex text-left flex-col md:flex-row">
              <span class="text-left font-bold text-xl">{{ activity.name }}</span>
              <span v-if="activity.isDefault" class="md:ml-6 inline-flex items-center">default</span>
            </div>
            <div v-if="!activity.isDefault" class="flex items-center">
              <Button icon="pi pi-pencil" severity="secondary" rounded outlined class="mx-2" @click="openEditDialog(activity)" />
              <Button icon="pi pi-times" severity="secondary" rounded outlined class="mx-2" @click="openConfirmation(activity)" />
            </div>
        </div>
    </div>
    <Dialog header="Confirmation" v-model:visible="displayConfirmation" class="w-screen md:w-1/2 2xl:w-1/4" :modal="true">
        <div class="flex align-items-center justify-content-center">
            <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
            <span>Are you sure you want to delete this activifty?</span>
        </div>
        <template #footer>
            <Button label="Cancel" raised @click="closeConfirmation" class="mx-4" />
            <Button label="Delete" text raised @click="deleteActivity" class="mr-4" />
        </template>
    </Dialog>
    <Dialog header="edit activity type" class="w-full md:w-1/2 lg:w-1/3 absolute md:static top-20" v-model:visible="displayEdit" 
         :modal="true" :draggable="false">
            <ActivityTypeEditCard :visible="displayEdit" :activity="activeType" @closeModal="closeEditDialog()"/>
    </Dialog>
  </div>
</template>

<script setup>
import ActivityService from '@/services/ActivityService';
import ActivityTypeEditCard from '@/components/activities/ActivityTypeEditCard';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import { onMounted, ref } from 'vue';

const activityTypes = ref('');
const activeType = ref(null);
const displayConfirmation = ref(false);
const displayEdit = ref(false);

const openEditDialog = (activity) => {
  activeType.value = activity;
  displayEdit.value = true;
}

const closeEditDialog = () => {
  displayEdit.value = false;
  activeType.value = null;
  getAllActivities();
}

const openConfirmation = (setActiveType) => {
  activeType.value = setActiveType;
  displayConfirmation.value = true;
}

const closeConfirmation = () => {
  displayConfirmation.value = false;
  activeType.value = null;
}

const deleteActivity = () => {
  ActivityService.deleteActivityType(activeType.value.id).then(
    (response) => {
      getAllActivities();
    },
    (error) => {
      console.log('error');
      console.log(error.toString());
    }
  )

  displayConfirmation.value = false;
  activeType.value = null;
}

onMounted(() => {
  getAllActivities();
});

const getAllActivities = () => {
  ActivityService.getAllActivities().then(
      (response) => {
        activityTypes.value = response.data;
      },
      (error) => {
        console.log('error');
        console.log(error.toString());
      }
    )
}
</script>

<style scoped>
</style>

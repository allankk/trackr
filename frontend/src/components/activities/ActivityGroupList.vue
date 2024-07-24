<template>
  <div class="activity-list mx-auto max-w-5xl">
    <div v-for="activityGroup in activityGroups" :key="activityGroup.id" class="my-2">
        <div class="bg-white px-4 md:px-20 py-4 rounded-xl shadow-sm flex items-center flex-row justify-between">
            <div class="flex text-left flex-col md:flex-row">
              <span class="text-left font-bold text-xl">{{ activityGroup.name }}</span>
            </div>
            <div class="flex items-center">
              <Button icon="pi pi-pencil" severity="secondary" rounded outlined class="mx-2" @click="openEditDialog(activityGroup)" />
              <Button icon="pi pi-times" severity="secondary" rounded outlined class="mx-2" @click="openConfirmation(activityGroup)" />
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
            <Button label="Delete" text raised @click="deleteActivityGroup" class="mr-4" />
        </template>
    </Dialog>
    <Dialog header="edit activity group" class="w-full md:w-1/2 lg:w-1/3 absolute md:static top-20" v-model:visible="displayEdit" 
         :modal="true" :draggable="false">
            <ActivityGroupEditCard :visible="displayEdit" :activityGroup="activeGroup" @closeModal="closeEditDialog()"/>
    </Dialog>
  </div>
</template>

<script setup>
import ActivityGroupService from '@/services/ActivityGroupService';
import ActivityGroupEditCard from '@/components/activities/ActivityGroupEditCard';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import { onMounted, ref } from 'vue';

const activityGroups = ref([]);
const activeGroup = ref(null);
const displayConfirmation = ref(false);
const displayEdit = ref(false);

const openEditDialog = (activityGroup) => {
  activeGroup.value = activityGroup;
  displayEdit.value = true;
}

const closeEditDialog = () => {
  displayEdit.value = false;
  activeGroup.value = null;
  getAllActivityGroups();
}

const openConfirmation = (setActiveGroup) => {
  activeGroup.value = setActiveGroup;
  displayConfirmation.value = true;
}

const closeConfirmation = () => {
  displayConfirmation.value = false;
  activeGroup.value = null;
}

const deleteActivityGroup = () => {
  ActivityGroupService.deleteActivityGroup(activeGroup.value.id).then(
    (success) => {
      getAllActivityGroups();
    },
    (error) => {
      console.log('error');
      console.log(error.toString());
    }
  )

  displayConfirmation.value = false;
  activeGroup.value = null;
}

onMounted(() => {
  getAllActivityGroups();
});

const getAllActivityGroups = () => {
  ActivityGroupService.getAllActivityGroups().then(
      (response) => {
        activityGroups.value = response.data;
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

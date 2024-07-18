<template>
    <div>
        <h3 class="font-bold">activity types</h3>
        <Button type="button" rounded class="my-10" @click="visible = true">
            <i class="pi pi-fw pi-plus"></i>
            <span class="font-bold">add activity</span>
        </Button>
        <Dialog header="add activity type" class="w-full md:w-1/2 lg:w-1/3 absolute md:static top-20" v-model:visible="visible" 
         :modal="true" :draggable="false">
            <ActivityTypeAddCard :visible="visible" @closeModal="visible = false"/>
        </Dialog>
        <ActivityList />    
    </div>
</template>

<script setup>
import { ref } from 'vue';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import ActivityTypeAddCard from '@/components/activities/ActivityTypeAddCard.vue';
import ActivityList from '@/components/activities/ActivityList.vue';

const visible = ref(false);

</script>
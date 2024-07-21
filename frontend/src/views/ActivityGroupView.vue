<template>
    <div>
        <h3 class="font-bold">activity groups</h3>
        <Button type="button" rounded class="my-10" @click="visible = true">
            <i class="pi pi-fw pi-plus"></i>
            <span class="font-bold">create activity group</span>
        </Button>
        <Dialog header="add activity group" class="w-full md:w-1/2 lg:w-1/3 absolute md:static top-20" v-model:visible="visible" 
         :modal="true" :draggable="false">
            <ActivityGroupAddCard :visible="visible" @closeModal="visible = false"/>
        </Dialog>
        <ActivityGroupList />    
    </div>
</template>

<script setup>
import { ref } from 'vue';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import ActivityGroupAddCard from '@/components/activities/ActivityGroupAddCard.vue';
import ActivityGroupList from '@/components/activities/ActivityGroupList.vue';

const visible = ref(false);

</script>
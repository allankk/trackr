<template>
    <div>
        <h3 class="font-bold">sessions</h3>
        <router-link class="px-6" to="/activity/sessions/create">
            <Button type="button" rounded class="my-10" @click="visible = true">
                <i class="pi pi-fw pi-plus"></i>
                <span class="font-bold">new session</span>
            </Button>
        </router-link>
        <router-view></router-view>
        <ActivityList />    
    </div>
</template>

<script setup>
import { ref } from 'vue';
import Button from 'primevue/button';
import ActivityList from '@/components/activities/ActivityList.vue';

const visible = ref(false);

</script>
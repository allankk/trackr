<template>
    <div>
        <h3 class="font-bold">Exercises</h3>
        <Button type="button" rounded class="my-10" @click="showForm = !showForm">
            <i class="pi pi-fw pi-plus"></i>
            <span class="font-bold">add exercise</span>
        </Button>
        <ExerciseAddCard v-if="showForm" @close="showForm = false" />
        <ActivityList></ActivityList>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import Button from 'primevue/button';
import ExerciseAddCard from '@/components/ExerciseAddCard.vue';
import ActivityList from '@/components/ActivityList.vue';

const showForm = ref(false);

</script>
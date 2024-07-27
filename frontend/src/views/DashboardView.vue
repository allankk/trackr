<template>
    <div>
        <h3 class="font-bold">dashboard</h3>
        <ActivityCalendar />
        <StatisticsGraph />
    </div>
</template>

<script setup>
import ActivityCalendar from '@/components/dashboard/ActivityCalendar';
import StatisticsGraph from '@/components/dashboard/StatisticsGraph.vue';

</script>
<template>
  <div class="session-summary">
    <div class="font-bold text-lg mt-10 md:mt-0">{{ formattedDate }} summary</div>
    <div v-if="sessions.length">
      <div v-for="(session, index) in sessions" :key="index" class="session flex flex-col p-2 pr-0">
        <div v-for="(s, i) in session.sessions" :key="i" class="session-details bg-white my-2 shadow-lg flex-col">
          <p class="notes p-1 w-full">{{ s.notes }}</p>
          <div class="flex">
            <div v-for="(activityType, j) in s.activityTypes" :key="j" class="p-2">
              <p class="activity-name">{{ activityType.name }}</p>
              <div class="metrics">
                <div v-for="(metric, k) in activityType.metrics" :key="k" class="metric">
                  <span>{{ metric.value }} {{ formatUnit(metric.selectedUnitName) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <p v-else>No sessions for this date.</p>
  </div>
</template>

<script setup>
import { defineProps, computed } from 'vue';

const props = defineProps({
  sessions: {
    type: Array,
    required: true
  },
  selectedDate: {
    type: Date,
    required: false
  }
});

// Compute formatted date
const formattedDate = computed(() => {
  if (props.selectedDate) {
    const date = new Date(props.selectedDate);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${day}.${month}.${year}`;
  }
  return '';
});

// Format units to be pluralized
const formatUnit = (unit) => {
  if (!unit) return '';
  return unit.endsWith('s') ? unit : `${unit}s`;
};
</script>

<style scoped>
.activity-type {
  margin-bottom: 0.5rem;
}

.activity-name {
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.metrics {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.metric {
  background: #f0f0f0;
  border-radius: 4px;
  padding: 0.25rem 0.5rem;
}

.notes {
  font-style: italic;
  color: #666;
  margin-top: 0.5rem;
}

.session-summary p {
  margin: 0.5rem 0;
}
</style>
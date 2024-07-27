<template>
  <div class="home">
    <img class="mx-auto mb-10" alt="FitnessTrack logo" src="../assets/fitnesstrack-logo-transp.png">
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App" />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import HelloWorld from '@/components/HelloWorld.vue';

@Options({
  components: {
    HelloWorld,
  },
})
export default class HomeView extends Vue { }
</script>

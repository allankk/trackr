<template>
  <div class="home" :class="{ 'home-guest': !loggedIn }">
    <!--<div :class="loggedIn ? 'home-logged' : 'home-guest'">-->
    <div
      class="top-container hero relative h-screen bg-cover bg-center text-white flex flex-col justify-center items-center max-h-screen"
      style="background-image: url('../splash-1.jpg');">
      <img class="mx-auto mb-8 h-24 z-10" alt="Trackr Logo" src="../assets/trackr-logo-transp.png" />
      <h1 class="text-3xl font-bold mb-4 text-white z-10 tracking-wide opacity-90">Activity Tracker</h1>
      <p class="text-lg mb-8 z-10 max-w-xl text-center opacity-80">
        Track all your fitness activities, study and work sessions effortlessly!
      </p>
      <Button v-if="!loggedIn" as="router-link" label="Sign up" class="w-full px-6 py-3 max-w-72 mt-4 opacity-90 z-10"
        to="/register"></Button>
      <Button v-if="!loggedIn" @click="handleDemoLogin" :loading="loading" label="Try with a demo account"
        class="w-full px-6 mt-4 max-w-72 opacity-90 z-10"></Button>
      <div class="absolute inset-0 bg-black opacity-50"></div>
    </div>

    <div class="features p-8 text-center">
      <span class="font-bold text-3xl tracking-wide">Features</span>
      <Divider />
      <div class="flex flex-col justify-center items-center">
        <div class="feature-item mb-6">
          <h2 class="text-2xl font-semibold mb-2">Dashboard</h2>
          <p class="text-gray-600 mb-4">
            Easily view and manage all your recorded sessions with our intuitive dashboard.
          </p>
          <Image src="/trackr-screen-new-session.png" alt="Image" width="500" preview class="shadow-lg p-4 border" />
        </div>
        <Divider />
        <div class="feature-item mt-4">
          <h2 class="text-2xl font-semibold mb-2">Add Sessions</h2>
          <p class="text-gray-600 mb-4">
            Track your activities in detail and keep a log of your progress.
          </p>
          <Image src="/trackr-screen-new-session.png" alt="Image" width="500" preview class="shadow-lg" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useToast } from 'primevue/usetoast';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import Button from 'primevue/button';
import Image from 'primevue/image';
import Divider from 'primevue/divider';

const loading = ref(false);

const toast = useToast();
const store = useStore();
const router = useRouter();

const loggedIn = computed(() => store.state.auth.status.loggedIn);

const handleDemoLogin = () => {
  loading.value = true;
  const demoCredentials = { email: 'demo@demo.com', password: 'password' };

  store.dispatch("auth/login", demoCredentials).then(
    () => {
      loading.value = false;
      router.push("/dashboard");
    },
    (error) => {
      toast.add({ severity: 'error', summary: 'Error', detail: error.message || 'Something went wrong', life: 3000 });
    }
  );
};

</script>


<style scoped>
.top-container {
  width: 100vw;
  left: calc(-50vw + 50%);
  bottom: 30px;
  max-height: 700px;
}

@media (min-width: 1025px) and (max-width: 1279px) {

  .home {
    margin-left: -300px;
  }

  .home>* {
    padding-left: 300px;
  }

  .home.home-guest {
    padding-left: 0;

    .features {
      padding-left: 0;
    }

    .top-container {
      width: calc(100vw + 300px);
      left: calc(-50vw + 50% - 300px);
    }
  }
}
</style>
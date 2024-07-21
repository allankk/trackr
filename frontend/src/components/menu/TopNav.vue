<template>
  <nav class="py-4 border-b sticky top-0 border-slate-400 layout-topbar-menu h-14 z-20 bg-white">
    <a href="/">
      <img alt="FitnessTrack logo" class="max-h-10 absolute top-2" src="@/assets/fitnesstrack-logo.png">
    </a>
    <button v-if="isScreenSmall || currentUser" class="p-link layout-menu-button float-right lg:hidden layout-topbar-button" @click="onMenuToggle()">
        <i class="pi pi-bars"></i>
    </button>
    <div class="hidden md:block">
        <router-link class="px-6" to="/">Home</router-link>
        <router-link class="px-6" to="/about">About</router-link>
        <router-link class="px-6" to="/hello">Hello</router-link>
        <router-link v-if="!currentUser" class="px-6" to="/login">Login</router-link>
    </div>
  </nav>
</template>

<script setup>
import { useLayout } from '@/components/menu/menuLayout';

const { onMenuToggle, currentUser, isScreenSmall } = useLayout();
</script>
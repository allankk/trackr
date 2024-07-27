<template>
  <div class="layout-wrapper" :class="containerClass">
    <TopNav />
    <div>
      <SideNav />
    </div>
    <div class="layout-main-container">
      <div class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <Component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useLayout } from '@/components/menu/menuLayout';
import { computed, watch, ref } from 'vue';
import TopNav from './components/menu/TopNav.vue';
import SideNav from './components/menu/SideNav.vue';

const { layoutState, isSidebarActive } = useLayout();
const outsideClickListener = ref(null);

const containerClass = computed(() => {
  return {
    'layout-mobile-active': layoutState.staticMenuMobileActive.value
  }
})

watch(isSidebarActive, (newVal) => {
  if (newVal) {
    bindOutsideClickListener();
  } else {
    unbindOutsideClickListener();
  }
});

const bindOutsideClickListener = () => {
  if (!outsideClickListener.value) {
    outsideClickListener.value = (event) => {
      if (isOutsideClicked(event)) {
        layoutState.staticMenuMobileActive.value = false;
      }
    };
    document.addEventListener('click', outsideClickListener.value);
  }
};
const unbindOutsideClickListener = () => {
  if (outsideClickListener.value) {
    document.removeEventListener('click', outsideClickListener);
    outsideClickListener.value = null;
  }
};
const isOutsideClicked = (event) => {
  const sidebarEl = document.querySelector('.layout-sidebar');
  const topbarEl = document.querySelector('.layout-menu-button');

  if (!sidebarEl || !topbarEl) {
    return false;
  }
  return !(sidebarEl.isSameNode(event.target) || sidebarEl.contains(event.target) || topbarEl.isSameNode(event.target) || topbarEl.contains(event.target));
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.1s ease-out;
}
</style>

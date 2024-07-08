<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in filteredModel" :key="item">
            <SideMenuItem :item="item" :index="i"></SideMenuItem>
        </template>
    </ul>
</template>
  
<script setup>
import { computed, ref, onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";
import SideMenuItem from '@/components/menu/SideMenuItem.vue';

const store = useStore();
const isScreenSmall = ref(window.innerWidth < 1024);

const currentUser = computed(() => {
    return store.state.auth.user;
});

const userItems = computed(() => {
    if (currentUser.value) {
        return [
            { label: 'Profile', icon: 'pi pi-fw pi-user', to: '/profile'}
        ]
    } else {
        return [
            { label: 'Login', icon: 'pi pi-fw pi-tablet', to: '/login' },
        ]
    }
});

const model = computed(() => ([
    {
        label: 'Home',
        items: [
            { label: 'Home', icon: 'pi pi-fw pi-home', to: '/' },
            { label: 'About', icon: 'pi pi-fw pi-home', to: '/about' },
            { label: 'Hello', icon: 'pi pi-fw pi-home', to: '/hello' },
        ]
    },
    {
        label: 'Profile',
        items: userItems.value
    }
]));

const filteredModel = computed(() => {
  if (!isScreenSmall.value) {
    return model.value.filter(item => item.label !== 'Home');
  }
  return model.value;
});

const updateScreenSize = () => {
    isScreenSmall.value = window.innerWidth < 768;
};

onMounted(() => {
    window.addEventListener("resize", updateScreenSize);
});

onUnmounted(() => {
    window.removeEventListener("resize", updateScreenSize);
});
</script>
  
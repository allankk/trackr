<template>
    <div class="layout-sidebar" v-if="isScreenSmall || currentUser">
        <ul class="layout-menu">
            <template v-for="(item, i) in filteredModel" :key="item">
                <SideMenuItem :item="item" :index="i"></SideMenuItem>
            </template>
        </ul>
    </div>
</template>
  
<script setup>
import { computed } from "vue";
import { useLayout } from '@/components/menu/menuLayout';
import SideMenuItem from '@/components/menu/SideMenuItem.vue';

const { currentUser, isScreenSmall } = useLayout();

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
</script>
  
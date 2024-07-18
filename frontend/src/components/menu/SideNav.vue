<template>
    <div class="layout-sidebar" v-if="isScreenSmall || currentUser">
        <ul class="layout-menu">
            <template v-for="(item, i) in filteredModel" :key="item.label">
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
            { label: 'Profile', icon: 'pi pi-fw pi-user', to: '/profile' },
            { label: 'User Board', icon: 'pi pi-fw pi-user', to: '/userboard' },
            { label: 'Log out', icon: 'pi pi-fw pi-sign-out', action: 'logout' }
        ];
    } else {
        return [
            { label: 'Login', icon: 'pi pi-fw pi-sign-in', to: '/login' },
        ];
    }
});

const commonItems = [
    {
        label: 'Home',
        items: [
            { label: 'Home', icon: 'pi pi-fw pi-home', to: '/' },
            { label: 'About', icon: 'pi pi-fw pi-question', to: '/about' },
            { label: 'Hello', icon: 'pi pi-fw pi-bookmark', to: '/hello' },
        ]
    }
];

const loggedInItems = computed(() => {
    if (currentUser.value) {
        return [
            {
                label: 'Activities',
                items: [
                    { label: 'Groups', icon: 'pi pi-fw pi-users', to: '/activity/groups' },
                    { label: 'Exercises', icon: 'pi pi-fw pi-plus-circle', to: '/activity/types' },
                ]
            }
        ];
    }
    return [];
});

// Combine the models
const model = computed(() => {
    const userModel = {
        label: 'Profile',
        items: userItems.value
    };

    return [...commonItems, ...loggedInItems.value, userModel];
});

// Filter model based on screen size
const filteredModel = computed(() => {
    if (!isScreenSmall.value) {
        return model.value.filter(item => item.label !== 'Home');
    }
    return model.value;
});
</script>

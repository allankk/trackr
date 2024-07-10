<template>
    <li :class="{ 'layout-root-menuitem': root, 'active-menuitem': isActiveMenu }">
        <div v-if="root && item.visible !== false" class="layout-menuitem-root-text">{{ item.label }}</div>
        <a v-if="(!item.to || item.items) && item.visible !== false" :href="item.url" @click="itemClick($event, item, index)" :class="item.class" :target="item.target" tabindex="0">
            <i :class="item.icon" class="layout-menuitem-icon"></i>
            <span class="layout-menuitem-text">{{ item.label }}</span>
            <i class="pi pi-fw pi-angle-down layout-submenu-toggler" v-if="item.items"></i>
        </a>
        <router-link v-if="item.to && !item.items && item.visible !== false" :class="[item.class, { 'active-route': checkActiveRoute(item)}]" @click="itemClick($event, item, index)" tabindex="0" :to="item.to">
            <i :class="item.icon" class="layout-menuitem-icon"></i>
            <span class="layout-menuitem-text">{{ item.label }}</span>
            <i class="pi pi-fw pi-angle-down layout-submenu-toggler" v-if="item.items"></i>
        </router-link>
        <Transition v-if="item.items && item.visible !== false" name="layout-submenu">
            <ul v-show="root ? true : isActiveMenu" class="layout-submenu">
                <SideMenuItem v-for="(child, i) in item.items" :key="child" :index="i" :item="child" :parentItemKey="itemKey" :root="false"></SideMenuItem>
            </ul>
        </Transition>
    </li>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute, useRouter  } from 'vue-router';
import { useStore } from 'vuex';
import { useLayout } from '@/components/menu/menuLayout';

defineProps({
    item: {
        type: Object,
        default: () => ({})
    },
    index: {
        type: Number,
        default: 0
    },
    root: {
        type: Boolean,
        default: true
    },
    parentItemKey: {
        type: String,
        default: null
    }
});

const { layoutState, onMenuToggle } = useLayout();
const route = useRoute();
const isActiveMenu = ref(false);
const itemKey = ref(null);
const store = useStore();
const router = useRouter();

const itemClick = (event, item) => {
    if ((item.to || item.url) && (layoutState.staticMenuMobileActive.value)) {
        onMenuToggle();
    }

    if (item.action == 'logout') {
        store.dispatch('auth/logout');
        router.push('/login');
    }
};

const checkActiveRoute = (item) => {
    return route.path === item.to;
};


</script>

<style scoped>

</style>
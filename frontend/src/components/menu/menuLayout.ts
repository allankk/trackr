import { reactive, toRefs, computed } from 'vue';

const layoutState = reactive({
    staticMenuMobileActive: false,
});

export function useLayout() {
    const onMenuToggle = () => {
        console.log('onmenutoggle');
        layoutState.staticMenuMobileActive = !layoutState.staticMenuMobileActive;
    };

    const isSidebarActive = computed(() => layoutState.staticMenuMobileActive);

    return { layoutState: toRefs(layoutState), onMenuToggle, isSidebarActive };
}

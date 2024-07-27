import { reactive, toRefs, computed, ref, onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";

const layoutState = reactive({
  staticMenuMobileActive: false,
});

export function useLayout() {
  const store = useStore();

  const onMenuToggle = () => {
    layoutState.staticMenuMobileActive = !layoutState.staticMenuMobileActive;
  };

  const isSidebarActive = computed(() => layoutState.staticMenuMobileActive);

  const isScreenSmall = ref(window.innerWidth < 768);

  const currentUser = computed(() => {
    return store.state.auth.user;
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

  return {
    layoutState: toRefs(layoutState),
    onMenuToggle,
    isSidebarActive,
    currentUser,
    isScreenSmall,
  };
}

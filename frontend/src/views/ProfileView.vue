<template>
  <div class="profile flex justify-center mt-20">
    <UserProfile />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import UserProfile from '@/components/UserProfile.vue';
import SideMenu from '@/components/menu/SideNav.vue';

@Options({
  components: {
    SideMenu,
    UserProfile,
  },
})
export default class ProfileView extends Vue { }
</script>

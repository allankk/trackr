<template>
  <div class="flex flex-col items-center justify-center">
    <span class="text-red-500 italic mb-6">Changing user details is not available yet</span>
    <div class="bg-white rounded-lg shadow-lg p-6 w-96">
      <div class="flex flex-col items-center">
        <Avatar :label="avatarLabel" class="mr-2" size="xlarge" />
        <h2 class="text-xl font-semibold mb-2">User Profile</h2>
        <div class="px-10 w-full">
          <p class="text-gray-600 mb-4 w-full flex justify-between">
            <strong>Email</strong> {{ content.email }}
          </p>
          <p class="text-gray-600 w-full flex justify-between"><strong>Role</strong> {{ content.role }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import UserService from "@/services/UserService";
import Avatar from "primevue/avatar";
import { useToast } from "primevue/usetoast";

const toast = useToast();
const content = ref({});

const avatarLabel = computed(() => {
  return content.value.email ? content.value.email.charAt(0).toUpperCase() : "";
});

onMounted(() => {
  UserService.getUserBoard().then(
    (response) => {
      content.value = response.data;
    },
    (error) => {
      content.value = {
        email: "",
        role: "",
      };
      toast.add({
        severity: "error",
        summary: "Error",
        detail: error.message || "Failed to retrieve user info",
        life: 3000,
      });
    }
  );
});
</script>

<style scoped></style>

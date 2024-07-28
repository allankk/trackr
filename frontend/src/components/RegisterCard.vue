<template>
  <div class="flex justify-center align-items-center min-h-screen">
    <div
      class="card shadow-2 surface-card border-round w-full sm:w-30rem py-8 px-4 md:px-14 border max-w-lg h-full shadow-sm rounded-2xl">
      <h1 class="text-center mb-4 font-bold text-xl">Register</h1>
      <Form @submit="handleRegister" :validation-schema="schema" class="p-fluid" validateOnChange="false"
        validateOnBlur="false" validateOnInput="false" validateOnModelUpdate="false">
        <InputGroup class="mt-8 relative">
          <InputGroupAddon>
            <i class="pi pi-user"></i>
          </InputGroupAddon>
          <FloatLabel>
            <Field name="email" v-slot="{ field }" :validateOnChange="false" :validateOnBlur="true"
              :validateOnInput="false" :validateOnModelUpdate="false">
              <InputText v-model="email" v-bind="field" id="email" class="input"></InputText>
            </Field>
            <label for="email">email</label>
            <div class="absolute right-0 top-10">
              <ErrorMessage name="email" class="text-xs text-red-500 pr-4 font-bold"></ErrorMessage>
            </div>
          </FloatLabel>
        </InputGroup>
        <InputGroup class="mt-8 relative">
          <InputGroupAddon>
            <i class="pi pi-lock"></i>
          </InputGroupAddon>
          <FloatLabel>
            <Field name="password" v-slot="{ field }" :validateOnChange="false" :validateOnBlur="true"
              :validateOnInput="false" :validateOnModelUpdate="false">
              <Password v-model="password" v-bind="field" id="password" class="input no-left-border-radius"
                :feedback="false" toggleMask></Password>
            </Field>
            <label for="password">password</label>
          </FloatLabel>
          <div class="absolute right-0 top-10">
            <ErrorMessage name="password" class="text-xs text-red-500 pr-4 font-bold"></ErrorMessage>
          </div>
        </InputGroup>
        <InputGroup class="mt-8 relative">
          <InputGroupAddon>
            <i class="pi pi-lock"></i>
          </InputGroupAddon>
          <FloatLabel>
            <Field name="confirmPassword" v-slot="{ field }" :validateOnChange="false" :validateOnBlur="true">
              <Password v-model="confirmPassword" v-bind="field" id="confirmPassword"
                class="input no-left-border-radius" :feedback="false" toggleMask></Password>
            </Field>
            <label for="confirmPassword">confirm password</label>
          </FloatLabel>
          <div class="absolute right-0 top-10">
            <ErrorMessage name="confirmPassword" class="text-xs text-red-500 pr-4 font-bold"></ErrorMessage>
          </div>
        </InputGroup>

        <div class="alert alert-danger mt-3 text-sm text-red-600 min-h-5 font-bold" role="alert">
          {{ message }}
        </div>
        <Button type="submit" label="Sign up" class="w-full mt-4" :loading="loading"></Button>
      </Form>
      <div class="mt-6 text-sm">
        Already have an account?
        <Button as="router-link" link label="Sign in" to="/login"
          class="text-sm hover:transition-all text-green-500 font-bold hover:text-green-700" unstyled></Button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import InputText from "primevue/inputtext";
import Password from "primevue/password";
import FloatLabel from "primevue/floatlabel";
import Button from "primevue/button";
import InputGroup from "primevue/inputgroup";
import InputGroupAddon from "primevue/inputgroupaddon";
import { useToast } from 'primevue/usetoast';

const toast = useToast();

const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const loading = ref(false);
const message = ref("");

const schema = yup.object().shape({
  email: yup.string().required().email(),
  password: yup.string().required().min(8),
  confirmPassword: yup
    .string()
    .required("Confirming password is required")
    .oneOf([yup.ref("password")], "Passwords do not match"),
});

const store = useStore();
const router = useRouter();

const loggedIn = computed(() => store.state.auth.status.loggedIn);

onMounted(() => {
  if (loggedIn.value) {
    router.push("/profile");
  }
});

const handleRegister = (user) => {
  loading.value = true;

  store.dispatch("auth/register", user).then(
    (response) => {
      loading.value = false;
      message.value = response.message;

      router.push("/profile");
    },
    (error) => {
      loading.value = false;
      toast.add({ severity: 'error', summary: 'Error', detail: error.message || 'Failed to Register', life: 3000 });
    }
  );
};
</script>

<style>
.p-inputgroup .no-left-border-radius input.p-password-input {
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
}
</style>

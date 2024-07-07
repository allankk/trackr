<template>
  <div class="flex justify-center align-items-center min-h-screen">
    <div class="card shadow-2 surface-card border-round w-full sm:w-30rem py-8 sm:px-4 md:px-14 border max-w-lg h-full shadow-sm rounded-2xl">
      <h1 class="text-center mb-4 font-bold text-xl">Login</h1>
      <Form @submit="handleLogin" :validation-schema="schema" class="p-fluid">
        <InputGroup class="mt-8 relative">
          <InputGroupAddon>
               <i class="pi pi-user"></i>
          </InputGroupAddon>
          <FloatLabel>
            <Field name="email" v-slot="{ field }">
              <InputText v-model="email" v-bind="field" id="email" class="input"></InputText>
            </Field>
            <label for="email">email</label>
            <div class="absolute right-0 top-10">
              <ErrorMessage name="email" class="text-xs text-red-500"></ErrorMessage>
            </div>
          </FloatLabel>
        </InputGroup>
        <InputGroup class="mt-8 relative">
          <InputGroupAddon>
               <i class="pi pi-lock"></i>
          </InputGroupAddon>
          <FloatLabel>
            <Field name="password" v-slot="{ field }">
              <Password v-model="password" v-bind="field" id="password" class="input no-left-border-radius" :feedback="false" toggleMask></Password>
            </Field>
            <label for="password">password</label>
          </FloatLabel>
          <div class="absolute right-0 top-10">
            <ErrorMessage name="password" class="text-sm text-red-500"></ErrorMessage>
          </div>
        </InputGroup>

        <div class="alert alert-danger mt-3 text-sm text-red-600 min-h-5" role="alert">
          {{ message }}
        </div>
        <Button type="submit" label="Login" class="w-full mt-4" :loading="loading"></Button>
      </Form>
    </div>
  </div>
</template>

<script>
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import FloatLabel from 'primevue/floatlabel';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';

export default {
  name: "LoginCard",
  components: {
    Form,
    ErrorMessage,
    Field,
    InputText,
    FloatLabel,
    Button,
    InputGroup,
    InputGroupAddon,
    Password
  },
  data() {
    const schema = yup.object().shape({
      email: yup.string().required().email(),
      password: yup.string().required(),
    });

    return {
      email: "",
      password: "",
      loading: false,
      message: "",
      schema,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  created() {
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    handleLogin(user) {
      console.log(user);
      this.loading = true;

      this.$store.dispatch("auth/login", user).then(
        () => {
          this.$router.push("/profile");
        },
        (error) => {
          console.log(error);
          this.loading = false;
          this.message =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
        }
      );
    },
  },
};
</script>

<style>
.no-left-border-radius .p-password-input {
  border-top-left-radius: 0 !important;
  border-bottom-left-radius: 0 !important;
}
</style>


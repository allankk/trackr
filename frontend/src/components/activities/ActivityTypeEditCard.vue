<template>
  <div class="exercise-form p-fluid">
    <div class="mx-auto max-w-2xl">
      <Form @submit="updateData" :initial-values="initialValues" :validation-schema="schema" class="p-fluid"
        validateOnChange="false" validateOnBlur="false" validateOnInput="false" validateOnModelUpdate="false">
        <Field name="name" v-slot="{ field }" :validateOnChange=false :validateOnBlur=true :validateOnInput=false
          :validateOnModelUpdate=false>
          <div class="field my-4 relative">
            <label for="name1">Name</label>
            <InputText v-model="name" v-bind="field" id="name" type="text" />
            <div class="absolute right-0 top-16">
              <ErrorMessage name="name" class="text-xs text-red-500 pr-4"></ErrorMessage>
            </div>
          </div>
        </Field>
        <Field name="description" v-slot="{ field }" :validateOnChange=false :validateOnBlur=true :validateOnInput=false
          :validateOnModelUpdate=false>
          <div class="field my-4">
            <label for="description">Description</label>
            <InputText v-model="description" v-bind="field" id="description" type="text" />
          </div>
        </Field>
        <Field name="metrics" v-slot="{ field }" :validateOnChange=false :validateOnBlur=true :validateOnInput=false
          :validateOnModelUpdate=false>
          <div class="field my-4 relative">
            <label for="age1">Metrics</label>
            <MultiSelect v-bind="field" v-model="metrics" display="chip" :options="dropdownItems" optionLabel="name"
              placeholder="Select Metrics" :maxSelectedLabels="6" class="w-full" />
            <div class="absolute right-0 top-16">
              <span class="text-xs text-red-500 pr-4">{{ metricsError }}</span>
            </div>
          </div>
        </Field>
        <div class="mt-10 mb-4 flex justify-end gap-2">
          <Button type="button" label="Cancel" severity="secondary" @click="$emit('closeModal')"></Button>
          <Button type="submit" label="Save"></Button>
        </div>
      </Form>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, toRaw } from 'vue';
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import MultiSelect from 'primevue/multiselect';
import ActivityService from '@/services/ActivityService';

const props = defineProps(['activity', 'visible']);
const emit = defineEmits(['closeModal'])
const dropdownItems = ref([]);
const metrics = ref([]);
const metricsError = ref('');
const name = ref(props.activity.name);
const description = ref(props.activity.description);

metrics.value = props.activity.metrics.map(item => {
  return {
    name: item.name,
    code: item.id
  }
});

const initialValues = {
  name: props.activity.name,
  description: props.activity.description,
  metrics: metrics.value
}

const schema = yup.object().shape({
  name: yup.string().required(),
});

const updateData = () => {
  const rawMetrics = toRaw(metrics.value).map(item => toRaw(item));

  if (rawMetrics.length === 0) {
    console.log('error updating data');
    metricsError.value = 'At least 1 metric is required';
    return;
  } else {
    metricsError.value = '';
  }

  const activityTypeData = {
    name: name.value,
    description: description.value,
    metrics: rawMetrics.map(item => item.code)
  }

  ActivityService.updateActivityType(props.activity.id, activityTypeData).then(
    () => {
      emit('closeModal');
    },
    (error) => {
      console.log(error);
    }
  )
}

onMounted(() => {
  ActivityService.getAllMetrics().then(
    (response) => {
      response.data.forEach(item => {
        dropdownItems.value.push({
          name: item.name,
          code: item.id
        })
      })
    },
    (error) => {
      console.log(error);
    }
  )
});


</script>

<style scoped></style>

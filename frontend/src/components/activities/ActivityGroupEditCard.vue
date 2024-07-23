<template>
  <div class="exercise-form p-fluid">
    <div class="mx-auto max-w-2xl">
      <Form @submit="updateData" :initial-values="initialValues" :validation-schema="schema" class="p-fluid" validateOnChange="false" validateOnBlur="false" validateOnInput="false" validateOnModelUpdate="false">
        <Field name="name" v-slot="{ field }" :validateOnChange=false :validateOnBlur=true :validateOnInput=false :validateOnModelUpdate=false>
          <div class="field my-4 relative">
              <label for="name1">Name</label>
              <InputText v-model="name" v-bind="field" id="name" type="text" />
              <div class="absolute right-0 top-16">
                <ErrorMessage name="name" class="text-xs text-red-500 pr-4"></ErrorMessage>
              </div>
          </div>
        </Field>
        <Field name="description" v-slot="{ field }" :validateOnChange=false :validateOnBlur=true :validateOnInput=false :validateOnModelUpdate=false>
          <div class="field my-4">
              <label for="description">Description</label>
              <InputText v-model="description" v-bind="field" id="description" type="text" />
          </div>
        </Field>
        <Field name="activityTypes" v-slot="{ field }" :validateOnChange=false :validateOnBlur=true :validateOnInput=false :validateOnModelUpdate=false>
          <div class="field my-4 relative">
              <label for="activityTypes">Activity Types</label>
              <MultiSelect v-bind="field" v-model="activityTypes" display="chip" :options="dropdownItems" optionLabel="name" placeholder="Select Activities"
              :maxSelectedLabels="6" class="w-full" />
              <div class="absolute right-0 top-16">
                <span class="text-xs text-red-500 pr-4">{{ activityTypesError }}</span>
              </div>
          </div>
        </Field>
        <div class="mt-10 mb-4 flex justify-end gap-2">
          <Button type="button" label="Cancel" severity="secondary" @click="$emit('closeModal')"></Button>
          <Button type="submit" label="Save" class=""></Button>
        </div>
      </Form>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import MultiSelect from 'primevue/multiselect';
import ActivityGroupService from '@/services/ActivityGroupService';
import ActivityService from '@/services/ActivityService';

const props = defineProps(['activityGroup', 'visible']);
const emit = defineEmits(['closeModal'])
const dropdownItems = ref([]);
const activityTypes = ref([]);
const activityTypesError = ref('');
const name = ref(props.activityGroup.name);
const description = ref(props.activityGroup.description);

activityTypes.value = props.activityGroup.activityTypes.map(item => {
  return {
    name: item.name,
    code: item.id
  }
});

const initialValues = {
  name: props.activityGroup.name,
  description: props.activityGroup.description,
  activityTypes: activityTypes.value
}

const schema = yup.object().shape({
  name: yup.string().required(),
});

const updateData = () => {
  if (activityTypes.value.length === 0) {
    console.log('error');
    activityTypesError.value = 'At least 1 activity is required'; 
    return; 
  } else {
    activityTypesError.value = '';
  }

  const activityGroupData = {
    name: name.value,
    description: description.value,
    activityTypes: activityTypes.value.map(item => item.code)
  }

  ActivityGroupService.updateActivityGroup(props.activityGroup.id, activityGroupData).then(
    (response) => {
      emit('closeModal');
    },
    (error) => {
      console.log('error' + error.toString());
    }
  )
}

onMounted(() => {
      ActivityService.getAllActivities().then(
      (response) => {
        response.data.forEach(item => {
          dropdownItems.value.push({
            name: item.name,
            code: item.id
          })
        })
      },
      (error) => {
        console.log('error: ' + error.toString());
      }
    )
});


</script>

<style scoped>
</style>

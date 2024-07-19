<template>
    <div class="relative h-0">
        <div class="absolute left-0">
            <Button label="Plain" plain text class="mb-2 mr-2 text-gray-500 hover:text-black" @click="goBack()">
                <i class="pi pi-fw pi-chevron-left text-gray-500"></i>
                <span class="-ml-1">back</span>
            </Button>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import Button from 'primevue/button'

const router = useRouter();

const goBack = () => {
    if (router) {
        router.back()
    }
}
</script>
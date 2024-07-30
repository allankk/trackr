<template>

  <div class="about flex flex-col mx-auto max-w-5xl p-8 text-center">
    <!-- Logo -->
    <img class="mx-auto mb-8 h-24" alt="Trackr Logo" src="../assets/trackr-logo-transp.png" />

    <!-- Description -->
    <p class="text-lg mb-8 text-gray-600">
      Built with Vue and Spring Boot
    </p>

    <div class="flex flex-col justify-center items-center text-lg">
      <a href="mailto:allankossas@gmail.com" class="text-gray-600 mb-6 hover:text-red-700 hover:no-underline flex items-center">
        <i class="pi pi-fw pi-envelope" style="font-size: 1.5rem"></i>
        <span class="ml-2">allankossas@gmail.com</span>
      </a>
      <div class="flex space-x-4 mb-8">
        <a href="https://www.linkedin.com/in/allankossas" class="text-gray-600 hover:text-blue-700 rounded-lg">
          <i class="mx-2 pi pi-fw pi-linkedin"></i>
        </a>
        <a href="https://github.com/allankk" class="text-gray-600 hover:text-black">
          <i class="pi pi-fw pi-github"></i>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>

</script>


<style scoped>
.about i {
  font-size: 2rem;

}

</style>

<template>
  <nav class="py-4 mb-20 border-b border-slate-400">
    <img alt="FitnessTrack logo" class="max-h-10 absolute top-2" src="./assets/fitnesstrack-logo.png">
    <router-link class="px-6" to="/">Home</router-link>
    <router-link class="px-6" to="/about">About</router-link>
    <router-link class="px-6" to="/hello">Hello</router-link>
    <router-link class="px-6 nav-link" to="/login">Login</router-link>
  </nav>
  <router-view/>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>

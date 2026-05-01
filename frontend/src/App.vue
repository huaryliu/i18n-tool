<template>
  <div id="app">
    <ExtractorView />
  </div>
</template>

<script>
import ExtractorView from './views/ExtractorView.vue'

export default {
  name: 'App',
  components: {
    ExtractorView
  }
}
</script>

<style>
#app {
  margin: 0;
  padding: 0;
  font-family: 'Arial', sans-serif;
  height: 100vh;
}

body {
  margin: 0;
  padding: 0;
}
</style>

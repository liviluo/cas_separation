<template>
  <h3>logout success</h3>
</template>

<script>
export default {
  beforeMount() {
    this.distroyCookie();
  },
  methods: {
    distroyCookie() {
      this.$store.dispatch("LogOut");
    },
  },
};
</script>

<style>
</style>

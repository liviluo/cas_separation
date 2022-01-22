<template>
  <div></div>
</template>

<script>
export default {
  beforeMount() {
    this.loginByCas();
  },
  methods: {
    loginByCas() {
      const redirect = this.$route.query.redirect;
      window.location.href = 'http://localhost:9001/auth/cas?currentPath='.concat(redirect);
    }
  }
};
</script>

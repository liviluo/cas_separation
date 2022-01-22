<template>
  <div class="hello">
    <img alt="Vue logo" src="@/assets/logo.png" />
    <SayHello :msg="msg" />
    <button @click="get">get data</button> |
    <button @click="send">send data</button> |
    <button @click="logout">lougout</button>
  </div>
</template>

<script>
import SayHello from "@/components/home/say-hello.vue";
import { getData, sendData } from "@/api/data.js";

export default {
  components: {
    SayHello,
  },

  data() {
    return {
      msg: "前后端分离项目使用CAS认证模块",
    };
  },

  methods: {
    get() {
      getData({
        id: 123,
      }).then((res) => {
        console.log(res);
      });
    },

    send() {
      sendData({
        id: 123,
        name: "test",
      }).then((res) => {
        console.log(res);
      });
    },

    logout() {
      window.location.href = "http://localhost:9001/auth/logout";
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>

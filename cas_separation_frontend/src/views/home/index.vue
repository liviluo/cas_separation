<template>
  <div class="hello">
    <img alt="Vue logo" src="@/assets/logo.png" />
    <SayHello msg="axios demo" />
    <button @click="get">get data</button> |
    <button @click="send">send data</button>
  </div>
</template>

<script>
import SayHello from "@/components/home/say-hello.vue";
import { getData, sendData } from "@/api/data.js";
export default {
  components: {
    SayHello,
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
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>

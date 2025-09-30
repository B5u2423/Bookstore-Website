<template>
  <p>{{ msg }}</p>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  data () {
    return {
      msg: ''
    }
  },
  mounted() {
    fetch("/api/greetings/hello")
          .then((response) => response.text())
          .then((data) => {
              this.msg = data;
          });
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>

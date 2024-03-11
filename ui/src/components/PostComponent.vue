<script setup lang="ts">
import type {PostDTO} from "../services/post-service/model/PostDTO";
import {onMounted, type Ref, ref} from "vue";

const props = defineProps({
  propPost: Object
});

const post : Ref<PostDTO | null> = ref(null);

onMounted(() => {
  post.value = props.propPost as PostDTO;
})
</script>

<template>
<div class="container">
  <div>
    {{ post?.content }}
  </div>
</div>
</template>

<style scoped>

</style>
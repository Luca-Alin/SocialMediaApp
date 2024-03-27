<script setup lang="ts">

import {onMounted, type Ref, ref} from "vue";
import type {CommentDTO} from "../services/comment-service/model/CommentDTO";
import {formatDate} from "../services/format-date-service/FormatDateService";

const props = defineProps({
  commentProp: Object
});
const comment : Ref<CommentDTO | null> = ref(null);


onMounted(() => {
  comment.value = props.commentProp as CommentDTO;
});
</script>

<template>
<div v-if="comment" class="d-flex pt-2">
  <div class="d-flex">
    <div class="pe-2">
      <img :src="`data:image/png;base64,${comment.user?.profileImage}`" class="rounded-circle" width="40" height="40" alt="">
    </div>
    <div class="d-flex flex-column">
      <div class="rounded-3 bg-dark-subtle p-2">
        <router-link :to="{
              name: `User`,
              params: {id: `${comment?.user?.uuid}`}}">
          {{ comment?.user?.firstName }} {{ comment?.user?.lastName }}
        </router-link>
        <div>
          {{ comment.content }}
        </div>
      </div>
      <div class="ps-2">
        {{ formatDate(comment.createdAt!) }}
      </div>
    </div>
  </div>

</div>
</template>

<style scoped>

</style>
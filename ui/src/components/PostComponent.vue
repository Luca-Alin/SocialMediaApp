<script setup lang="ts">
import type {PostDTO} from "../services/post-service/model/PostDTO";
import {onMounted, type Ref, ref} from "vue";

const props = defineProps({
  propPost: Object
});

const post: Ref<PostDTO | null> = ref(null);

onMounted(() => {
  post.value = props.propPost as PostDTO;
});

const slide: any = ref(0);
const sliding: any = ref(null);

function onSlideStart(slide: any) {
  sliding.value = true;
}

function onSlideEnd(slide: any) {
  sliding.value = false;
}
</script>

<template>
  <div class="container border border-1">

    <div id="carouselExampleSlidesOnly" class="carousel slide" data-ride="carousel">
      <div class="carousel-inner">

        <div v-for="image in post?.images">
          <div class="carousel-item active">
            <img class="d-block w-100" :src="`data:image/png;base64,${image}`" alt="First slide">
          </div>
        </div>

      </div>
    </div>

    <div>
      <router-link :to="{
      name: `User`,
      params: {
        id: `${post?.user.uuid}`
      }
    }"
      >{{ post?.user.firstName }} {{ post?.user.lastName }}
      </router-link>
    </div>
    <div>
      {{ post?.content }}
    </div>

  </div>

</template>

<style scoped>

</style>
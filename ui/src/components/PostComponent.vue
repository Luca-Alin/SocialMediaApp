<script setup lang="ts">
import type {PostDTO} from "../services/post-service/model/PostDTO";
import {onMounted, type Ref, ref} from "vue";
import PostCommentSection from "../components/PostCommentSection.vue";
import {usePostsStore} from "../stores/PostsStore";
import {storeToRefs} from "pinia";

const postsStore = usePostsStore();
const {posts} = storeToRefs(postsStore);

const props = defineProps({
  propPost: Object,
  postId: String
});


const post: Ref<PostDTO | null> = ref(null);
const bootstrapId: Ref<string | null> = ref(null);
onMounted(() => {
  post.value = posts.value.get(props.postId!) as PostDTO;
  bootstrapId.value = `bootstrapId-${post.value?.uuid}`;

  console.log(post.value);
});
</script>

<template>
  <div class="container border border-1">
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

    <!-- Post Images Carousel -->
    <div :id="bootstrapId!" class="carousel carousel-dark slide">
      <div class="carousel-indicators">
        <button type="button" :data-bs-target="`#${bootstrapId}`" data-bs-slide-to="0" class="active"
                aria-current="true"
                aria-label="Slide 1"></button>
        <button type="button" :data-bs-target="`#${bootstrapId}`" data-bs-slide-to="1" aria-label="Slide 2"></button>
        <button type="button" :data-bs-target="`#${bootstrapId}`" data-bs-slide-to="2" aria-label="Slide 3"></button>
      </div>
      <div class="carousel-inner">
        <div class="carousel-item active" data-bs-interval="10000">
          <svg class="bd-placeholder-img bd-placeholder-img-lg d-block w-100" width="800" height="400"
               xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder: First slide"
               preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title>
            <rect width="100%" height="100%" fill="#f5f5f5" data-darkreader-inline-fill=""
                  style="--darkreader-inline-fill: #1e2021;"></rect>
            <text x="50%" y="50%" fill="#aaa" dy=".3em" data-darkreader-inline-fill=""
                  style="--darkreader-inline-fill: #b2aca2;">First slide
            </text>
          </svg>
          <div class="carousel-caption d-none d-md-block">
            <h5>First slide label</h5>
            <p>Some representative placeholder content for the first slide.</p>
          </div>
        </div>
        <div class="carousel-item" data-bs-interval="2000">
          <svg class="bd-placeholder-img bd-placeholder-img-lg d-block w-100" width="800" height="400"
               xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder: Second slide"
               preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title>
            <rect width="100%" height="100%" fill="#eee" data-darkreader-inline-fill=""
                  style="--darkreader-inline-fill: #dddad6;"></rect>
            <text x="50%" y="50%" fill="#bbb" dy=".3em" data-darkreader-inline-fill=""
                  style="--darkreader-inline-fill: #bdb7af;">Second slide
            </text>
          </svg>
          <div class="carousel-caption d-none d-md-block">
            <h5>Second slide label</h5>
            <p>Some representative placeholder content for the second slide.</p>
          </div>
        </div>
        <div class="carousel-item">
          <svg class="bd-placeholder-img bd-placeholder-img-lg d-block w-100" width="800" height="400"
               xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder: Third slide"
               preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title>
            <rect width="100%" height="100%" fill="#e5e5e5" data-darkreader-inline-fill=""
                  style="--darkreader-inline-fill: #d8d4cf;"></rect>
            <text x="50%" y="50%" fill="#999" dy=".3em" data-darkreader-inline-fill=""
                  style="--darkreader-inline-fill: #a8a095;">Third slide
            </text>
          </svg>
          <div class="carousel-caption d-none d-md-block">
            <h5>Third slide label</h5>
            <p>Some representative placeholder content for the third slide.</p>
          </div>
        </div>
      </div>
      <button class="carousel-control-prev" type="button" :data-bs-target="`#${bootstrapId}`" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button class="carousel-control-next" type="button" :data-bs-target="`#${bootstrapId}`" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>

    <!-- Other Post Data -->
    <div>
      {{ post?.content }}
    </div>

    <PostCommentSection :post-id="props.postId!"/>
  </div>

</template>

<style scoped>

</style>
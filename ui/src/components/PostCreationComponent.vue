<script setup lang="ts">
import {onMounted, type Ref, ref} from "vue";
import type {PostDTO} from "../services/post-service/model/PostDTO";
import postService from "../services/post-service/PostService";
import {usePostsStore} from "../stores/PostsStore";
import {storeToRefs} from "pinia";

const postsStore = usePostsStore();

const images: Ref<any[]> = ref([]);
const postContent = ref("");

function deleteImage(image: any) {

  images.value = images.value.filter(img => img != image);
}

function createPost() {
  const post: PostDTO = {
    images: images.value.map((img: string) => img.split("data:image/png;base64,")[1]),
    content: postContent.value,
    comments: null,
    createdAt: null,
    uuid: null,
    user: null
  };
  postService.createPost(post)
      .then(res => {
        postsStore.addPost(res.data);
      })
      .catch(err => {
        console.log(err);
      })
      .finally(() => {
      });
}

onMounted(() => {
  function handleFileSelect(event: any) {
    const file = event.target.files[0];

    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        images.value.push(e.target?.result);
      };
      reader.readAsDataURL(file);
    }
  }

// Add event listener to the file input
  document!
      .getElementById("imageUploadInput")!
      .addEventListener("change", handleFileSelect);


  const dropArea = document.getElementById("dropArea")!;

  // Prevent default drag behaviors
  ["dragenter", "dragover", "dragleave", "drop"].forEach(eventName => {
    dropArea.addEventListener(eventName, preventDefaults, false);
    document.body.addEventListener(eventName, preventDefaults, false);
  });

  // Highlight drop area when a file is dragged over it
  ["dragenter", "dragover"].forEach(eventName => {
    dropArea.addEventListener(eventName, highlight, false);
  });

  ["dragleave", "drop"].forEach(eventName => {
    dropArea.addEventListener(eventName, unhighlight, false);
  });

  // Handle dropped files
  dropArea.addEventListener("drop", handleDrop, false);

  // Functions to prevent default behaviors
  function preventDefaults(e: any) {
    e.preventDefault();
    e.stopPropagation();
  }

  function highlight() {
    dropArea.classList.add("highlight");
  }

  function unhighlight() {
    dropArea.classList.remove("highlight");
  }

  function handleDrop(e: any) {
    const dt = e.dataTransfer;
    const files = dt.files;

    handleFiles(files);
  }

  function handleFiles(files: any) {
    for (const file of files) {
      const reader = new FileReader();

      reader.onload = function (e) {
        images.value.push(e.target!.result);
      };

      reader.readAsDataURL(file);
    }
  }
});
</script>

<template>
  <div class="p-1">
    <div class="d-flex flex-column">
      Content:
      <div class="d-flex">
        <div>
          <textarea rows="5" cols="25" v-model="postContent"></textarea>
        </div>

        <div class="d-flex flex-column">
          <div v-for="content in postContent.split('\n')">
            {{ content }}
          </div>
        </div>

      </div>
    </div>

    <div class="d-flex justify-content-center flex-column align-content-center border border-dark border-1"
         id="dropArea">
      <div>Drag images here</div>

      <br/>
      <input type="file" id="imageUploadInput">
      <br/>

      <div class="d-flex">
        <div v-for="image in images">
          <img :src="image" width="50" height="50" alt="" class="p-1">
          <button @click="deleteImage(image)" class="btn btn-danger">X</button>
        </div>
      </div>
    </div>

    <div class="d-flex justify-content-center">
      <button class="btn btn-primary" @click="createPost()">Post!</button>
    </div>
  </div>
</template>

<style scoped>

</style>
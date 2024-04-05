<script setup lang="ts">

import {onBeforeUnmount, onMounted, type Ref, ref} from "vue";
import type {Group} from "@/services/group-service/model/Group";
import {WhoCanCreatePosts} from "@/services/group-service/model/enums/WhoCanCreatePosts";
import {WhoCanSeePosts} from "@/services/group-service/model/enums/WhoCanSeePosts";
import {HowToJoin} from "@/services/group-service/model/enums/HowToJoin";
import groupService from "@/services/group-service/GroupService";
import router from "@/router";

const group: Ref<Group> = ref({
  id: null!,
  groupName: "",
  groupDescription: "",
  groupImage: null!,
  whoCanCreatePosts: null!,
  whoCanSeePosts: null!,
  howToJoin: null!,
  groupUsers: null!,
  groupPosts: null!,
  bannedUsers: null!
});


// TODO -- remove this interval, and replace it with an optimized solution that resizes the group description text area automatically
// let intervalId = null;
onMounted(_ => {
  function handleFileSelect(event: any) {
    const file = event.target.files[0];

    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        group.value.groupImage = (e.target.result as string).split("data:image/png;base64,")[1];
      };
      reader.readAsDataURL(file);
    }
  }

  document!
      .getElementById("imageUploadInput")!
      .addEventListener("change", handleFileSelect);

  // intervalId = setInterval(() => {
  //   const textarea = document.getElementById("group-description");
  //   textarea.style.height = "1px"; // Reset height to minimum
  //   textarea.style.height = (textarea.scrollHeight) + "px"; // Set height based on content
  // }, 1000 / 60);
});
onBeforeUnmount(_ => {
  // if (intervalId)
  // clearInterval(intervalId);
});

function createGroup() {
  groupService.createGroup(group.value)
      .then(_ => {
        router.push("/home");
      }).catch(err => {
    console.log(err);
  });
}
</script>

<template>
  <div class="d-flex flex-column">
    <!-- Upload group image form -->
    <div class="pt-2">
      <div class="d-flex justify-content-center">
        <input type="file"
               id="imageUploadInput"
               class="d-none"
               multiple accept="image/*"
               :disabled="isLoading">
        <label for="imageUploadInput" class="btn btn-link">Select Group Image</label>
      </div>
    </div>

    <!-- Display group image -->
    <div class="pt-2 d-flex justify-content-center align-items-center">
      <button v-if="group.groupImage" @click="group.groupImage = null!" class="btn p-0 m-2 border-0 position-relative">

        <img :src="`data:image/png;base64,${group.groupImage}`" alt=""
             width="200" height="200"
             class="rounded-circle">
        <svg xmlns="http://www.w3.org/2000/svg"
             width="40" height="40" fill="white"
             class="bi bi-trash-fill position-absolute"
             viewBox="0 0 16 16">
          <path
              d="M2.5 1a1 1 0 0 0-1 1v1a1 1 0 0 0 1 1H3v9a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V4h.5a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H10a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1zm3 4a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 .5-.5M8 5a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 8 5m3 .5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 1 0"/>
        </svg>
      </button>

      <div v-else>
        No group image selected
      </div>
    </div>


    <!-- group name -->
    <div class="pt-2 form-floating">
      <input id="group-name" type="text" class="form-control" v-model="group.groupName" placeholder="Group Name">
      <label for="group-name" class="form-label">Group Name</label>
    </div>


    <!-- group  description text area-->
    <div class="pt-2 form-floating">
    <textarea id="group-description" class="form-control"
              v-model="group.groupDescription"
              placeholder="Group Description">
    </textarea>
      <label for="group-description" class="form-label">
        Group Description
      </label>
    </div>


    <!-- group who can create posts -->
    <div class="pt-2 d-flex flex-column">
      Who can create posts:
      {{ group.whoCanCreatePosts ? group.whoCanCreatePosts.split("_").map(str => str.toLowerCase()).join(" ") : "undefined" }}
      <div class="flex-fill d-flex justify-content-center">
        <button @click="group.whoCanCreatePosts = WhoCanCreatePosts.ADMINS_ONLY" class="w-100  btn btn-outline-primary">
          Admins only
        </button>
        <button @click="group.whoCanCreatePosts = WhoCanCreatePosts.GROUP_MEMBERS"
                class="w-100  btn btn-outline-primary">All group members
        </button>
      </div>
    </div>

    <!-- group who can see posts -->
    <div class="pt-2 d-flex flex-column">
      Who can see posts:
      {{ group.whoCanSeePosts ? group.whoCanSeePosts.split("_").map(str => str.toLowerCase()).join(" ") : "undefined" }}
      <div class="flex-fill d-flex justify-content-center">
        <button @click="group.whoCanSeePosts = WhoCanSeePosts.ANYONE" class="w-100  btn btn-outline-primary">Anyone
        </button>
        <button @click="group.whoCanSeePosts = WhoCanSeePosts.GROUP_MEMBERS" class="w-100  btn btn-outline-primary">Only
          group members
        </button>
      </div>
    </div>

    <!-- group how to join -->
    <div class="pt-2 d-flex flex-column">
      How to join: {{
        group.howToJoin ? group.howToJoin.split("_").map(str => str.toLowerCase()).join(" ") : "undefined"
      }}
      <div class="flex-fill d-flex justify-content-center">
        <button @click="group.howToJoin = HowToJoin.FREE_FOR_ANYONE" class="w-100  btn btn-outline-primary">Free for
          anyone
        </button>
        <button @click="group.howToJoin = HowToJoin.ADMIN_PERMISSION" class="w-100 btn btn-outline-primary">Admin
          permission only
        </button>
      </div>
    </div>

    <!-- create group button -->
    <div class="w-100 pt-2">
      <button class="w-100 btn btn-primary" @click="createGroup">Create Group</button>
    </div>
  </div>

</template>

<style scoped>
#group-description {
  border: none;
  resize: none;
  outline: none;
  font-family: inherit; /* Optional: Use the same font as the rest of the document */
  font-size: inherit; /* Optional: Use the same font size as the rest of the document */
  line-height: inherit; /* Optional: Use the same line height as the rest of the document */
}

.bi-trash-fill {
  display: none; /* Hide the SVG initially */
}

.btn:hover .bi-trash-fill {
  display: inline-block; /* Show the SVG when button is hovered */
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
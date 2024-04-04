<script setup lang="ts">
import {onMounted, type Ref, ref, watch} from "vue";
import type {PostDTO} from "../services/post-service/model/PostDTO";
import postService from "../services/post-service/PostService";
import {usePostsStore} from "../stores/PostsStore";

const postsStore = usePostsStore();

const images: Ref<any[]> = ref([]);
const postContent = ref("");

function deleteImage(image: any) {
  images.value = images.value.filter(img => img != image);
}

const isLoading = ref(false);
function createPost() {
  isLoading.value = true;

  const post: PostDTO = {
    images: images.value.map((img: string) => img.split("data:image/png;base64,")[1]),
    content: postContent.value,
    comments: null!,
    createdAt: null!,
    uuid: null!,
    user: null!,
    postReactions: null!
  };

  postService.createPost(post)
      .then(res => {
        postsStore.addPost(res.data);

        console.log(res.data.images.length);

        images.value = [];
        postContent.value = "";
      })
      .catch(err => {
        console.error(err);
        errorsList.value.push("Could not create posts");
      })
      .finally(_ => {
        isLoading.value = false;
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
    if (isLoading.value) return;

    for (const file of files) {
      if (file.type.startsWith("image/")) {
        const reader = new FileReader();

        reader.onload = function (e) {
          images.value.push(e.target!.result);
        };

        reader.readAsDataURL(file);
      } else {
        const fileName = (file.name.length < 30) ? file.name : `${file.name.substring(0, 30)}... `;
        errorsList.value.push(`${fileName} is not an image`);
      }
    }
  }
});

const errorsList = ref([]);
watch(errorsList, _ => {
  if (errorsList.value.length != 0) {
    while (errorsList.value.length > 2)
      errorsList.value.pop();

    setTimeout(() => {
      errorsList.value.pop();
    }, 3000);
  }
}, {
  deep: true
});
</script>

<template>
  <div style="max-width: 500px">
    <img src="" alt="" width="800" height="1" class="d-block w-100">


    <div class="d-flex flex-column">
      <div class="d-flex flex-fill p-3">
        <textarea class="w-100 rounded-5 bg-transparent p-3 text-area custom-scrollbar"
                  rows="3" cols="25"
                  v-model="postContent"
                  placeholder="post about..."
                  :disabled="isLoading"
        ></textarea>
      </div>
    </div>

    <div class="d-flex justify-content-center flex-column align-content-center rounded-4"
         id="dropArea">

      <div class="d-flex flex-column justify-content-center align-content-center align-items-center pt-5 pb-5">
        <div class="d-flex justify-content-center">
          <input type="file"
                 id="imageUploadInput"
                 class="d-none"
                 multiple accept="image/*"
                 :disabled="isLoading">
          <label for="imageUploadInput" class="btn btn-link">Select Image</label>
        </div>
        <div class="d-flex justify-content-center">
          Or drag and drop (even multiple images)
        </div>
      </div>

      <div class="d-flex flex-wrap">
        <div v-for="image in images">
          <div class="d-flex flex-column">
            <button @click="deleteImage(image)"
                    class="btn p-0 m-2 border-0 position-relative"
                    :disabled="isLoading">
              <img :src="image" class="p-0 m-0" width="55" height="55" alt="">
              <svg xmlns="http://www.w3.org/2000/svg"
                   width="16" height="16" fill="white"
                   class="bi bi-trash-fill position-absolute"
                   viewBox="0 0 16 16">
                <path
                    d="M2.5 1a1 1 0 0 0-1 1v1a1 1 0 0 0 1 1H3v9a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V4h.5a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H10a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1zm3 4a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 .5-.5M8 5a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 8 5m3 .5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 1 0"/>
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="d-flex justify-content-center pt-2">
      <button class="btn btn-primary" @click="createPost()"
              :disabled="isLoading">
        Post!
      </button>
    </div>
  </div>

  <div v-for="error in errorsList" class="text-danger">
    {{ error }}
  </div>
</template>

<style scoped>


#dropArea {
  border: 1px dashed black;
}

.text-area {
  resize: none;
  border: none;
}

.text-area:focus {
  resize: none;
  outline: none;
  border: none;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 0.45em;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: lightgray;
  outline: 1px solid slategrey;
  border-radius: 20px;
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
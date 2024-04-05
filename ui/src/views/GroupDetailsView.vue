<script lang="ts" setup>
import {useRoute} from "vue-router";
import {onMounted, type Ref, ref, watch} from "vue";
import groupService from "@/services/group-service/GroupService";
import type {Group} from "@/services/group-service/model/Group";
import {type GroupJoinStatus} from "@/services/group-service/model/enums/GroupJoinStatus";

const route = useRoute();

watch(route, () => {
  mountPage();
});

onMounted(() => {
  mountPage();
});

const group: Ref<Group> = ref(null!);
const groupJoinStatus: Ref<GroupJoinStatus> = ref(null!);

const isLoading = ref(false);

function mountPage() {
  const groupId = route.params["id"];

  isLoading.value = true;
  groupService.findGroupById(groupId)
      .then(res => {
        console.log(res.data);
        group.value = res.data;
      })
      .catch(err => console.log(err))
      .finally(_ => isLoading.value = false);

  groupService.findGroupJoinStatus(groupId)
      .then(res => groupJoinStatus.value = res.data)
      .catch(err => console.log(err))
      .finally(_ => {
      });
}

function groupJoinAction() {
  if (!groupJoinStatus.value) return;

  switch (groupJoinStatus.value) {
    case GroupJoinStatus.NONE:
      console.log("joining group...");
      break;
    case GroupJoinStatus.BANNED:
      console.log("you are banned");
      break;
    case GroupJoinStatus.MEMBER:
      console.log("do you really want to leave?")
      break;
  }
}
</script>

<template>
  <div v-if="isLoading">
    Loading screen...
  </div>
  <div v-else>
    <div v-if="group">
      <img :src="`data:image/png;base64,${group.groupImage}`" alt=""
           class="rounded-circle" height="40" width="40">
      {{ group.groupName }}
      {{ group.groupDescription }}


      <button @click="groupJoinAction()">{{ groupJoinStatus }}</button>
    </div>
  </div>
</template>

<style scoped>

</style>
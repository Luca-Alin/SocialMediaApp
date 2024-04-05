<script lang="ts" setup>

import {onMounted, ref, type Ref} from "vue";
import groupService from "@/services/group-service/GroupService";
import type {Group} from "@/services/group-service/model/Group";

const groups: Ref<Group[]> = ref(null!);

onMounted(() => {
  groupService.findAllGroups()
      .then(res => {
        console.log(res.data);
        groups.value = res.data;
      })
      .catch(err => console.log(err));

});

</script>

<template>
  <div>
    <router-link class="btn btn-success" to="/create-group">
      Create Group
    </router-link>
    <div v-if="groups" class="d-flex flex-column">
      <div v-for="group in groups" class="d-flex p-2">
        <router-link :to="`/group/${group.id}`">
          <div>
            <img :src="`data:image/png;base64,${group.groupImage}`" alt=""
                 class="rounded-circle" height="40" width="40">
          </div>
          <div>
            {{ group.groupName }}
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
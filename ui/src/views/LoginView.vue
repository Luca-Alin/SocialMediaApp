<script setup lang="ts">
import {ref} from "vue";
import authenticationService from "../services/authentication-service/AuthenticationService";
import userService from "../services/user-service/UserService";
import {useUserInfoStore} from "../stores/UserInfoStore";
import router from "../router";
import {storeToRefs} from "pinia";

const userInfoStore = useUserInfoStore();
const {rememberMe} = storeToRefs(userInfoStore);

const email = ref("");
const password = ref("");

function login() {
  authenticationService.login({email: email.value, password: password.value})
      .then((res) => {
        const accessToken = res.data.accessToken;
        const refreshToken = res.data.refreshToken;
        userInfoStore.setAccessToken(accessToken);
        userInfoStore.setRefreshToken(refreshToken);

        userService.findUserDetails()
            .then((res) => {
              console.log(res);
              const user = res.data;
              userInfoStore.setUser(user);
            });

        router.push("/posts");
      })
      .catch(reason => {
        alert(reason.response.data);
      })
      .finally(() => {
      });
}

function randomLogin() {

  authenticationService.random()
      .then((res) => {
        const accessToken = res.data.accessToken;
        const refreshToken = res.data.refreshToken;
        userInfoStore.setAccessToken(accessToken);
        userInfoStore.setRefreshToken(refreshToken);


        userService.findUserDetails()
            .then((res) => {
              console.log(res);
              const user = res.data;
              userInfoStore.setUser(user);
            });

        router.push("/posts");
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
      });
}
</script>

<template>

  <form class="pt-5" @submit.prevent style="max-width: 420px">

    <!-- Email -->
    <div class="form-outline mb-4">
      <input type="email"
             id="form2Example1"
             class="form-control"
             v-model="email"/>
      <label class="form-label" for="form2Example1">Email address</label>
    </div>

    <!-- Password -->
    <div class="form-outline mb-4">
      <input type="password"
             id="form2Example2"
             class="form-control"
             v-model="password"/>
      <label class="form-label" for="form2Example2">Password</label>
    </div>

    <!-- Remember Me -->
    <div class="row mb-4">
      <div class="col d-flex justify-content-center">
        <!-- Checkbox -->
        <div class="form-check">
          <input class="form-check-input"
                 type="checkbox"
                 id="form2Example31"
                 v-model="rememberMe"
                 @click="userInfoStore.toggleRememberMe()"/>
          <label class="form-check-label" for="form2Example31"> Remember me </label>
        </div>
      </div>

      <!-- Forgot password -->
      <div class="col">
        <router-link to="hell">Forgot password?</router-link>
      </div>
    </div>

    <!-- Submit button -->
    <button type="button" class="btn btn-primary btn-block mb-4" @click="login()">Sign in</button>

    <!-- Other buttons -->
    <div class="text-center">
      <!-- Register User -->
      <p>Not a member?
        <router-link to="register">Register</router-link>
      </p>

      <!-- Random Account -->
      <p>or log in with:</p>
      <button class="btn btn-warning" @click="randomLogin()">Random Account</button>
    </div>

  </form>

</template>

<style scoped>

</style>
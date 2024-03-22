<script setup lang="ts">
import {type Ref, ref} from "vue";
import authenticationService from "../services/authentication-service/AuthenticationService";
import userService from "../services/user-service/UserService";
import {useUserInfoStore} from "../stores/UserInfoStore";
import router from "../router";
import {storeToRefs} from "pinia";
import type {AxiosError, AxiosResponse} from "axios";
import type {AuthenticationResponse} from "../services/authentication-service/model/AuthenticationResponse";
import {validateEmailAndPassword} from "../services/validation-service/LoginValidationService";


const userInfoStore = useUserInfoStore();
const {rememberMe} = storeToRefs(userInfoStore);

const email = ref("");
const password = ref("");

const isLoading = ref(false);
const loginErrorMessage : Ref<string | null> = ref(null);
function login() {
  isLoading.value = true;
  const a = validateEmailAndPassword(email.value, password.value);
  if (a != null) {
    loginError({
      response: {
        data: a
      }
    });
    loginFinally();
    return;
  }
  authenticationService.login({email: email.value, password: password.value})
      .then((res) => loginSuccess(res))
      .catch((err) => loginError(err))
      .finally(() => loginFinally());
}
function randomLogin() {
  isLoading.value = true;

  authenticationService.random()
      .then((res) => loginSuccess(res))
      .catch((err) => loginError(err))
      .finally(() => loginFinally());
}

function loginSuccess(res : AxiosResponse<AuthenticationResponse>) {
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
}
function loginError(err : AxiosError) {
  let errMessage = "Server is not working";
  if (err?.response?.data != null) {
    errMessage = err.response.data;
  }
  loginErrorMessage.value = errMessage;
  setTimeout(() => {
    loginErrorMessage.value = null;
  }, 2000);
}
function loginFinally() {
  isLoading.value = false;
}
</script>

<template>
<div class="">
  <form class="pt-5" @submit.prevent style="max-width: 420px">
    <div class="d-flex justify-content-center fs-1">Login</div>

    <div v-if="loginErrorMessage" class="text-danger">
      {{ loginErrorMessage }}
    </div>


    <!-- Email -->
    <div class="form-outline mb-4">
      <input type="email"
             id="form2Example1"
             class="form-control"
             v-model="email"
             :disabled="isLoading"/>
      <label class="form-label" for="form2Example1">Email address</label>
    </div>

    <!-- Password -->
    <div class="form-outline mb-4">
      <input type="password"
             id="form2Example2"
             class="form-control"
             v-model="password"
             :disabled="isLoading"/>
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
                 @click="userInfoStore.toggleRememberMe()"
                 :disabled="isLoading"
          />
          <label class="form-check-label" for="form2Example31"> Remember me </label>
        </div>
      </div>

      <!-- Forgot password -->
      <div class="col">
        <router-link to="hell">Forgot password?</router-link>
      </div>
    </div>

    <!-- Submit button -->
    <button type="button" class="btn btn-primary btn-block mb-4" @click="login()" :disabled="isLoading">Sign in</button>

    <!-- Other buttons -->
    <div class="text-center">
      <!-- Register User -->
      <p>Not a member?
        <router-link to="register" :disabled="isLoading">Register</router-link>
      </p>

      <!-- Random Account -->
      <div>
        <p class="fs-5">or try the application with a:</p>
        <button class="btn btn-warning" @click="randomLogin()" :disabled="isLoading">Random Account</button>
      </div>
    </div>

  </form>
</div>

</template>

<style scoped>

</style>
<script setup lang="ts">
import {type Ref, ref} from "vue";
import authenticationService from "../services/authentication-service/AuthenticationService";
import userService from "../services/user-service/UserService";
import {useUserInfoStore} from "../stores/UserInfoStore";
import router from "../router";
import {storeToRefs} from "pinia";
import type {AxiosResponse} from "axios";
import type {AuthenticationResponse} from "../services/authentication-service/model/AuthenticationResponse";
import type {RegisterRequest} from "../services/authentication-service/model/RegisterRequest";
import {validateRegisterRequest} from "../services/validation-service/RegisterValidationService";


const userInfoStore = useUserInfoStore();
const {rememberMe} = storeToRefs(userInfoStore);

const firstName = ref("");
const lastName = ref("");
const email = ref("");
const password = ref("");

const isLoading = ref(false);
const registerErrorMessages: Ref<Map<String, String> | null> = ref(null);

function register() {
  isLoading.value = true;
  const registerRequest: RegisterRequest = {
    email: email.value,
    password: password.value,
    lastName: lastName.value,
    firstName: firstName.value
  };

  const a = validateRegisterRequest(registerRequest);
  if (a != null) {
    validationError(a);
    return;
  }

  authenticationService.register(registerRequest)
      .then((res) => success(res))
      .catch((err) => error(err))
      .finally(() => registerFinally());

}

function randomLogin() {
  isLoading.value = true;

  authenticationService.random()
      .then((res) => success(res))
      .catch((err) => error(err))
      .finally(() => registerFinally());
}

function success(res: AxiosResponse<AuthenticationResponse>) {
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

function error(err: any) {
  registerErrorMessages.value = err.response.data;
  setTimeout(() => {
    registerErrorMessages.value = null;
  }, 2000);
}

function validationError(a : Map<string, string>) {
  registerErrorMessages.value = a;
  isLoading.value = false;
  setTimeout(() => {
    registerErrorMessages.value = null;
  }, 2000);
}

function registerFinally() {
  isLoading.value = false;
}

</script>

<template>
  <div class="">
    <form class="pt-5" @submit.prevent style="max-width: 420px">
      <div class="d-flex justify-content-center fs-1">Login</div>

      <div class="d-flex">
        <div class="form-outline mb-4">
          <input type="text"
                 id="first-name"
                 class="form-control"
                 v-model="firstName"
                 :disabled="isLoading"/>
          <label class="form-label" for="first-name"
                 :class="registerErrorMessages?.has('firstName') ? 'text-danger' : ''"
          >{{ registerErrorMessages?.get("firstName") ?? "First Name" }}</label>
        </div>
        <div class="form-outline mb-4">
          <input type="text"
                 id="last-name"
                 class="form-control"
                 v-model="lastName"
                 :disabled="isLoading"/>
          <label class="form-label" for="last-name"
                 :class="registerErrorMessages?.has('lastName') ? 'text-danger' : ''"
          >{{ registerErrorMessages?.get("lastName") ?? "Last Name" }}</label>
        </div>
      </div>

      <!-- Email -->
      <div class="form-outline mb-4">
        <input type="email"
               id="form2Example1"
               class="form-control"
               v-model="email"
               :disabled="isLoading"/>
        <label class="form-label" for="form2Example1"
               :class="registerErrorMessages?.has('email') ? 'text-danger' : ''"
        >{{ registerErrorMessages?.get("email") ?? "Email address" }}</label>
      </div>

      <!-- Password -->
      <div class="form-outline mb-4">
        <input type="password"
               id="form2Example2"
               class="form-control"
               v-model="password"
               :disabled="isLoading"/>
        <label class="form-label" for="form2Example2"
               :class="registerErrorMessages?.has('password') ? 'text-danger' : ''"
        >{{ registerErrorMessages?.get("password") ?? "Password" }}</label>
      </div>

      <div class="d-flex justify-content-between">

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

        </div>

        <!-- Submit button -->
        <button type="button" class="btn btn-primary btn-block mb-4" @click="register()" :disabled="isLoading">Register
        </button>
      </div>


      <!-- Other buttons -->
      <div class="text-center">

        <!-- Register User -->
        <p>Already a member?
          <router-link to="login" :disabled="isLoading">Login</router-link>
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
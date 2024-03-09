import { Injectable } from '@angular/core';
import {UserDTO} from "../http/user-service/model/UserDTO";

@Injectable({
  providedIn: 'root'
})
export class GlobalService {
  baseUrl: string = "http://localhost:8080/api/v1";
  // baseUrl: string = environment.baseApiUrl;
  user: UserDTO | null = null;
  constructor() {
    console.log(this.baseUrl);
  }

}

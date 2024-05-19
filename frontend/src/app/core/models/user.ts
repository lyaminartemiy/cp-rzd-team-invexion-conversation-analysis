import {Implement} from "@angular/cli/lib/config/workspace-schema";

export interface RegisterUser{
  username: string,
  email: string,
  password: string
}
export interface LoginUser{
  usernameOrEmail: string,
  password: string,
}
export interface LoginRequest {
  "isSuccess": true,
  "message": string,
  "token": string,
  "expiresIn": number,
  "user": {
    "username": string,
    "password": string,
    "enabled": boolean,
    "authorities": [
      {
        "authority": string
      }
    ],
    "accountNonExpired": boolean,
    "accountNonLocked": boolean,
    "credentialsNonExpired": boolean
  }
}

export interface User {
  "username": string,
    "password": string,
  email: string,
    "enabled": boolean,
    "authorities": [
    {
      "authority": string
    }
  ],
    "accountNonExpired": boolean,
    "accountNonLocked": boolean,
    "credentialsNonExpired": boolean
}
export interface UserReq {
  "username": string,
  "password": string,
  email: string,
}

import axios from "axios";
import { urlBuilder } from "./common";

const headers = {
  Authorization: `Bearer ${localStorage.getItem("access-token")}`,
};

const userPath = "/api/v1/user";

export async function createUser(userInput) {
  try {
    const response = await axios.post(urlBuilder(userPath), userInput, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("access-token")}`,
      },
    });

    return response.data;
  } catch (error) {
    console.log("에러 데이터 : " + error.data);
    throw error;
  }
}

export async function readUsers() {
  try {
    const response = await axios.get(urlBuilder(userPath), {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("access-token")}`,
      },
    });

    return response.data;
  } catch (error) {
    console.log("에러 데이터 : " + error);
    throw error;
  }
}

export async function updateUser(id, userInput) {
  try {
    const response = await axios.patch(
      urlBuilder(userPath) + `/${id}`,
      userInput,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("access-token")}`,
        },
      }
    );
    return response;
  } catch (error) {
    console.log("에러 데이터 : " + error.data);
    throw error;
  }
}

export async function deleteUser(id) {
  try {
    const response = await axios.delete(urlBuilder(userPath) + `/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("access-token")}`,
      },
    });

    return response.data;
  } catch (error) {
    console.log("에러 데이터 : " + error.data);
    throw error;
  }
}

import axios from "axios";
import { urlBuilder } from "./common";

const headers = {
  Authorization: `Bearer ${localStorage.getItem("access-token")}`,
};

const path = "/api/v1/category";

export async function createCategory(input) {
  try {
    const response = await axios.post(urlBuilder(path), input, {
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

export async function getCategoryList() {
  try {
    const response = await axios.get(urlBuilder(path), {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("access-token")}`,
      },
    });

    return response.data.category;
  } catch (error) {
    console.log("에러 데이터 : " + error);
    throw error;
  }
}

export async function patchCategory(code, input) {
  try {
    const response = await axios.patch(urlBuilder(path) + "/" + code, input, {
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

export async function deleteCategory(code) {
  try {
    const response = await axios.delete(urlBuilder(path) + "/" + code, {
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

import axios from "axios";
import { urlBuilder } from "./common";

const headers = {
  Authorization: `Bearer ${localStorage.getItem("apollo-token")}`,
};

const path = "/api/v1/category";

export async function createCategory(categoryInput) {
  try {
    const response = await axios.post(urlBuilder(path), categoryInput, {
      headers,
    });

    return response.data;
  } catch (error) {
    console.log("에러 데이터 : " + error.data);
    throw error;
  }
}

export async function getCategoryList() {
  try {
    const response = await axios.get(urlBuilder(path + "/list"), {
      headers,
    });

    return response.data;
  } catch (error) {
    console.log("에러 데이터 : " + error);
    throw error;
  }
}

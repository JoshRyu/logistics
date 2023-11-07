import axios from "axios";
import { urlBuilder } from "./common";

const headers = {
  Authorization: `Bearer ${localStorage.getItem("apollo-token")}`,
};

const path = "/api/v1/product";

export async function getProductList() {
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

export async function getProductById(id) {
  try {
    const response = await axios.get(urlBuilder(path + "/" + id), {
      headers,
    });

    return response.data;
  } catch (error) {
    console.log("에러 데이터 : " + error);
    throw error;
  }
}

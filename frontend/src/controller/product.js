import axios from "axios";
import { urlBuilder } from "./common";

const headers = {
  Authorization: `Bearer ${localStorage.getItem("access-token")}`,
};

const path = "/api/v1/product";

export async function createProduct(input) {
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

export async function getProductList(params) {
  try {
    const response = await axios.get(
      urlBuilder(path) +
        `?type=${params.type}&page=${params.page}&size=${params.size}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("access-token")}`,
        },
      }
    );

    return response.data;
  } catch (error) {
    console.log("에러 데이터 : " + error);
    throw error;
  }
}

export async function getProductById(id) {
  try {
    const response = await axios.get(urlBuilder(path + "/" + id), {
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

export async function patchProduct(code, input) {
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

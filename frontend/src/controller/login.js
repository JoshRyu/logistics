import axios from "axios";
import endpoint from "@/config/endpoint.json";

const axiosConfig = {
  headers: {
    "Content-Type": "application/json;charset=UTF-8",
  },
};

export async function login(loginInfo) {
  try {
    const response = await axios.post(
      urlBuilder() + "/api/v1/user/login",
      loginInfo,
      axiosConfig
    );

    return response.data;
  } catch (error) {
    console.log("에러 데이터 : " + error.data);
    throw error;
  }
}

function urlBuilder() {
  return endpoint.protocol + "://" + endpoint.url + ":" + endpoint.port;
}

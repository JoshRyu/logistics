import decode from "jwt-decode";
import axios from "axios";
import { urlBuilder } from "@/controller/common";

const headers = {
  Authorization: `Bearer ${localStorage.getItem("refresh-token")}`,
};

const path = "/api/v1/user";

export function requireAuth(to, from, next) {
  try {
    if (isAccessTokenValid()) {
      // Access token is valid, proceed
      handleUserRoleAuthorization(to, next);
    } else if (isRefreshTokenValid()) {
      // Access token expired, refresh it
      refreshAccessToken()
        .then(() => {
          // Redirect to the original path after successful refresh
          next({ path: to.fullPath });
        })
        .catch((error) => {
          // Handle refresh token error
          console.error("Error refreshing access token:", error);
          next({ path: "/login", query: { refreshError: true } });
        });
    } else {
      // No valid tokens, redirect to login
      next({ path: "/login", query: { redirect: to.fullPath } });
    }
  } catch (error) {
    // Handle general errors
    console.error("Authentication error:", error);
    next({ path: "/error/500" });
  }
}

function handleUserRoleAuthorization(to, next) {
  try {
    const userRole = getUserRole();

    if (to.meta.role && !to.meta.role.includes(userRole)) {
      // Unauthorized access
      next({ path: "/error/403" });
      return;
    }

    // Authorized access, proceed
    next();
  } catch (error) {
    // Handle errors during role authorization
    console.error("Error authorizing user role:", error);
    next({ path: "/error/500" }); // Redirect to a generic error page
  }
}

function isAccessTokenValid() {
  const accessToken = getAccessToken();
  return !!accessToken && !isTokenExpired(accessToken);
}

function isRefreshTokenValid() {
  const refreshToken = getRefreshToken();
  return !!refreshToken && !isTokenExpired(refreshToken);
}

function getAccessToken() {
  return localStorage.getItem("access-token");
}

function getRefreshToken() {
  return localStorage.getItem("refresh-token");
}

async function refreshAccessToken() {
  const refreshToken = getRefreshToken();

  try {
    const response = await axios.get(
      urlBuilder(path) + "/refresh?refreshToken=" + refreshToken,
      {
        headers,
      }
    );

    localStorage.setItem("access-token", response.data.accessToken);
  } catch (error) {
    console.log("에러 데이터 : " + error);
    throw error;
  }
}

function getTokenExpirationDate(encodedToken) {
  const token = decode(encodedToken);
  if (!token.exp) {
    return null;
  }
  const date = new Date(0);
  date.setUTCSeconds(token.exp);
  return date;
}

function isTokenExpired(token) {
  const expirationDate = getTokenExpirationDate(token);
  return expirationDate < new Date();
}

function getUserRole() {
  const idToken = getAccessToken();
  return decode(idToken).role;
}

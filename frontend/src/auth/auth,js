import decode from "jwt-decode";

export function requireAuth(to, from, next) {
  if (!isLoggedIn()) {
    next({
      path: "/login",
      query: { redirect: to.fullPath },
    });
  } else {
    if (to.meta.role) {
      if (to.meta.role.includes(getUserRole())) {
        next();
      } else {
        next({
          path: "/error/403",
        });
      }
    } else {
      next();
    }
  }
}

function isLoggedIn() {
  const idToken = getIdToken();
  return !!idToken && !isTokenExpired(idToken);
}

function getIdToken() {
  return localStorage.getItem("apollo-token");
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
  const idToken = getIdToken();
  return decode(idToken).sub;
}

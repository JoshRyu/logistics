export function urlBuilder(path) {
  const baseURL = import.meta.env.VITE_APP_API_URL;
  return baseURL + path;
}

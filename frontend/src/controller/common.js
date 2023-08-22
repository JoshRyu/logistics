import endpoint from "@/config/endpoint.json";

export function urlBuilder(path) {
  return endpoint.protocol + "://" + endpoint.url + ":" + endpoint.port + path;
}

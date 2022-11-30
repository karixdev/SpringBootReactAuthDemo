const BASE_URL = 'http://localhost:8080';

export const buildUri = endpoint => {
  return BASE_URL + endpoint;
}

export const buildUriAuth = endpoint => {
  return buildUri('/api/auth' + endpoint);
}


// process.env.REACT_APP_HOST_IP_ADDRESS
const baseUrl = "http://172.21.0.5:8080"

export const loginEndpoint = baseUrl + "/api/v1/auth/authenticate"
export const registerEndpoint = baseUrl + "/api/v1/auth/register"
export const getAllClients = baseUrl+ "/api/v1/client/"
export const ssh =  baseUrl+ "/api/v1/client/ssh"
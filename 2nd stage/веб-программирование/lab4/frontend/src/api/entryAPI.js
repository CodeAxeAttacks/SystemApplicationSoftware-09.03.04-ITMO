import axios from 'axios';
import keycloak from "../Keycloack";

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8081/web-lab4/api/entries/'
});

const entryAPI = {
  async getEntries(username, token) {
    return axiosInstance.get('',  {
      headers: {
        Authorization: 'Bearer ' + token
      },
      params: {
        username: username
      }
    });
  },

  async checkEntry(x, y, r, username, token) {
    return axiosInstance.post('', {x, y, r, username}, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    });
  },

  async clearEntries(username, token) {
    return axiosInstance.delete('', {
      headers: {
        Authorization: 'Bearer ' + token
      },
      params: {
        username: username
      }
    });
  }
}

export default entryAPI;

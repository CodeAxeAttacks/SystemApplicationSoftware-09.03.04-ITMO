import Keycloak from "keycloak-js";
const keycloak = new Keycloak({
  url: "http://localhost:8080",
  realm: "web_lab4",
  clientId: "web_lab4_rest_api",
});

export default keycloak;

import React from 'react';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import MainPage from '../pages/MainPage';
import Nav from "../components/Nav"
import keycloak from "../Keycloack"
import PrivateRoute from "../helpers/PrivateRoute"


function App() {
  return (
    <div>
      <ReactKeycloakProvider authClient={keycloak}>
        <Nav />
        <BrowserRouter>
          <Switch>
            <Route
              path="/"
              element={
                <PrivateRoute>
                  <MainPage />
                </PrivateRoute>
              }
            />
          </Switch>
        </BrowserRouter>
      </ReactKeycloakProvider>
    </div>
  );
}

export default App;

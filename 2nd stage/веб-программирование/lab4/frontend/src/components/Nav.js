import React, {useEffect} from "react";
import { useKeycloak } from "@react-keycloak/web";
import {useNavigate} from "react-router";
import HeaderContainer from "./Header/HeaderContainer";
import TitleHelmet from "./common/TitleHelmet";
import MainContent from "./MainContent/MainContent";
import keycloak from "../Keycloack";


const Nav = () => {
  const { keycloak, initialized } = useKeycloak()


    const handleLogout = () => {
        keycloak.logout()

        sessionStorage.removeItem('token')
    };

  return (
    <div>
      <div className="top-0 w-full flex flex-wrap">
        <section className="x-auto">
          <nav className="flex justify-between bg-gray-200 text-blue-800 w-screen">
            <div className="px-5 xl:px-12 py-6 flex w-full items-center">
              <div className="hidden xl:flex items-center space-x-5">
                <div className="hover:text-gray-200">
                  {!keycloak.authenticated && (

                    <div>
                      <h1 className="text-3xl font-bold font-heading">
                        <HeaderContainer/>
                      </h1>
                      <button
                        type="button"
                        className="text-blue-800"
                        onClick={() => keycloak.login()}
                      >
                        Login
                      </button>
                    </div>
                  )}

              {!!keycloak.authenticated && (
                <div>
                  <HeaderContainer />
                  <TitleHelmet title="Лабораторная работа №4 - Основная страница" />
                  <MainContent />

                  <button
                    type="button"
                    className="text-blue-800"
                    onClick={handleLogout}
                  >
                    Logout ({keycloak.tokenParsed.preferred_username})
                  </button>
                </div>

                  )}
                </div>
              </div>
            </div>
          </nav>
        </section>
      </div>
    </div>
  );
};

export default Nav;

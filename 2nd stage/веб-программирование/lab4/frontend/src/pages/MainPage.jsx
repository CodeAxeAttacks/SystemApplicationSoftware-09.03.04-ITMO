import React, {useEffect, useState} from "react";
import HeaderContainer from "../components/Header/HeaderContainer";
import TitleHelmet from "../components/common/TitleHelmet";
import MainContent from "../components/MainContent/MainContent";

const Main = () => {
    return (
      <div>
          <HeaderContainer />
          <TitleHelmet title="Лабораторная работа №4 - Основная страница" />
          <MainContent />
      </div>
    );
}

export default Main;

import React from 'react';
import CSSModules from 'react-css-modules';
import styles from './MainContent.module.css';
import sharedStyles from './main.module.css';
import GraphSection from './GraphSection/GraphSection';
import ValuesSection from './ValuesSection/ValuesSection';
import TableSectionContainer from './TableSection/TableSectionContainer';

const MainContent = (props) => {
  return (
    <main styleName="main-container">
      <h1 className="visually-hidden">Лабораторная работа №4 - Основная страница</h1>
      <div styleName="main-container__item main-container__item_left-column column-container">
        <GraphSection />
        <ValuesSection />
      </div>
      <TableSectionContainer />
    </main>
  );
}

export default CSSModules(MainContent, { ...styles, ...sharedStyles }, { allowMultiple: true, handleNotFoundStyleName: 'ignore' });

import React from 'react';
import CSSModules from 'react-css-modules';
import styles from './ValuesSection.module.css';
import sharedStyles from '../main.module.css';
import ValuesFormContainer from './ValuesForm/ValuesFormContainer';

const ValuesSection = (props) => {
  return (
    <section styleName="column-container__item section" className="content-section">
      <h2 className="theme section-header">
        <span className="section-header__text">
          Данные
        </span>
      </h2>
      <ValuesFormContainer />
    </section>
  );
}

export default CSSModules(ValuesSection, { ...styles, ...sharedStyles }, { allowMultiple: true, handleNotFoundStyleName: 'ignore' });

import React from 'react';
import CSSModules from 'react-css-modules';
import styles from './Preloader.module.css';
import preloader from 'assets/preloader.svg';

const Preloader = (props) => {
  return (
    <div styleName="preloader-container">
      <img src={preloader} alt="Загрузка..."/>
    </div>
  );
}

export default CSSModules(Preloader, styles, { allowMultiple: true, handleNotFoundStyleName: 'ignore' });

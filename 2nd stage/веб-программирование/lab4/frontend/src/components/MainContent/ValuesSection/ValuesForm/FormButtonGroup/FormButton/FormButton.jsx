import React from 'react';
import CSSModules from 'react-css-modules';
import styles from './FormButton.module.css';

const FormButton = (props) => {
  return (
    <button styleName={(props.valueCurrent === props.value) ? "form-button form-button_active" : "form-button"}
      type="button" onClick={() => props.selectValue(props.value)}>
      {props.value}
    </button>
  );
}

export default CSSModules(FormButton, styles, { allowMultiple: true, handleNotFoundStyleName: 'ignore' });

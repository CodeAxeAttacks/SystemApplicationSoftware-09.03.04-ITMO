import React from 'react';
import CSSModules from 'react-css-modules';
import styles from './FormButtonGroup.module.css';
import FormButton from './FormButton/FormButton';

const FormButtonGroup = (props) => {
  return (
    <ul styleName="button-list">
      {props.groupValues.map(value => <li styleName="button-list__item">
        <FormButton value={value} valueCurrent={props.valueCurrent} selectValue={props.selectValue} />
      </li>)}
    </ul>
  );
}

export default CSSModules(FormButtonGroup, styles, { allowMultiple: true, handleNotFoundStyleName: 'ignore' });

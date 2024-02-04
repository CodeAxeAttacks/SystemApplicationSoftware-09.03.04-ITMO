import { connect } from 'react-redux';
import ValuesForm from './ValuesForm'
import {
  selectR,
  selectX,
  changeY,
  checkEntry,
  clearEntries
} from 'redux/modules/values';
import {getEntries} from "../../../../redux/modules/table";

function mapStateToProps(state) {
  return {
    rValues: state.values.rValues,
    rCurrent: state.values.rCurrent,
    xValues: state.values.xValues,
    xCurrent: state.values.xCurrent,
    yMin: state.values.yMin,
    yMax: state.values.yMax,
    yCurrent: state.values.yCurrent
  };
}

function mapDispatchToProps(dispatch) {
  return {
    selectR: (value) => dispatch(selectR(value)),
    selectX: (value) => dispatch(selectX(value)),
    changeY: (value) => dispatch(changeY(value)),
    checkEntry: () => dispatch(checkEntry()),
    clearEntries: () => dispatch(clearEntries()),
    getEntries: () => dispatch(getEntries())
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ValuesForm);

import { combineReducers } from 'redux';
import valuesReducer from './modules/values';
import tableReducer from './modules/table';

const reducer = combineReducers({
  values: valuesReducer,
  table: tableReducer,
});

export default reducer;

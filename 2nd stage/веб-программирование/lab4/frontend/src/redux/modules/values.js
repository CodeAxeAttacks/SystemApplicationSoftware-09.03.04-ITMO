import entryAPI from 'api/entryAPI';
//import { logout } from './auth';
import { setEntries, addEntry } from './table';
import keycloack from "../../Keycloack";

const SELECT_R = 'web-lab4/values/SELECT_R';
const SELECT_X = 'web-lab4/values/SELECT_X';
const CHANGE_Y = 'web-lab4/values/CHANGE_Y';
const CLEAR_CURRENT = 'web-lab4/values/CLEAR_CURRENT';

const initialState = {
  rValues: [-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2],
  rCurrent: 1,
  xValues: [-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2],
  xCurrent: undefined,
  yMin: -3,
  yMax: 3,
  yCurrent: undefined
};

export default function valuesReducer(state = initialState, action = {}) {
  switch (action.type) {
    case SELECT_R:
      return Object.assign(
        {},
        state,
        {
          rCurrent: action.value
        }
      );
    case SELECT_X:
      return Object.assign(
        {},
        state,
        {
          xCurrent: action.value
        }
      );
    case CHANGE_Y:
      return Object.assign(
        {},
        state,
        {
          yCurrent: action.value
        }
      );
    case CLEAR_CURRENT:
      return Object.assign(
        {},
        state,
        {
          rCurrent: 1,
          xCurrent: undefined,
          yCurrent: undefined
        }
      );
    default:
      return state;
  }
}

export function selectR(value) {
  return { type: SELECT_R, value };
}

export function selectX(value) {
  return { type: SELECT_X, value };
}

export function changeY(value) {
  return { type: CHANGE_Y, value };
}

export function clearCurrent() {
  return { type: CLEAR_CURRENT };
}

export const checkEntry = () => (dispatch, getState) => {
    sessionStorage.setItem("token", keycloack.token);
  entryAPI.checkEntry(
    getState().values.xCurrent,
    getState().values.yCurrent,
    getState().values.rCurrent,
      keycloack.tokenParsed.preferred_username,
      keycloack.token)
    .then(response => {
        dispatch(addEntry(response.data));
    })
    .catch(error => {
        alert(`Непредвиденный ответ ${error.response.status} от сервера!`);
       //}
    });;
}

export const clearEntries = () => (dispatch) => {
  entryAPI.clearEntries(keycloack.tokenParsed.preferred_username, keycloack.token)
    .then(response => {
        dispatch(setEntries([]));
    })
    .catch(error => {
    });;
}

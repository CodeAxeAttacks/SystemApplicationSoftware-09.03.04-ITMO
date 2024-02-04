import entryAPI from 'api/entryAPI';
import keycloack from "../../Keycloack";

const SET_ENTRIES = 'web-lab4/table/SET_ENTRIES';
const CLEAR_ENTRIES = 'web-lab4/table/CLEAR_ENTRIES';
const ADD_ENTRY = 'web-lab4/table/ADD_ENTRY';

const initialState = {
  entries: []
};

export default function tableReducer(state = initialState, action = {}) {
  switch (action.type) {
    case SET_ENTRIES:
      return Object.assign(
        {},
        state,
        {
          entries: action.value
        }
      );
    case CLEAR_ENTRIES:
      return Object.assign(
        {},
        state,
        {
          entries: []
        }
      );
    case ADD_ENTRY:
      return Object.assign(
        {},
        state,
        {
          entries: [...state.entries, action.value]
        }
      );
    default:
      return state;
  }
}

export function setEntries(value) {
  return { type: SET_ENTRIES, value };
}

export function clearEntries() {
  return { type: CLEAR_ENTRIES };
}

export function addEntry(value) {
  return { type: ADD_ENTRY, value };
}


export const getEntries = () => (dispatch) => {
  entryAPI.getEntries(keycloack.tokenParsed.preferred_username, keycloack.token)
      .then(response => {
        if (response.status === 200) {
          dispatch(setEntries(response.data));
        } else {
          alert(`Непредвиденный ответ ${response.status} от сервера!`);
        }
      })
      .catch(error => {
        if (error.response.status === 401) {
        } else {
          alert(`Непредвиденный ответ ${error.response.status} от сервера!`);
        }
      });;
}

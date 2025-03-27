import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage'; // для использования localStorage

// Интерфейс состояния пользователя
interface UserState {
    id: number;
    roles: string[];
    username: string
}

// Начальное состояние
const initialState: UserState = {
    id: 0,
    roles: [],
    username: ""
};

// Создание slice для пользователя
const userSlice = createSlice({
    name: 'user',
    initialState,
    reducers: {
        setUser: (state, action: PayloadAction<UserState>) => {
            state.id = action.payload.id;
            state.roles = action.payload.roles;
            state.username = action.payload.username
        },
        clearUser: (state) => {
            state.id = 0;
            state.roles = [];
            state.username = ""
        }
    }
});

// Экспортируем экшены
export const { setUser, clearUser } = userSlice.actions;

// Конфигурация persist
const persistConfig = {
    key: 'user',
    storage,
};

// Экспортируем редьюсер с persist
export default persistReducer(persistConfig, userSlice.reducer);

import React from "react";
import './App.css'
import { Navigate, Outlet, BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginForm from "./components/auth/LoginForm.tsx";
import RegisterForm from "./components/auth/RegisterForm.tsx";
import MainScreen from "./components/MainScreen.tsx";
import {Provider} from "react-redux";
import {persistor, store} from "./stores/store.ts";
import {PersistGate} from "redux-persist/integration/react";
import AdminPanel from "./components/AdminPanel.tsx";

const PrivateRoute = () => {
    const token = localStorage.getItem('accessToken');

    return token ? <Outlet /> : <Navigate to="/login" />;
};

const AdminRoute = () => {
    const user = localStorage.getItem('persist:user')

    return PrivateRoute() && user?.includes("ADMIN") ? <Outlet/> : <Navigate to="/login" />
}

const App: React.FC = () => {
    return (
        <Provider store={store}>
            <PersistGate loading={null} persistor={persistor}>
                <Router>
                    <Routes>
                        <Route path="/" element={<LoginForm />} />
                        <Route path="/login" element={<LoginForm />} />
                        <Route path="/register" element={<RegisterForm />} />

                        <Route element={<PrivateRoute />}>
                            <Route path="/main-screen" element={<MainScreen />} />
                        </Route>

                        <Route element={<AdminRoute />}>
                            <Route path="/admin-panel" element={<AdminPanel />} />
                        </Route>
                    </Routes>
                </Router>
            </PersistGate>
        </Provider>
    );
};

export default App

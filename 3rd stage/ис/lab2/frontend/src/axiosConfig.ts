import axios from "axios";

// const token = localStorage.getItem('token')

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/',
    headers: {
        'Content-Type': 'application/json',
    }
})


axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`; // Добавляем токен в заголовок Authorization
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('accessToken');

            const currentPath = window.location.pathname;

            // Проверяем, что текущая страница не "/" и не "/login"
            if (currentPath !== '/' && currentPath !== '/login') {
                window.location.href = '/';
            }
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;
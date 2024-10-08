import axios from 'axios';
import { getToken } from './authService';

// Default path
const API_URL = 'http://localhost:8080/api/user';

// Create an Axios instance with default settings
const axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    }
});

// Add a request interceptor to include the token in every request
axiosInstance.interceptors.request.use(
    (config) => {
        const token = getToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Fetch balance
export const getBalance = async () => {
    try {
        const response = await axiosInstance.get('/balance');
        return response.data;
    } catch (error) {
        console.error("Error fetching balance:", error);
        throw error;
    }
};

// Add balance
export const addBalance = async (amount) => {
    try {
        const response = await axiosInstance.post('/balance/add', { amount });
        return response.data;
    } catch (error) {
        console.error("Error adding balance:", error);
        throw error;
    }
};

// Fetch personal information
export const getPersonalInfo = async () => {
    try {
        const response = await axiosInstance.get('/profile/personal-info');
        return response.data;
    } catch (error) {
        console.error("Error fetching personal information:", error);
        throw error;
    }
};

// Update personal information
export const updatePersonalInfo = async (personalInfo) => {
    try {
        const response = await axiosInstance.put('/profile/personal-info', personalInfo);
        return response.data;
    } catch (error) {
        console.error("Error updating personal information:", error);
        throw error;
    }
};

// Change password
export const changePassword = async (currentPassword, newPassword) => {
    try {
        const response = await axiosInstance.post('/security/change-password', {
            currentPassword,
            newPassword
        });
        return response.data;
    } catch (error) {
        console.error("Error changing password:", error);
        throw error;
    }
};
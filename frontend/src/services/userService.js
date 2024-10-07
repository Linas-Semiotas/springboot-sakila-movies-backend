import axios from 'axios';
import { getToken } from './authService';

const API_URL = 'http://localhost:8080/api/user';

export const getBalance = async () => {
    const token = getToken();
    const response = await axios.get(`${API_URL}/balance`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
    return response.data;
};

export const addBalance = async (amount) => {
    const token = getToken();
    const response = await axios.post(`${API_URL}/balance/add`, { amount }, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
    return response.data;
};

export const changePassword = async (currentPassword, newPassword) => {
    const token = getToken();
    const response = await axios.post(`${API_URL}/change-password`, {
        currentPassword,
        newPassword
    }, {
        headers: {
            Authorization: `Bearer ${token}`,
        }
    });
    return response.data;
};
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

export const login = async (username, password) => {
    const response = await axios.post(`${API_URL}/login`, {
        username,
        password
    }, {
        withCredentials: true  // Include credentials like cookies or tokens if necessary
    });
    return response.data;
};

export const register = async (username, password, firstName, lastName, email, storeId) => {
    const response = await axios.post(`${API_URL}/register`, {
        username,
        password,
        firstName,
        lastName,
        email,
        storeId
    });
    return response;
};

export const getToken = () => {
    return localStorage.getItem('token');
};

export const logout = async () => {
    localStorage.removeItem('token');
};

// export const logout = async () => {
//     const token = getToken();
//     if (token) {
//         try {
//             await axios.post(`${API_URL}/logout`, {}, {
//                 headers: {
//                     Authorization: `Bearer ${token}`
//                 },
//                 withCredentials: true
//             });
//         } catch (error) {
//             console.error("Logout error:", error);
//         } finally {
//             localStorage.removeItem('token');
//         }
//     }
// };
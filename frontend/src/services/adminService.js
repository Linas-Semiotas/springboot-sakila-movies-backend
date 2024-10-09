import createAxiosInstance from './axiosInstance';

const axiosInstance = createAxiosInstance('/api/admin');

// USERS
// Fetch all users
export const fetchUsers = async () => {
    try {
        const response = await axiosInstance.get('/users');
        return response.data;
    } catch (error) {
        console.error("Error fetching users:", error);
        throw error;
    }
};

// Update a specific user
export const updateUser = async (userId, updatedUserData) => {
    try {
        const response = await axiosInstance.put(`/users/${userId}`, updatedUserData);
        return response.data;
    } catch (error) {
        console.error("Error updating user:", error);
        throw error;
    }
};

//LANGUAGES
export const fetchLanguages = async () => {
    const response = await axiosInstance.get('/languages');
    return response.data;
};

export const addLanguage = async (name) => {
    const response = await axiosInstance.post('/languages', { name });
    return response.data;
};

export const deleteLanguage = async (languageId) => {
    const response = await axiosInstance.delete(`/languages/${languageId}`);
    return response.data;
};
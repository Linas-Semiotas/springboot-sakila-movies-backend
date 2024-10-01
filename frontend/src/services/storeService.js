import axios from 'axios';

const API_URL = 'http://localhost:8080/stores';

const getAllStores = () => {
    return axios.get(`${API_URL}`)
        .then(response => response.data)
        .catch(error => {
            console.error('Error fetching stores:', error.response ? error.response.data : error.message);
            throw error;
        });
};

const storeService = {
    getAllStores
};

export default storeService;